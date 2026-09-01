const STORAGE_KEYS = {
  currentUser: 'precificacaocao:user',
  historyPrefix: 'precificacaocao:historico:'
};

const USERS = {
  admin: '12420628',
  loja: 'czr4212'
};

function getCurrentUser() {
  return localStorage.getItem(STORAGE_KEYS.currentUser) || '';
}

function getHistoryKey(user = getCurrentUser()) {
  return `${STORAGE_KEYS.historyPrefix}${user || 'guest'}`;
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

function bindLoginPage() {
  const loginForm = document.getElementById('loginForm');
  const msgErro = document.getElementById('msgErro');

  if (!loginForm) return;

  if (getCurrentUser()) {
    window.location.href = 'precificacao.html';
    return;
  }

  const usuario = document.getElementById('usuario')
  const senha = document.getElementById('senha')
  const form = document.getElementById('loginForm')

  form.addEventListener('submit', async function (event) {
    event.preventDefault()

    const user = usuario.value.trim()
    const pass = senha.value.trim()

    if (!user || !pass) {
      mostrarErro('Preencha usuário e senha.')
      return
    }

    const { data, error } = await supabase.auth.signInWithPassword({
      email: user,
      password: pass
    })

    if (error) {
      mostrarErro(error.message)
      return
    }

    localStorage.setItem(STORAGE_KEYS.currentUser, user);
    localStorage.setItem('usuarioLogado', user)
    window.location.href = 'precificacao.html';
  })
}

function bindCalculatorPage() {
  const currentUser = getCurrentUser();
  if (!currentUser) {
    window.location.href = 'login.html';
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
      window.location.assign('login.html');
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

  function calcular() {
    const custo = Number.parseFloat($('custo').value) || 0;
    const frete = Number.parseFloat($('frete').value) || 0;
    const taxaCartao = Number.parseFloat($('taxaCartao').value) || 0;
    const imposto = Number.parseFloat($('imposto').value) || 0;
    const margem = Number.parseFloat($('margem').value) || 0;

    const custoBase = custo + frete;
    const percentuaisSobreVenda = (taxaCartao + imposto + margem) / 100;

    let precoFinal = 0;
    if (percentuaisSobreVenda < 1 && custoBase > 0) {
      precoFinal = custoBase / (1 - percentuaisSobreVenda);
    }

    const custoTotal = custoBase + precoFinal * (taxaCartao / 100) + precoFinal * (imposto / 100);
    const lucro = precoFinal - custoTotal;

    $('valorFinal').textContent = formatMoney(precoFinal);
    $('custoTotalMini').textContent = formatMoney(custoTotal);
    $('lucroMini').textContent = formatMoney(lucro);

    $('btnSalvar').disabled = !(custo > 0 && precoFinal > 0);

    return { precoFinal, custoTotal, lucro, custo, frete, taxaCartao, imposto, margem };
  }

  ['custo', 'frete', 'taxaCartao', 'imposto'].forEach((id) => {
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
      taxaCartao: dados.taxaCartao,
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

  async function pegarLista() {
    try {
      const raw = await storageGet(getHistoryKey(currentUser));
      return raw ? JSON.parse(raw) : [];
    } catch (error) {
      console.error('Erro ao carregar histórico:', error);
      return [];
    }
  }

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
