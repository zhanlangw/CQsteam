import { videos_list, videos_upd, videos_add, videos_del, videos_item } from '@/services/videos';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'videos',

  state: {
    videos_list:{
      totalCount:0,
      value:[]
    },
    videos_params:{
      start:0,
      count:count
    },
    videos_item:{},
  },

  effects: {
    *fetch_videos_upd({payload}, { call, put }) {
      const response = yield call(videos_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/videos/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_videos_add({payload}, { call, put }) {
      const response = yield call(videos_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/videos/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_videos_del({payload}, { call, put, select }) {
      const response = yield call(videos_del,payload);
      const videos = yield select(state=>state.videos);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_videos_list',
            payload: videos.videos_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_videos_list({payload}, { call, put }) {
      const response = yield call(videos_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_videos_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'videos_list',
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
    *fetch_videos_item({payload,callback}, { call, put }) {
        const response = yield call(videos_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'videos_item',
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
    videos_item(state, {payload}) {
        let videos_item = state.videos_item;
        videos_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    videos_list(state, {payload}) {
        return {
          ...state,
          videos_params:payload.params,
          videos_list:payload.response
        };
      },
  },
};
