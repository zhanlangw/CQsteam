import { advert_list, advert_upd, advert_add, advert_del, advert_item } from '@/services/advert';
import { message } from 'antd';
import router from 'umi/router';

export default {
  namespace: 'advert',

  state: {
    advert_list:[],
    advert_item:{},
  },

  effects: {
    *fetch_advert_upd({payload,callback}, { call, put }) {
      const response = yield call(advert_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          yield put({
            type:'fetch_advert_list'
          })
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
    *fetch_advert_add({payload,callback}, { call, put }) {
      const response = yield call(advert_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          yield put({
            type:'fetch_advert_list'
          })
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
    *fetch_advert_del({payload,callback}, { call, put, select }) {
      const response = yield call(advert_del,payload);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type:'fetch_advert_list'
          })
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
    *fetch_advert_list({payload}, { call, put }) {
      const response = yield call(advert_list,payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'advert_list',
            payload: response.value,
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_advert_item({payload}, { call, put }) {
        const response = yield call(advert_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'advert_item',
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
    advert_item(state, {payload}) {
        let advert_item = state.advert_item;
        advert_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    advert_list(state, {payload}) {
        return {
          ...state,
          advert_list:payload
        };
      },
  },
};
