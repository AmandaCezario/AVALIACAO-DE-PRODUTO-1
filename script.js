    //LOGIN//

const STORAGE_KEYS = {
  currentUser: 'index:user',
  historyPrefix: 'index:historico:'
};

const API_BASE_URL = 'http://localhost:8080';

const USERS = {
  admin: 'admin123',
  loja: 'czr4212'
};

export function normalizeUsername(value = '') {
  return String(value).trim().toLowerCase();
}

export function buildBasicAuthHeader(username, password) {
  const encoded = btoa(`${normalizeUsername(username)}:${String(password)}`);
  return `Basic ${encoded}`;
}

export function getCurrentUser() {
  return localStorage.getItem(STORAGE_KEYS.currentUser) || '';
}

function getHistoryKey(user = getCurrentUser()) {
  return `${STORAGE_KEYS.historyPrefix}${user || 'guest'}`;
}

function mostrarErro(mensagem) {
  const msgErro = document.getElementById('msgErro');
  if (msgErro) {
    msgErro.textContent = mensagem;
  }
}

async function storageGet(key) {
  try {
    if (window.storage && typeof window.storage.get === 'function') {
      const result = await window.storage.get(key, false);
      if (result && typeof result.value !== 'undefined') {
        return result.value;
      }
    }

    return localStorage.getItem(key);
  } catch (error) {
    console.error('Erro ao ler storage:', error);
    return null;
  }
}

async function storageSet(key, value) {
  try {
    if (window.storage && typeof window.storage.set === 'function') {
      return await window.storage.set(key, String(value), false);
    }

    localStorage.setItem(key, String(value));
    return true;
  } catch (error) {
    console.error('Erro ao salvar storage:', error);
    return false;
  }
}

function formatMoney(value) {
  return 'R$ ' + Number(value || 0).toLocaleString('pt-BR', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
}

export function calculateFinalPrice({
  custo = 0,
  frete = 0,
  embalagem = 0,
  taxaCartao = 0,
  taxaPlataforma = 0,
  imposto = 0,
  margem = 0
}) {
  const custoBase = Number(custo) + Number(frete) + Number(embalagem);
  const percentuaisSobreVenda = (Number(taxaCartao) + Number(taxaPlataforma) + Number(imposto) + Number(margem)) / 100;

  let precoFinal = 0;
  if (percentuaisSobreVenda < 1 && custoBase > 0) {
    precoFinal = custoBase / (1 - percentuaisSobreVenda);
  }

  const custoTotal = custoBase
    + precoFinal * (Number(taxaCartao) / 100)
    + precoFinal * (Number(taxaPlataforma) / 100)
    + precoFinal * (Number(imposto) / 100);

  const lucro = precoFinal - custoTotal;

  return {
    custoBase,
    precoFinal,
    custoTotal,
    lucro,
    custo: Number(custo),
    frete: Number(frete),
    embalagem: Number(embalagem),
    taxaCartao: Number(taxaCartao),
    taxaPlataforma: Number(taxaPlataforma),
    imposto: Number(imposto),
    margem: Number(margem)
  };
}

async function authenticateWithApi(username, password) {
  const response = await fetch(`${API_BASE_URL}/api/usuarios`, {
    method: 'GET',
    headers: {
      Authorization: buildBasicAuthHeader(username, password),
      'Content-Type': 'application/json'
    },
    mode: 'cors'
  });

  if (!response.ok) {
    throw new Error('Credenciais inválidas.');
  }

  return response;
}

function bindLoginPage() {
  const loginForm = document.getElementById('loginForm');

  if (!loginForm) return;

  if (getCurrentUser()) {
    window.location.href = 'calculadora.html';
    return;
  }

  const usuario = document.getElementById('usuario');
  const senha = document.getElementById('senha');

  loginForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const user = normalizeUsername(usuario.value);
    const pass = senha.value;

    if (!user || !pass) {
      mostrarErro('Preencha usuário e senha.');
      return;
    }

    try {
      await authenticateWithApi(user, pass);
      localStorage.setItem(STORAGE_KEYS.currentUser, user);
      localStorage.setItem('usuarioLogado', user);
      window.location.href = 'calculadora.html';
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Usuário ou senha inválidos.';
      mostrarErro(message);
    }
  });
}

 //PAGINA INICIAL CALCULADORA//

function bindCalculatorPage() {
  const currentUser = getCurrentUser();
  if (!currentUser) {
    window.location.href = 'index.html';
    return;
  }

  const username = document.getElementById('usuarioLogado');
  if (username) {
    username.textContent = `Usuário: ${currentUser}`;
  }

  const logoutBtn = document.getElementById('btnLogout');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      console.log('cliquei sair');
      localStorage.removeItem(STORAGE_KEYS.currentUser);
      window.location.assign('index.html');
    });
  }

  const $ = (id) => document.getElementById(id);

  document.querySelectorAll('.tab').forEach((btn) => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.tab').forEach((b) => b.classList.remove('active'));
      document.querySelectorAll('main section').forEach((s) => s.classList.remove('active'));
      btn.classList.add('active');
      const target = $('tab-' + btn.dataset.tab);
      if (target) {
        target.classList.add('active');
      }
      if (btn.dataset.tab === 'historico') {
        carregarHistorico();
      }
    });
  });

  document.querySelectorAll('.margem-btn').forEach((btn) => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.margem-btn').forEach((b) => b.classList.remove('active'));
      btn.classList.add('active');
      $('margem').value = btn.dataset.margem;
      calcular();
    });
  });

  $('margem').addEventListener('input', () => {
    document.querySelectorAll('.margem-btn').forEach((b) => b.classList.remove('active'));
    calcular();
  });


  // Função para calcular o preço final, custo total e lucro//
  function calcular() {
    const custo = Number.parseFloat($('custo').value) || 0;
    const frete = Number.parseFloat($('frete').value) || 0;
    const embalagem = Number.parseFloat($('embalagem').value) || 0;
    const taxaCartao = Number.parseFloat($('taxaCartao').value) || 0;
    const taxaPlataforma = Number.parseFloat($('taxaPlataforma').value) || 0;
    const imposto = Number.parseFloat($('imposto').value) || 0;
    const margem = Number.parseFloat($('margem').value) || 0;

    const dados = calculateFinalPrice({
      custo,
      frete,
      embalagem,
      taxaCartao,
      taxaPlataforma,
      imposto,
      margem
    });

    $('valorFinal').textContent = formatMoney(dados.precoFinal);
    $('custoTotalMini').textContent = formatMoney(dados.custoTotal);
    $('lucroMini').textContent = formatMoney(dados.lucro);

    $('btnSalvar').disabled = !(custo > 0 && dados.precoFinal > 0);

    return dados;
  }

  ['custo', 'frete', 'embalagem', 'taxaCartao', 'taxaPlataforma', 'imposto'].forEach((id) => {
    $(id).addEventListener('input', calcular);
  });

  $('btnSalvar').addEventListener('click', async () => {
    const nome = $('nome').value.trim() || 'Mercadoria sem nome';
    const dados = calcular();

    if (!(dados.custo > 0 && dados.precoFinal > 0)) return;

    const item = {
      id: 'item_' + Date.now(),
      nome,
      custo: dados.custo,
      frete: dados.frete,
      embalagem: dados.embalagem,
      taxaCartao: dados.taxaCartao,
      taxaPlataforma: dados.taxaPlataforma,
      imposto: dados.imposto,
      margem: dados.margem,
      precoFinal: dados.precoFinal,
      lucro: dados.lucro,
      data: new Date().toISOString()
    };

    try {
      const listaAtual = await pegarLista();
      listaAtual.unshift(item);
      const ok = await storageSet(getHistoryKey(currentUser), JSON.stringify(listaAtual));

      if (ok) {
        $('msgSalvo').textContent = 'Salvo! ✓';
        setTimeout(() => {
          $('msgSalvo').textContent = '';
        }, 2000);
        $('nome').value = '';
      } else {
        $('msgSalvo').textContent = 'Não deu pra salvar, tenta de novo.';
      }
    } catch (error) {
      console.error(error);
      $('msgSalvo').textContent = 'Não deu pra salvar, tenta de novo.';
    }
  });

  // Função para pegar a lista do histórico do usuário atual //

  async function pegarLista() {
    try {
      const raw = await storageGet(getHistoryKey(currentUser));
      return raw ? JSON.parse(raw) : [];
    } catch (error) {
      console.error('Erro ao carregar histórico:', error);
      return [];
    }
  }

  // Função para carregar o histórico do usuário atual //
  
  async function carregarHistorico() {
    const lista = await pegarLista();
    const container = $('listaHistorico');
    const resumo = $('resumoHist');

    if (lista.length === 0) {
      resumo.style.display = 'none';
      container.innerHTML = '<div class="vazio"><div class="icone">🏷️</div><p>Nenhuma mercadoria salva ainda.<br>Calcule um preço e salve aqui.</p></div>';
      return;
    }

    resumo.style.display = 'flex';
    $('totalItens').textContent = lista.length;
    const lucroTotal = lista.reduce((soma, item) => soma + Number(item.lucro || 0), 0);
    $('totalLucro').textContent = formatMoney(lucroTotal);

    container.innerHTML = lista.map((item) => `
      <div class="hist-item" data-id="${item.id}">
        <button class="apagar" data-id="${item.id}" type="button">✕</button>
        <p class="nome">${item.nome}</p>
        <div class="linha"><span>Comprou por</span><b>${formatMoney(item.custo)}</b></div>
        <div class="linha"><span>Vender por</span><b>${formatMoney(item.precoFinal)}</b></div>
        <div class="linha"><span>Lucro (${item.margem}%)</span><b class="lucro">${formatMoney(item.lucro)}</b></div>
      </div>
    `).join('');

    container.querySelectorAll('.apagar').forEach((btn) => {
      btn.addEventListener('click', async () => {
        const id = btn.dataset.id;
        const listaAtual = await pegarLista();
        const novaLista = listaAtual.filter((item) => item.id !== id);
        await storageSet(getHistoryKey(currentUser), JSON.stringify(novaLista));
        carregarHistorico();
      });
    });
  }

  calcular();
}

document.addEventListener('DOMContentLoaded', () => {
  const body = document.body;
  if (body.dataset.page === 'login') {
    bindLoginPage();
    return;
  }

  bindCalculatorPage();
});
