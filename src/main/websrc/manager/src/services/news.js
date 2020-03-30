import request from '@/utils/request';
import { stringify } from 'qs';

export async function news_list(params) {
    return request(`/api/news/list?${stringify(params)}`);
}

export async function news_item(params) {
    return request(`/api/news/item?${stringify(params)}`);
}

export async function news_upd(params) {
    return request('/api/news/upd', {method:'POST', body: params });
}

export async function news_add(params) {
    return request('/api/news/add', {method:'POST', body: params });
}

export async function news_del(params) {
    return request(`/api/news/del?${stringify(params)}`);
}