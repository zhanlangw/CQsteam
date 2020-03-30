import request from '@/utils/request';
import { stringify } from 'qs';

export async function project_del(params) {
    return request(`/api/project/admin/del?${stringify(params)}`);
}

export async function project_list(params) {
    return request(`/api/project/list?${stringify(params)}`);
}

export async function send_msg(params) {
    return request(`/api/project/send_msg?${stringify(params)}`);
}

export async function import_result(payload,params) {
    return request(`/api/project/import_result?${stringify(params)}`, { method:'POST', body: payload });
  }

export async function category_stage(params) {
    return request(`/api/category/stage?${stringify(params)}`);
  }
  //类别列表
export async function category_list(params) {
    return request(`/api/category/list?${stringify(params)}`);
  }
//类别详情
export async function category_item(params) {
    return request(`/api/home/category/item?${stringify(params)}`);
}
//保存基础信息
export async function basis_add(params) {
  return request('/api/project/sign_up/basis/add', { method:'POST', body: params });
}
//修改基础信息
export async function basis_upd(params) {
    return request('/api/project/sign_up/basis/upd', { method:'POST', body: params });
  }
//基本信息详情
export async function basis_item(params) {
  return request(`/api/project/sign_up/basis/item?${stringify(params)}`);
}
//保存修改项目基础信息
export async function project_add(params) {
    return request('/api/project/sign_up/basis/project/add', { method:'POST', body: params });
}
//项目基础信息详情
export async function project_item(params) {
    return request(`/api/project/sign_up/basis/project/item?${stringify(params)}`);
}
//保存修改指导老师信息
export async function teacher_add(params) {
    return request('/api/project/sign_up/teacher/add', { method:'POST', body: params });
}
//指导老师详情
export async function teacher_item(params) {
    return request(`/api/project/sign_up/teacher/item?${stringify(params)}`);
}
//保存修改比赛材料
export async function material_add(params) {
    return request('/api/project/sign_up/material/add', { method:'POST', body: params });
}
//比赛材料详情
export async function material_item(params) {
    return request(`/api/project/sign_up/material/item?${stringify(params)}`);
}
//项目资料完成状态
export async function status(params) {
    return request(`/api/project/sign_up/status?${stringify(params)}`);
}
//提交项目信息
export async function submit(params) {
    return request(`/api/project/sign_up/submit?${stringify(params)}`);
}
//区域
export async function area_tree(params) {
    return request(`/api/area/tree?${stringify(params)}`);
}
//学校列表
export async function school_list(params) {
    return request(`/api/school/list?${stringify(params)}`);
}
//添加学校
export async function school_add(params) {
    return request('/api/school/add', { method:'POST', body: params });
}