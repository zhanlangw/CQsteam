import { activity_list, activity_upd, activity_add, activity_del, activity_item } from '@/services/activity';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'activity',

  state: {
    activity_list:{
      totalCount:0,
      value:[]
    },
    activity_params:{
      start:0,
      count:count
    },
    activity_item:{},
  },

  effects: {
    *fetch_activity_upd({payload}, { call, put }) {
      const response = yield call(activity_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/activity/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_activity_add({payload}, { call, put }) {
      const response = yield call(activity_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/activity/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_activity_del({payload}, { call, put, select }) {
      const response = yield call(activity_del,payload);
      const activity = yield select(state=>state.activity);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_activity_list',
            payload: activity.activity_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_activity_list({payload}, { call, put }) {
      const response = yield call(activity_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_activity_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'activity_list',
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
    *fetch_activity_item({payload}, { call, put }) {
        const response = yield call(activity_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'activity_item',
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
    activity_item(state, {payload}) {
        let activity_item = state.activity_item;
        activity_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    activity_list(state, {payload}) {
        return {
          ...state,
          activity_params:payload.params,
          activity_list:payload.response
        };
      },
  },
};
