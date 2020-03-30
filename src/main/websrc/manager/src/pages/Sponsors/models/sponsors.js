import { sponsors_list, sponsors_upd, sponsors_add, sponsors_del, sponsors_item } from '@/services/sponsors';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'sponsors',

  state: {
    sponsors_list:{
      totalCount:0,
      value:[]
    },
    sponsors_params:{
      start:0,
      count:count
    },
    sponsors_item:{},
  },

  effects: {
    *fetch_sponsors_upd({payload}, { call, put }) {
      const response = yield call(sponsors_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/sponsors/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_sponsors_add({payload}, { call, put }) {
      const response = yield call(sponsors_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/sponsors/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_sponsors_del({payload}, { call, put, select }) {
      const response = yield call(sponsors_del,payload);
      const sponsors = yield select(state=>state.sponsors);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_sponsors_list',
            payload: sponsors.sponsors_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_sponsors_list({payload}, { call, put }) {
      const response = yield call(sponsors_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_sponsors_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'sponsors_list',
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
    *fetch_sponsors_item({payload,callback}, { call, put }) {
        const response = yield call(sponsors_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'sponsors_item',
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
    sponsors_item(state, {payload}) {
        let sponsors_item = state.sponsors_item;
        sponsors_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    sponsors_list(state, {payload}) {
        return {
          ...state,
          sponsors_params:payload.params,
          sponsors_list:payload.response
        };
      },
  },
};
