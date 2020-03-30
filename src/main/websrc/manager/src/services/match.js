import request from '@/utils/request';
import { stringify } from 'qs';

export async function match_list(params) {
    return request(`/api/category/list?${stringify(params)}`);
}

export async function match_item(params) {
    return request(`/api/category/item?${stringify(params)}`);
}

export async function match_upd(params) {
    return request('/api/category/upd', {method:'POST', body: params });
}

export async function match_add(params) {
    return request('/api/category/add', {method:'POST', body: params });
}

export async function match_del(params) {
    return request(`/api/category/del?${stringify(params)}`);
}