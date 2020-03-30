import request from '@/utils/request';
import { stringify } from 'qs';

export async function attention_item(params) {
    return request(`/api/competition/notice/item?${stringify(params)}`);
}
export async function attention_upd(params) {
    return request('/api/competition/notice/upd', {method:'POST', body: params });
}


