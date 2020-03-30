import { match_list, match_upd, match_add, match_del, match_item } from '@/services/match';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'match',

  state: {
    match_list:{
      totalCount:0,
      value:[]
    },
    match_params:{
      start:0,
      count:count
    },
    match_item:{},
  },

  effects: {
    *fetch_match_upd({payload}, { call, put }) {
      const response = yield call(match_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/match/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_match_add({payload}, { call, put }) {
      const response = yield call(match_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/match/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_match_del({payload}, { call, put, select }) {
      const response = yield call(match_del,payload);
      const match = yield select(state=>state.match);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_match_list',
            payload: match.match_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_match_list({payload}, { call, put }) {
      const response = yield call(match_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_match_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'match_list',
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
    *fetch_match_item({payload,callback}, { call, put }) {
        const response = yield call(match_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'match_item',
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
    match_item(state, {payload}) {
        let match_item = state.match_item;
        match_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    match_list(state, {payload}) {
        return {
          ...state,
          match_params:payload.params,
          match_list:payload.response
        };
      },
  },
};
