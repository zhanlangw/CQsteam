import { role_list, role_upd, role_add, role_del, role_item, role_tree } from '@/services/roles';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'role',

  state: {
    role_list:{
      totalCount:0,
      value:[]
    },
    role_tree:[],
    role_params:{
      start:0,
      count:count
    },
    role_item:{},
  },

  effects: {
    *fetch_role_upd({payload}, { call, put }) {
      const response = yield call(role_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/roles/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_role_add({payload}, { call, put }) {
      const response = yield call(role_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/roles/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_role_del({payload}, { call, put, select }) {
      const response = yield call(role_del,payload);
      const role = yield select(state=>state.role);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_role_list',
            payload: role.role_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_role_tree({payload}, { call, put, select }) {
      const response = yield call(role_tree,payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'role_tree',
            payload: response.value
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_role_list({payload}, { call, put }) {
      const response = yield call(role_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_role_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'role_list',
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
    *fetch_role_item({payload,callback}, { call, put }) {
        const response = yield call(role_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'role_item',
              payload: {
                response:response.value,
                id:payload.id
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
  },

  reducers: {
    role_item(state, {payload}) {
        let role_item = state.role_item;
        role_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
      role_tree(state, {payload}) {
        return {
          ...state,
          role_tree:payload
        };
      },
    role_list(state, {payload}) {
        return {
          ...state,
          role_params:payload.params,
          role_list:payload.response
        };
      },
  },
};
