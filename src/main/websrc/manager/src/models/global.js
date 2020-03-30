import { file_del, area, file_upload, del_file } from '@/services/global';
import { message } from 'antd';

export default {
  namespace: 'global',

  state: {
    collapsed: false,
    area:[]
  },

  effects: {
    *fetch_area({ payload, callback }, { call, put, select }) {
      const response = yield call(area, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'area',
            payload: response.value,
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
    *fetch_del_file({ payload }, { call, put, select }) {
      const response = yield call(del_file, payload);
      try {
        if (response.status === 0) {
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_file_del({ payload }, { call, put, select }) {
      const response = yield call(file_del, payload);
      try {
        if (response.status === 0) {
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_file_upload({ payload,params,callback }, { call, put, select }) {
      const response = yield call(file_upload, payload, params);
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
  },

  reducers: {
    changeLayoutCollapsed(state, {payload}) {
      return {
        ...state,
        collapsed:payload,
      };
    },
    area(state, {payload}) {
      return {
        ...state,
        area:payload,
      };
    },
  },
};
