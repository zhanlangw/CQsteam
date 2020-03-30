import request from '@/utils/request';
import { stringify } from 'qs';

export async function exports_list(params) {
    return request(`/api/export/list?${stringify(params)}`);
}

export async function exports_del(params) {
    return request(`/api/export/del?${stringify(params)}`);
}

export async function exports_add(params) {
    return request('/api/export/add', {method:'POST', body: params });
}