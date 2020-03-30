import { enter_data, register_data, award_data, visitor_data, document_data, word_data, category_list, category_item, school_list } from '@/services/statistics';
import { message } from 'antd';
import router from 'umi/router';
import moment from 'moment';

const defaultData = {
  line1:{
    data:[],
    totalCount:0
  },
  line2:{
    data:[],
    totalCount:0
  },
};

const defaultParams = {
  line1:{
    startTime:moment().subtract(7,'days').format('YYYY/MM/DD'),
    endTime:moment().format('YYYY/MM/DD'),
  },
  line2:{
    startTime:moment().subtract(7,'days').format('YYYY/MM/DD'),
    endTime:moment().format('YYYY/MM/DD'),
  },
};

export default {
  namespace: 'statistics',

  state: {
    category_list:[],
    enter_data:{
      ...defaultData
    }, 
    enter_areaOption:{
      line1:[],
      line2:[]
    },
    enter_groupOption:{
      line1:[],
      line2:[]
    },
    enter_schoolOption:{
      line1:[],
      line2:[]
    },
    enter_category_item:{
      line1:{
        area:[],
        group:[],
      },
      line2:{
        area:[],
        group:[],
      },
    },
    register_data:{
      ...defaultData
    }, 
    register_areaOption:{
      line1:[],
      line2:[]
    },
    register_groupOption:{
      line1:[],
      line2:[]
    },
    register_schoolOption:{
      line1:[],
      line2:[]
    },
    register_category_item:{
      line1:{
        area:[],
        group:[],
      },
      line2:{
        area:[],
        group:[],
      },
    },
    award_data:{
      line1:[],
      line2:[],
    },  
    award_areaOption:{
      line1:[],
      line2:[]
    },
    award_groupOption:{
      line1:[],
      line2:[]
    },
    award_schoolOption:{
      line1:[],
      line2:[]
    },
    award_category_item:{
      line1:{
        area:[],
        group:[],
      },
      line2:{
        area:[],
        group:[],
      },
    },
    visitor_data:{
      ...defaultData
    }, 
    visitor_areaOption:{
      line1:[],
      line2:[]
    },
    visitor_groupOption:{
      line1:[],
      line2:[]
    },
    visitor_schoolOption:{
      line1:[],
      line2:[]
    },
    visitor_category_item:{
      line1:{
        area:[],
        group:[],
      },
      line2:{
        area:[],
        group:[],
      },
    },
    document_data:{
      ...defaultData
    },  
    document_areaOption:{
      line1:[],
      line2:[]
    },
    document_groupOption:{
      line1:[],
      line2:[]
    },
    document_schoolOption:{
      line1:[],
      line2:[]
    },
    document_category_item:{
      line1:{
        area:[],
        group:[],
      },
      line2:{
        area:[],
        group:[],
      },
    },
    word_data:{
      ...defaultData
    }, 
    word_areaOption:{
      line1:[],
      line2:[]
    },
    word_groupOption:{
      line1:[],
      line2:[]
    },
    word_schoolOption:{
      line1:[],
      line2:[]
    },
    word_category_item:{
      line1:{
        area:[],
        group:[],
      },
      line2:{
        area:[],
        group:[],
      },
    },
    enter_data_params:{
      ...defaultParams
    }, 
    register_data_params:{
      ...defaultParams
    }, 
    award_data_params:{
      line1:{
      },
      line2:{
      },
    }, 
    visitor_data_params:{
      ...defaultParams
    }, 
    document_data_params:{
      ...defaultParams
    }, 
    word_data_params:{
      ...defaultParams
    }, 
  },

  effects: {
    *fetch_school_list({payload,callback}, { call, put }) {
      const response = yield call(school_list,payload);
      try {
        if (response.status === 0) {
          if(callback){
              yield call(callback,response.value.value);
            }
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_category_item({payload,params,callback}, { call, put }) {
      const type = params.type;
      const stage = params.stage;
      const response = yield call(category_item,payload);
      try {
        if (response.status === 0) {
            yield put({
                type: 'category_item',
                payload: {
                  response:{
                    [`line${type}`]:response.value,
                  },
                  stage:stage
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
    *fetch_category_list({payload}, { call, put }) {
      const response = yield call(category_list,payload);
      try {
        if (response.status === 0) {
            yield put({
                type: 'category_list',
                payload: response.value.value
              });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_enter_data({payload,params}, { call, put }) {
      const type = params.type;
      let response;
      if(payload.areaId){
        response = yield call(enter_data,{
          ...payload,
          areaId:payload.areaId[payload.areaId.length-1]
        });
      }else{
        response = yield call(enter_data,payload);
      }
      try {
        if (response.status === 0) {
            yield put({
                type: 'enter_data',
                payload: {
                  response:{
                    [`line${type}`]:response.value
                  },
                  params:{
                    [`line${type}`]:payload
                  }
                }
              });
        }else{
          message.error(response.message);
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_register_data({payload,params}, { call, put }) {
      const type = params.type;  
      let response;
      if(payload.areaId){
        response = yield call(register_data,{
          ...payload,
          areaId:payload.areaId[payload.areaId.length-1]
        });
      }else{
        response = yield call(register_data,payload);
      }
        try {
          if (response.status === 0) {
              yield put({
                  type: 'register_data',
                  payload: {
                    response:{
                      [`line${type}`]:response.value
                    },
                    params:{
                      [`line${type}`]:payload
                    }
                  }
                });
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
      *fetch_award_data({payload,params}, { call, put }) {
        const type = params.type;
        let response;
        if(payload.areaId){
          response = yield call(award_data,{
            ...payload,
            areaId:payload.areaId[payload.areaId.length-1]
          });
        }else{
          response = yield call(award_data,payload);
        }
        try {
          if (response.status === 0) {
              yield put({
                  type: 'award_data',
                  payload: {
                    response:{
                      [`line${type}`]:response.value
                    },
                    params:{
                      [`line${type}`]:payload
                    }
                  }
                });
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
      *fetch_visitor_data({payload,params}, { call, put }) {
        const type = params.type;
        let response;
        if(payload.areaId){
          response = yield call(visitor_data,{
            ...payload,
            areaId:payload.areaId[payload.areaId.length-1]
          });
        }else{
          response = yield call(visitor_data,payload);
        }
        try {
          if (response.status === 0) {
              yield put({
                  type: 'visitor_data',
                  payload: {
                    response:{
                      [`line${type}`]:response.value
                    },
                    params:{
                      [`line${type}`]:payload
                    }
                  }
                });
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
      *fetch_document_data({payload,params}, { call, put }) {
        const type = params.type;
        let response;
        if(payload.areaId){
          response = yield call(document_data,{
            ...payload,
            areaId:payload.areaId[payload.areaId.length-1]
          });
        }else{
          response = yield call(document_data,payload);
        }
        try {
          if (response.status === 0) {
              yield put({
                  type: 'document_data',
                  payload: {
                    response:{
                      [`line${type}`]:response.value
                    },
                    params:{
                      [`line${type}`]:payload
                    }
                  }
                });
          }else{
            message.error(response.message);
          }
        } catch (error) {
          message.error('请求失败，请稍后再试！');
        }
      },
      *fetch_word_data({payload,params}, { call, put }) {
        const type = params.type;
        let response;
        if(payload.areaId){
          response = yield call(word_data,{
            ...payload,
            areaId:payload.areaId[payload.areaId.length-1]
          });
        }else{
          response = yield call(word_data,payload);
        }
        try {
          if (response.status === 0) {
              yield put({
                  type: 'word_data',
                  payload: {
                    response:{
                      [`line${type}`]:response.value
                    },
                    params:{
                      [`line${type}`]:payload
                    }
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
    changeDataParams(state, {payload,params}) {
      const type = params.type;
      if(type === 1){
        return {
          ...state,
          enter_data_params:payload
        };
      }else if(type === 2){
        return {
          ...state,
          register_data_params:payload
        };
      }else if(type === 3){
        return {
          ...state,
          award_data_params:payload
        };
      }else if(type === 4){
        return {
          ...state,
          visitor_data_params:payload
        };
      }else if(type === 5){
        return {
          ...state,
          document_data_params:payload
        };
      }else if(type === 6){
        return {
          ...state,
          word_data_params:payload
        };
      }
    },
    changeAreaOption(state, {payload,params}) {
      const type = params.type;
      if(type === 1){
        return {
          ...state,
          enter_areaOption:payload
        };
      }else if(type === 2){
        return {
          ...state,
          register_areaOption:payload
        };
      }else if(type === 3){
        return {
          ...state,
          award_areaOption:payload
        };
      }else if(type === 4){
        return {
          ...state,
          visitor_areaOption:payload
        };
      }else if(type === 5){
        return {
          ...state,
          document_areaOption:payload
        };
      }else if(type === 6){
        return {
          ...state,
          word_areaOption:payload
        };
      }
    },
    changeGroupOption(state, {payload,params}) {
      const type = params.type;
      if(type === 1){
        return {
          ...state,
          enter_groupOption:payload
        };
      }else if(type === 2){
        return {
          ...state,
          register_groupOption:payload
        };
      }else if(type === 3){
        return {
          ...state,
          award_groupOption:payload
        };
      }else if(type === 4){
        return {
          ...state,
          visitor_groupOption:payload
        };
      }else if(type === 5){
        return {
          ...state,
          document_groupOption:payload
        };
      }else if(type === 6){
        return {
          ...state,
          word_groupOption:payload
        };
      }
    },
    changeSchoolOption(state, {payload,params}) {
      const type = params.type;
      if(type === 1){
        return {
          ...state,
          enter_schoolOption:payload
        };
      }else if(type === 2){
        return {
          ...state,
          register_schoolOption:payload
        };
      }else if(type === 3){
        return {
          ...state,
          award_schoolOption:payload
        };
      }else if(type === 4){
        return {
          ...state,
          visitor_schoolOption:payload
        };
      }else if(type === 5){
        return {
          ...state,
          document_schoolOption:payload
        };
      }else if(type === 6){
        return {
          ...state,
          word_schoolOption:payload
        };
      }
    },
    category_list(state, {payload}) {
      return {
        ...state,
        category_list:payload
      };
    },
    category_item(state, {payload}) {
      const stage=payload.stage;
      if(stage === 1){
        return {
          ...state,
          enter_category_item:{
            ...state.category_item,
            ...payload.response
          }
        };
      }else if(stage === 2){
        return {
          ...state,
          register_category_item:{
            ...state.category_item,
            ...payload.response
          }
        };
      }else if(stage === 3){
        return {
          ...state,
          award_category_item:{
            ...state.category_item,
            ...payload.response
          }
        };
      }else if(stage === 4){
        return {
          ...state,
          visitor_category_item:{
            ...state.category_item,
            ...payload.response
          }
        };
      }else if(stage === 5){
        return {
          ...state,
          document_category_item:{
            ...state.category_item,
            ...payload.response
          }
        };
      }else if(stage === 6){
        return {
          ...state,
          word_category_item:{
            ...state.category_item,
            ...payload.response
          }
        };
      }
    },
    enter_data(state, {payload}) {
        return {
          ...state,
          enter_data_params:{
            ...state.enter_data_params,
            ...payload.params
          },
          enter_data:{
            ...state.enter_data,
            ...payload.response
          }
        };
      },
      register_data(state, {payload}) {
        return {
          ...state,
          register_data_params:{
            ...state.register_data_params,
            ...payload.params
          },
          register_data:{
            ...state.register_data,
            ...payload.response
          }
        };
      },
      award_data(state, {payload}) {
        return {
          ...state,
          award_data_params:{
            ...state.award_data_params,
            ...payload.params
          },
          award_data:{
            ...state.award_data,
            ...payload.response
          }
        };
      },
      visitor_data(state, {payload}) {
        return {
          ...state,
          visitor_data_params:{
            ...state.visitor_data_params,
            ...payload.params
          },
          visitor_data:{
            ...state.visitor_data,
            ...payload.response
          }
        };
      },
      document_data(state, {payload}) {
        return {
          ...state,
          document_data_params:{
            ...state.document_data_params,
            ...payload.params
          },
          document_data:{
            ...state.document_data,
            ...payload.response
          }
        };
      },
      word_data(state, {payload}) {
        return {
          ...state,
          word_data_params:{
            ...state.word_data_params,
            ...payload.params
          },
          word_data:{
            ...state.word_data,
            ...payload.response
          }
        };
      },
  },
};
