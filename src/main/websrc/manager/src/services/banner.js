import request from '@/utils/request';
import { stringify } from 'qs';

export async function banner_list(params) {
    return request(`/api/banner/list?${stringify(params)}`);
}

export async function banner_item(params) {
    return request(`/api/banner/item?${stringify(params)}`);
}

export async function banner_upd(params) {
    return request('/api/banner/upd', {method:'POST', body: params });
}

export async function banner_add(params) {
    return request('/api/banner/add', {method:'POST', body: params });
}

export async function banner_del(params) {
    return request(`/api/banner/del?${stringify(params)}`);
}