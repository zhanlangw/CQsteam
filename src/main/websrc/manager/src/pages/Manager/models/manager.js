import { manager_list, manager_upd, manager_add, manager_del, manager_item, password_reset, role_list } from '@/services/manager';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'manager',

  state: {
    role_list:[],
    manager_list:{
      totalCount:0,
      value:[]
    },
    manager_params:{
      start:0,
      count:count
    },
    manager_item:{},
  },

  effects: {
    *fetch_role_list({payload}, { call, put }) {
      const response = yield call(role_list,payload);
      try {
        if (response.status === 0) {
            yield put({
              type: 'role_list',
              payload: response.value.value
            });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_password_reset({payload}, { call, put }) {
      const response = yield call(password_reset,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/manager/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_manager_upd({payload}, { call, put }) {
      const response = yield call(manager_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/manager/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_manager_add({payload}, { call, put }) {
      const response = yield call(manager_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/manager/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_manager_del({payload}, { call, put, select }) {
      const response = yield call(manager_del,payload);
      const manager = yield select(state=>state.manager);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_manager_list',
            payload: manager.manager_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_manager_list({payload}, { call, put }) {
      const response = yield call(manager_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_manager_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'manager_list',
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
    *fetch_manager_item({payload,callback}, { call, put }) {
        const response = yield call(manager_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'manager_item',
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
    manager_item(state, {payload}) {
        let manager_item = state.manager_item;
        manager_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
      role_list(state, {payload}) {
        return {
          ...state,
          role_list:payload
        };
      },
    manager_list(state, {payload}) {
        return {
          ...state,
          manager_params:payload.params,
          manager_list:payload.response
        };
      },
  },
};
