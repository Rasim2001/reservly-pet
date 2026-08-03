import http from 'k6/http';
import { BASE_URL, USER, JSON_HEADERS } from './config.js';

export function login(user = USER) {
    return http.post(`${BASE_URL}/api/auth/login`, JSON.stringify(user), JSON_HEADERS);
}

export function authHeaders(token) {
    return { headers: { Authorization: `Bearer ${token}` } };
}