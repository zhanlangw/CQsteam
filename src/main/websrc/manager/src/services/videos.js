import request from '@/utils/request';
import { stringify } from 'qs';

export async function videos_list(params) {
    return request(`/api/video/list?${stringify(params)}`);
}

export async function videos_item(params) {
    return request(`/api/video/item?${stringify(params)}`);
}

export async function videos_upd(params) {
    return request('/api/video/upd', {method:'POST', body: params });
}

export async function videos_add(params) {
    return request('/api/video/add', {method:'POST', body: params });
}

export async function videos_del(params) {
    return request(`/api/video/del?${stringify(params)}`);
}