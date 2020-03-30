import request from '@/utils/request';

export async function user_login(params) {
    return request('/api/admin/login', {method:'POST', body: params });
}