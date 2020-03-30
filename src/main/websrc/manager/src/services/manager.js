import request from '@/utils/request';
import { stringify } from 'qs';

export async function manager_list(params) {
    return request(`/api/admin/list?${stringify(params)}`);
}

export async function manager_item(params) {
    return request(`/api/admin/item?${stringify(params)}`);
}

export async function manager_upd(params) {
    return request('/api/admin/upd', {method:'POST', body: params });
}

export async function manager_add(params) {
    return request('/api/admin/add', {method:'POST', body: params });
}

export async function manager_del(params) {
    return request(`/api/admin/del?${stringify(params)}`);
}

export async function password_reset(params) {
    return request('/api/admin/password/reset', {method:'POST', body: params });
}

export async function role_list(params) {
    return request(`/api/role/list?${stringify(params)}`);
}
