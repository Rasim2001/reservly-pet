import { check } from 'k6';
import { Counter } from 'k6/metrics';
import { login } from './utils.js';

const ok = new Counter('login_200');
const limited = new Counter('login_429');

export const options = {
    scenarios: {
        burst: { executor: 'shared-iterations', vus: 30, iterations: 30, maxDuration: '30s' },
    },
    thresholds: {
        login_429: ['count>0'],  // лимитер обязан отбить всплеск
        login_200: ['count>0'],  // но часть должна пройти
        checks: ['rate==1.0'],   // ни одного 5xx
    },
};

export default function () {
    const res = login();
    if (res.status === 200) ok.add(1);
    if (res.status === 429) limited.add(1);
    check(res, { 'no server error (5xx)': (r) => r.status < 500 });
}