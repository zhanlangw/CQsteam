import { users_list, password_reset, users_disable } from '@/services/users';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'users',

  state: {
    users_list:{
      totalCount:0,
      value:[]
    },
    users_params:{
      start:0,
      count:count
    },
  },

  effects: {
    *fetch_users_disable({payload}, { call, put }) {
      const response = yield call(users_disable,payload);
      try {
        if (response.status === 0) {
          message.success('操作成功!');
          router.push('/users/list');
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
          router.push('/users/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_users_list({payload}, { call, put }) {
      const response = yield call(users_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_users_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'users_list',
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
  },

  reducers: {
    users_list(state, {payload}) {
        return {
          ...state,
          users_params:payload.params,
          users_list:payload.response
        };
      },
  },
};
