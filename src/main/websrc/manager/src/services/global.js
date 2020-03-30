import request from '@/utils/request';
import { stringify } from 'qs';

export async function file_del(params) {
    return request(`/api/project/sign_up/file/del?${stringify(params)}`);
}

export async function del_file(params) {
    return request(`/api/project/sign_up/del/file?${stringify(params)}`);
}

export async function file_upload(payload,params) {
    return request(`/api/file/upload?${stringify(params)}`, {method:'POST', body: payload });
}

export async function area(params) {
    return request(`/api/area/tree?${stringify(params)}`);
}
