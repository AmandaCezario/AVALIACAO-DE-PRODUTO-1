import { describe, expect, it } from 'vitest';
import { buildBasicAuthHeader, calculateFinalPrice, normalizeUsername } from './script.js';

describe('auth helpers', () => {
  it('normalizes the username for login', () => {
    expect(normalizeUsername(' Admin ')).toBe('admin');
  });

  it('builds a basic auth header', () => {
    expect(buildBasicAuthHeader('admin', 'kaori123')).toBe('Basic YWRtaW46a2FvcmkxMjM=');
  });
});

describe('pricing formula', () => {
  it('includes the platform fee in the final price', () => {
    const result = calculateFinalPrice({
      custo: 100,
      frete: 10,
      embalagem: 5,
      taxaCartao: 5,
      taxaPlataforma: 7,
      imposto: 10,
      margem: 50
    });

    expect(result.precoFinal).toBeCloseTo(410.71, 2);
    expect(result.custoTotal).toBeCloseTo(205.36, 2);
  });
});
