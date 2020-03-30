import { attention_item, attention_upd } from '@/services/attention';
import { message } from 'antd';
import router from 'umi/router';

export default {
  namespace: 'attention',

  state: {
    attention_item:{},
  },

  effects: {
    *fetch_attention_upd({payload}, { call, put }) {
      const response = yield call(attention_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/attention');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_attention_item({payload,callback}, { call, put }) {
        const response = yield call(attention_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'attention_item',
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
    attention_item(state, {payload}) {
        return {
          ...state,
          attention_item:payload
        };
      },
  },
};
