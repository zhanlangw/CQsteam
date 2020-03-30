import request from '@/utils/request';
import { stringify } from 'qs';

export async function introduction_item(params) {
    return request(`/api/competition/introduction/item?${stringify(params)}`);
}
export async function introduction_upd(params) {
    return request('/api/competition/introduction/upd', {method:'POST', body: params });
}


