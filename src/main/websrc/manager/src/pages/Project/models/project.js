import { 
  project_list,
  send_msg,
  category_list,
  category_item,
  basis_add,
  basis_upd,
  basis_item,
  project_add,
  project_item,
  teacher_add,
  teacher_item,
  material_add,
  material_item,
  submit,
  status,
  area_tree,
  school_list,
  school_add,
  import_result,
  category_stage,
  project_del,
 } from '@/services/project';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'project',

  state: {
    category_list:[],
    category_type:null,
    category_stage:{},
    project_info:{},
    project_list:{
      totalCount:0,
      value:[]
    },
    project_params:{
      start:0,
      count:count
    },
    area_tree:[]
  },

  effects: {
    *fetch_import_result({payload,params,callback}, { call, put }) {
      const response = yield call(import_result,payload,params);
      try {
        if (response.status === 0) {
          message.success('导入成功！');
          if(callback){
            yield call(callback)
          }
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_send_msg({payload}, { call, put }) {
      const response = yield call(send_msg,payload);
      try {
        if (response.status === 0) {
          message.success('发送成功！');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_category_stage({payload}, { call, put }) {
      const response = yield call(category_stage,payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'category_stage',
            payload: {
              response:response.value,
              id:payload.id
            }
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_project_del({payload}, { call, put, select }) {
      const response = yield call(project_del,payload);
      const params = yield select(state=>state.project.project_params);
      try {
        if (response.status === 0) {
          yield put({
            type: 'fetch_project_list',
            payload:{
              ...params
            }
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_project_list({payload}, { call, put }) {
      let response;
      if(payload.district){
        response = yield call(project_list,{
          ...payload,
          district:payload.district[payload.district.length-1]
        });
      }else{
        response = yield call(project_list,payload);
      }
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_project_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'project_list',
              payload: {
                response:response.value,
                params:payload
              }
            });
          }
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_school_add({payload,callback}, { call, put }) {
      const response = yield call(school_add,payload);
      try {
        if (response.status === 0) {
          message.success('添加成功');
          if(callback){
              yield call(callback);
            }
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_teacher_item({payload,callback}, { call, put }) {
        const response = yield call(teacher_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'project_info',
              payload: {
                response:response.value,
                id:payload.id,
                key:'teacher'
              }
            });
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_basis_item({payload,callback}, { call, put }) {
        const response = yield call(basis_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'project_info',
              payload: {
                response:response.value,
                id:payload.id,
                key:'basis'
              }
            });
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_advert({payload}, { call, put }) {
        const response = yield call(advert,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'advert',
              payload: {
                response:response.value,
                type:payload.type
              }
            });
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_submit({payload,callback}, { call, put }) {
        const response = yield call(submit,payload);
        try {
          if (response.status === 0) {
            message.success('提交成功！');
            if(callback){
                yield call(callback);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_material_add({payload,callback}, { call, put }) {
        const response = yield call(material_add,payload);
        try {
          if (response.status === 0) {
            message.success('保存成功！');
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_material_item({payload,params,callback}, { call, put }) {
        const response = yield call(material_item,payload);
        try {
          if (response.status === 0) {
            if(params.save){
                let key;
                if(payload.stageType === 1){
                    key = 'material1';
                }else if(payload.stageType === 2){
                    key = 'material2';
                }else if(payload.stageType === 3){
                    key = 'material3';
                }
                yield put({
                    type: 'project_info',
                    payload: {
                      response:response.value,
                      id:payload.id,
                      key:key
                    }
                  });
            }
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_teacher_add({payload,callback}, { call, put }) {
        const response = yield call(teacher_add,payload);
        try {
          if (response.status === 0) {
            message.success('保存成功！');
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_project_add({payload,callback}, { call, put }) {
        const response = yield call(project_add,payload);
        try {
          if (response.status === 0) {
            message.success('保存成功！');
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_project_item({payload,callback}, { call, put }) {
        const response = yield call(project_item,payload);
        try {
          if (response.status === 0) {
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_status({payload,callback}, { call, put }) {
        const response = yield call(status,payload);
        try {
          if (response.status === 0) {
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_school_list({payload,callback}, { call, put }) {
        const response = yield call(school_list,payload);
        try {
          if (response.status === 0) {
            if(callback){
                yield call(callback,response.value.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_area_tree({payload,params,callback}, { call, put }) {
        const response = yield call(area_tree,payload);
        const { notSetData } = params;
        try {
          if (response.status === 0) {
            if(!notSetData){
              yield put({
                type: 'area_tree',
                payload: response.value.map(e=>{
                  return {
                    label: e.title,
                    value: e.key,
                    isLeaf:false
                  }
                })
              });
            }
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_category_list({payload,callback}, { call, put }) {
        const response = yield call(category_list,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'category_list',
              payload: response.value.value
            });
            if(callback){
              yield call(callback,response.value.value);
            }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
      *fetch_category_item({payload,callback}, { call, put }) {
        const response = yield call(category_item,payload);
        try {
          if (response.status === 0) {
            if(callback){
                yield call(callback,response.value);
              }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
    *fetch_basis_add({payload,callback}, { call, put }) {
      const response = yield call(basis_add,payload);
      try {
        if (response.status === 0) {
          message.success('保存成功！');
          if(callback){
            yield call(callback,response.value.id);
          }
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_basis_upd({payload,callback}, { call, put }) {
        const response = yield call(basis_upd,payload);
        try {
          if (response.status === 0) {
            message.success('保存成功！');
            if(callback){
              yield call(callback,response.value.id);
            }
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
  },

  reducers: {
    project_list(state, {payload}) {
        return {
          ...state,
          project_params:payload.params,
          project_list:payload.response
        };
      },
    project_info(state, {payload}) {
        let project_info = state.project_info;
        project_info[payload.id]= project_info[payload.id] || {};
        project_info[payload.id][payload.key] = payload.response;
        return {
          ...state,
        };
      },
    category_stage(state, {payload}) {
      let category_stage = state.category_stage;
      category_stage[payload.id] = payload.response;
      return {
        ...state,
        category_type:payload.id
      };
    },
    category_list(state, {payload}) {
        return {
          ...state,
          category_list: payload,
        };
      },
    area_tree(state, {payload}) {
      return {
        ...state,
        area_tree: payload,
      };
    },
  },
};
