import request from '@/utils/request';
import { stringify } from 'qs';

export async function rule_list(params) {
    return request(`/api/rule/list?${stringify(params)}`);
}

export async function rule_item(params) {
    return request(`/api/rule/item?${stringify(params)}`);
}

export async function rule_upd(params) {
    return request('/api/rule/upd', {method:'POST', body: params });
}

export async function rule_add(params) {
    return request('/api/rule/add', {method:'POST', body: params });
}

export async function rule_del(params) {
    return request(`/api/rule/del?${stringify(params)}`);
}