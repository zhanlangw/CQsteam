import request from '@/utils/request';
import { stringify } from 'qs';

export async function activity_list(params) {
    return request(`/api/event/list?${stringify(params)}`);
}

export async function activity_item(params) {
    return request(`/api/event/item?${stringify(params)}`);
}

export async function activity_upd(params) {
    return request('/api/event/upd', {method:'POST', body: params });
}

export async function activity_add(params) {
    return request('/api/event/add', {method:'POST', body: params });
}

export async function activity_del(params) {
    return request(`/api/event/del?${stringify(params)}`);
}