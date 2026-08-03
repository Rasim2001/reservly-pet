export const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const USER = {
    email: __ENV.EMAIL || 'rasim2@mail.ru',
    password: __ENV.PASSWORD || 'r123R45678',
};

export const JSON_HEADERS = { headers: { 'Content-Type': 'application/json' } };
export const AUTH_URL = __ENV.AUTH_URL || 'http://localhost:8081';