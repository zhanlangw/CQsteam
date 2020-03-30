import request from '@/utils/request';
import { stringify } from 'qs';

export async function role_list(params) {
    return request(`/api/role/list?${stringify(params)}`);
}

export async function role_item(params) {
    return request(`/api/role/item?${stringify(params)}`);
}

export async function role_upd(params) {
    return request('/api/role/upd', {method:'POST', body: params });
}

export async function role_add(params) {
    return request('/api/role/add', {method:'POST', body: params });
}

export async function role_del(params) {
    return request(`/api/role/del?${stringify(params)}`);
}

export async function role_tree(params) {
    return request(`/api/menu/list?${stringify(params)}`);
}
