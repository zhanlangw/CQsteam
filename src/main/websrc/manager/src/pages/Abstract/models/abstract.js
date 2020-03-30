import { introduction_item, introduction_upd } from '@/services/abstract';
import { message } from 'antd';
import router from 'umi/router';

export default {
  namespace: 'abstract',

  state: {
    introduction_item:{},
  },

  effects: {
    *fetch_introduction_upd({payload}, { call, put }) {
      const response = yield call(introduction_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/abstract');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_introduction_item({payload,callback}, { call, put }) {
        const response = yield call(introduction_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'introduction_item',
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
  },

  reducers: {
    introduction_item(state, {payload}) {
        return {
          ...state,
          introduction_item:payload
        };
      },
  },
};
