import request from '@/utils/request';
import { stringify } from 'qs';

export async function result_list(params) {
    return request(`/api/result/list?${stringify(params)}`);
}

export async function result_item(params) {
    return request(`/api/result/item?${stringify(params)}`);
}

export async function result_upd(params) {
    return request('/api/result/upd', {method:'POST', body: params });
}

export async function result_add(params) {
    return request('/api/result/add', {method:'POST', body: params });
}

export async function result_del(params) {
    return request(`/api/result/del?${stringify(params)}`);
}