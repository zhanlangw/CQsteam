import request from '@/utils/request';
import { stringify } from 'qs';

export async function enter_data(params) {
    return request(`/api/stats/sign_up?${stringify(params)}`);
}

export async function register_data(params) {
    return request(`/api/stats/register?${stringify(params)}`);
}

export async function award_data(params) {
    return request(`/api/stats/prize?${stringify(params)}`);
}

export async function visitor_data(params) {
    return request(`/api/stats/visitor?${stringify(params)}`);
}

export async function document_data(params) {
    return request(`/api/stats/submit?${stringify(params)}`);
}

export async function word_data(params) {
    return request(`/api/stats/word?${stringify(params)}`);
}

export async function category_list(params) {
    return request(`/api/category/list?${stringify(params)}`);
}

export async function category_item(params) {
    return request(`/api/category/item?${stringify(params)}`);
}

export async function school_list(params) {
    return request(`/api/school/list?${stringify(params)}`);
}
