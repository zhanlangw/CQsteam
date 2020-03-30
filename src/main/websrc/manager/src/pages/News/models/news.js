import { news_list, news_upd, news_add, news_del, news_item } from '@/services/news';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'news',

  state: {
    news_list:{
      totalCount:0,
      value:[]
    },
    news_params:{
      start:0,
      count:count
    },
    news_item:{},
  },

  effects: {
    *fetch_news_upd({payload}, { call, put }) {
      const response = yield call(news_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/news/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_news_add({payload}, { call, put }) {
      const response = yield call(news_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/news/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_news_del({payload}, { call, put, select }) {
      const response = yield call(news_del,payload);
      const news = yield select(state=>state.news);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_news_list',
            payload: news.news_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_news_list({payload}, { call, put }) {
      const response = yield call(news_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_news_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'news_list',
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
    *fetch_news_item({payload,callback}, { call, put }) {
        const response = yield call(news_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'news_item',
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
    news_item(state, {payload}) {
        let news_item = state.news_item;
        news_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    news_list(state, {payload}) {
        return {
          ...state,
          news_params:payload.params,
          news_list:payload.response
        };
      },
  },
};
