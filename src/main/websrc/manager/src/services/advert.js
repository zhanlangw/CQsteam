import request from '@/utils/request';
import { stringify } from 'qs';

export async function advert_list(params) {
    return request(`/api/advert/list?${stringify(params)}`);
}

export async function advert_item(params) {
    return request(`/api/advert/item?${stringify(params)}`);
}

export async function advert_upd(params) {
    return request('/api/advert/upd', {method:'POST', body: params });
}

export async function advert_add(params) {
    return request('/api/advert/add', {method:'POST', body: params });
}

export async function advert_del(params) {
    return request(`/api/advert/del?${stringify(params)}`);
}