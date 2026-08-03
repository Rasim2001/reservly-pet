import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';
import { AUTH_URL, JSON_HEADERS } from './config.js';

const unauthorized = new Counter('login_401');
const blocked = new Counter('login_429');

export const options = {
    scenarios: {
        brute: { executor: 'shared-iterations', vus: 1, iterations: 10, maxDuration: '60s' },
    },
    thresholds: {
        login_401: ['count>0'],
        login_429: ['count>0'],
        checks: ['rate==1.0'],
    },
};

const attempt = JSON.stringify({
    email: 'brute-test@example.com',
    password: 'wrongPassword123',
});

export default function () {
    const res = http.post(`${AUTH_URL}/api/auth/login`, attempt, JSON_HEADERS);

    if (res.status === 401) unauthorized.add(1);
    if (res.status === 429) blocked.add(1);

    check(res, { 'no server error (5xx)': (r) => r.status < 500 });
}