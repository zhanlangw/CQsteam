import { rule_list, rule_upd, rule_add, rule_del, rule_item } from '@/services/rules';
import { message } from 'antd';
import router from 'umi/router';
import { count } from '@/defaultSettings';

export default {
  namespace: 'rules',

  state: {
    rule_list:{
      totalCount:0,
      value:[]
    },
    rule_params:{
      start:0,
      count:count
    },
    rule_item:{},
  },

  effects: {
    *fetch_rule_upd({payload}, { call, put }) {
      const response = yield call(rule_upd,payload);
      try {
        if (response.status === 0) {
          message.success('修改成功!');
          router.push('/rules/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_rule_add({payload}, { call, put }) {
      const response = yield call(rule_add,payload);
      try {
        if (response.status === 0) {
          message.success('新增成功!');
          router.push('/rules/list');
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_rule_del({payload}, { call, put, select }) {
      const response = yield call(rule_del,payload);
      const rules = yield select(state=>state.rules);
      try {
        if (response.status === 0) {
          message.success('删除成功!');
          yield put({
            type: 'fetch_rule_list',
            payload: rules.rule_params
          });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_rule_list({payload}, { call, put }) {
      const response = yield call(rule_list,payload);
      try {
        if (response.status === 0) {
          if(response.value.value.length ===0 && payload.start !== 0){
            yield put({
              type: 'fetch_rule_list',
              payload: {
                ...payload,
                start:payload.start-count
              }
            });
          }else{
            yield put({
              type: 'rule_list',
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
    *fetch_rule_item({payload,callback}, { call, put }) {
        const response = yield call(rule_item,payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'rule_item',
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
    rule_item(state, {payload}) {
        let rule_item = state.rule_item;
        rule_item[payload.id] = payload.response;
        return {
          ...state,
        };
      },
    rule_list(state, {payload}) {
        return {
          ...state,
          rule_params:payload.params,
          rule_list:payload.response
        };
      },
  },
};
