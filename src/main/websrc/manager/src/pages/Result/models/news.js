import { result_list, result_upd, result_add, result_del, result_item } from '@/services/result';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'result',

  state: {
    result_list:{
      totalCount:0,
      value:[]
    },
    result_params:{
      start:0,
      count:count
    },
    result_item:{},
  },

  effects: {
    *fetch_result_upd({payload}, { call, put }) {
      const response = yield call(result_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/result/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_result_add({payload}, { call, put }) {
      const response = yield call(result_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/result/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_result_del({payload}, { call, put, select }) {
      const response = yield call(result_del,payload);
      const result = yield select(state=>state.result);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_result_list',
            payload: result.result_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_result_list({payload}, { call, put }) {
      const response = yield call(result_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_result_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'result_list',
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
    *fetch_result_item({payload}, { call, put }) {
        const response = yield call(result_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'result_item',
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
  },

  reducers: {
    result_item(state, {payload}) {
        let result_item = state.result_item;
        result_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    result_list(state, {payload}) {
        return {
          ...state,
          result_params:payload.params,
          result_list:payload.response
        };
      },
  },
};
