import { exports_list, exports_del, exports_add } from '@/services/export';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'exports',

  state: {
    exports_list:{
      totalCount:0,
      value:[]
    },
    exports_params:{
      start:0,
      count:count
    },
  },

  effects: {
    *fetch_exports_add({payload,callback}, { call, put, select }) {
      const response = yield call(exports_add,payload);
      const exports = yield select(state=>state.exports);
      try {
        if (response.status === 0) {
          message.success('操作成功!');
          if(callback){
            yield call(callback)
          }
          yield put({
            type: 'fetch_exports_list',
            payload: exports.exports_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_exports_del({payload}, { call, put, select }) {
      const response = yield call(exports_del,payload);
      const exports = yield select(state=>state.exports);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_exports_list',
            payload: exports.exports_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_exports_list({payload}, { call, put }) {
      const response = yield call(exports_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_exports_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'exports_list',
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
    exports_list(state, {payload}) {
        return {
          ...state,
          exports_params:payload.params,
          exports_list:payload.response
        };
      },
  },
};
