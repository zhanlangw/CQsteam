import { banner_list, banner_upd, banner_add, banner_del, banner_item } from '@/services/banner';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'banner',

  state: {
    banner_list:{
      totalCount:0,
      value:[]
    },
    banner_params:{
      start:0,
      count:count
    },
    banner_item:{},
  },

  effects: {
    *fetch_banner_upd({payload}, { call, put }) {
      const response = yield call(banner_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/banner/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_banner_add({payload}, { call, put }) {
      const response = yield call(banner_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/banner/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_banner_del({payload}, { call, put, select }) {
      const response = yield call(banner_del,payload);
      const banner = yield select(state=>state.banner);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_banner_list',
            payload: banner.banner_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_banner_list({payload}, { call, put }) {
      const response = yield call(banner_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_banner_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'banner_list',
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
    *fetch_banner_item({payload,callback}, { call, put }) {
        const response = yield call(banner_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'banner_item',
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
    banner_item(state, {payload}) {
        let banner_item = state.banner_item;
        banner_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    banner_list(state, {payload}) {
        return {
          ...state,
          banner_params:payload.params,
          banner_list:payload.response
        };
      },
  },
};
