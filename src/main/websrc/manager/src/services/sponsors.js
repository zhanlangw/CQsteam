import request from '@/utils/request';
import { stringify } from 'qs';

export async function sponsors_list(params) {
    return request(`/api/sponsor/list?${stringify(params)}`);
}

export async function sponsors_item(params) {
    return request(`/api/sponsor/item?${stringify(params)}`);
}

export async function sponsors_upd(params) {
    return request('/api/sponsor/upd', {method:'POST', body: params });
}

export async function sponsors_add(params) {
    return request('/api/sponsor/add', {method:'POST', body: params });
}

export async function sponsors_del(params) {
    return request(`/api/sponsor/del?${stringify(params)}`);
}