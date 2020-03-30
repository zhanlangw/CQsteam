import request from '@/utils/request';
import { stringify } from 'qs';

export async function users_list(params) {
    return request(`/api/user/list?${stringify(params)}`);
}

export async function password_reset(params) {
    return request('/api/user/password/reset', {method:'POST', body: params });
}

export async function users_disable(params) {
    return request(`/api/user/disable?${stringify(params)}`);
}

