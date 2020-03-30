import React, { PureComponent } from 'react';
import { Card, Spin, Form, Input, DatePicker, Select, Cascader, Row, Col, Button, Tooltip } from 'antd';
import styles from './style.less';
import Link from 'umi/link';
import { connect } from 'dva';
import { stringify } from 'qs';
import moment from 'moment';
import { LineChart } from '@/components/Charts';

const Option = Select.Option;
const formProps = {
  labelCol:{span: 4},
  wrapperCol:{ span: 18 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ statistics, loading }) => ({
  statistics,
  loading: loading.models.statistics,
}))
export default class Word extends PureComponent{
  state = {
    startValue: null,
    endValue: null,
  };

  componentDidMount(){
    const { dispatch, statistics:{ word_data_params:{ line1,line2 } } } = this.props;
    dispatch({
      type:'statistics/fetch_category_list',
      payload:{
        start:0,
        count:10000
      },
    })
    dispatch({
      type:'statistics/fetch_word_data',
      payload:{
        ...line1
      },
      params:{
        type:1
      }
    })
    dispatch({
      type:'statistics/fetch_word_data',
      payload:{
        ...line2
      },
      params:{
        type:2
      }
    })
  }

  disabledStartDate = (startValue) => {
    const endValue = this.state.endValue;
    if (!startValue || !endValue) {
      return false;
    }
    return startValue.valueOf() > endValue.valueOf();
  }

  disabledEndDate = (endValue) => {
    const startValue = this.state.startValue;
    if (!endValue || !startValue) {
      return false;
    }
    return endValue.valueOf() <= startValue.valueOf();
  }

  onChange = (field, value) => {
    this.setState({
      [field]: value,
    });
  }

  onStartChange = (value) => {
    this.onChange('startValue', value);
  }

  onEndChange = (value) => {
    this.onChange('endValue', value);
  }

  categoryChange = (value,type,stage) => {
    const { dispatch,form:{ setFieldsValue }, statistics:{ word_areaOption, word_groupOption, word_data_params }  } = this.props;
    dispatch({
      type:'statistics/fetch_category_item',
      payload:{
        id:value
      },
      params:{
        type:type,
        stage:stage
      },
      callback:(data)=>{
        dispatch({
          type:'statistics/changeAreaOption',
          payload:{
            ...word_areaOption,
            [`line${type}`]:data.area.map(e=>{
              return {
                value: e.id,
                label: e.name,
                isLeaf:false
              }
            })
          },params:{
            type:6,
          }
        })
        dispatch({
          type:'statistics/changeGroupOption',
          payload:{
            ...word_groupOption,
            [`line${type}`]:data.group
          },params:{
            type:6
          }
        })
        setFieldsValue({[`params[${type-1}][areaId]`]:undefined});
        setFieldsValue({[`params[${type-1}][group]`]:undefined});
        setFieldsValue({[`params[${type-1}][schoolId]`]:undefined});
      }
    })
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...word_data_params,
        [`line${type}`]:{
          ...word_data_params[`line${type}`],
          categoryId:value,
        }
      },
      params:{
        type:6
      }
    })
  }

  loadCascaderData = (selectedOptions,index) => {
    let { dispatch, statistics:{ word_areaOption } } = this.props;
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    dispatch({
      type:'global/fetch_area',
      payload:{
       id:targetOption.value
      },
      callback:(data)=>{
        targetOption.loading = false;
        targetOption.children = data.map(e=>{
          return {
            label: e.title,
            value: e.key,
            isLeaf:e.isLeaf
          }
        })
        dispatch({
          type:'statistics/changeAreaOption',
          payload:{
            ...word_areaOption
          },params:{
            type:6
          }
        })
      }
    })  
  }

  cascaderChange = (value,index) => {
    const { dispatch, form:{ setFieldsValue }, statistics:{ word_data_params } } = this.props;
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...word_data_params,
        [`line${index}`]:{
          ...word_data_params[`line${index}`],
          areaId:value,
        }
      },params:{
        type:6
      }
    })
    setFieldsValue({[`params[${index-1}][schoolId]`]:undefined});
    setFieldsValue({[`params[${index-1}][group]`]:undefined});
  }

  groupChange = (value,index) => {
    const { dispatch, form:{ setFieldsValue, getFieldValue }, statistics:{ word_schoolOption, word_data_params } } = this.props;
    let areaId = getFieldValue(`params[${index-1}][areaId]`);
    areaId = areaId[areaId.length-1];
    dispatch({
      type:'statistics/fetch_school_list',
      payload:{
        areaId:areaId,
        start:0,
        count:100000,
        group:value
      },
      callback:(list)=>{
        dispatch({
          type:'statistics/changeSchoolOption',
          payload:{
            ...word_schoolOption,
            [`line${index}`]:list
          },params:{
            type:6
          }
        })
        setFieldsValue({[`params[${index-1}][schoolId]`]:undefined});
      }
    })
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...word_data_params,
        [`line${index}`]:{
          ...word_data_params[`line${index}`],
          group:value,
        }
      },params:{
        type:6
      }
    })
  }

  schoolChange = (value,index) => {
    const { dispatch, statistics:{ word_data_params } } = this.props;
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...word_data_params,
        [`line${index}`]:{
          ...word_data_params[`line${index}`],
          schoolId:value,
        }
      },params:{
        type:6
      }
    })
  }

  handleSubmit = () => {
    const { dispatch,form:{validateFields}, statistics:{ word_data_params:{ line1, line2 } } } = this.props;
    validateFields((err, values) => {
      if (!err) {
        dispatch({
          type:'statistics/fetch_word_data',
          payload:{
            ...line1,
            startTime:moment(values.startTime).format('YYYY/MM/DD'),
            endTime:moment(values.endTime).format('YYYY/MM/DD'),
          },
          params:{
            type:1
          }
        })
        dispatch({
          type:'statistics/fetch_word_data',
          payload:{
            ...line2,
            startTime:moment(values.startTime).format('YYYY/MM/DD'),
            endTime:moment(values.endTime).format('YYYY/MM/DD'),
          },
          params:{
            type:2
          }
        })
      }
    })
  }

  handleReset = () =>{
    const { dispatch,form:{resetFields}} = this.props;
    resetFields();
    dispatch({
      type:'statistics/fetch_word_data',
      payload:{
        startTime:moment().subtract(7,'days').format('YYYY/MM/DD'),
        endTime:moment().format('YYYY/MM/DD'),
      },
      params:{
        type:1
      }
    })
    dispatch({
      type:'statistics/fetch_word_data',
      payload:{
        startTime:moment().subtract(7,'days').format('YYYY/MM/DD'),
        endTime:moment().format('YYYY/MM/DD'),
      },
      params:{
        type:2
      }
    })
  }

  render(){
    const { statistics:{ 
      word_data:{ line1,line2 }, 
      word_data_params:{ line1:line1_params,line2:line2_params }, 
      category_list,
      word_category_item:{ line1:line1_category,line2:line2_category },
      word_areaOption,
      word_groupOption,
      word_schoolOption
    },loading, form:{ getFieldDecorator } } = this.props;
    const chartData = line1.data.map((e,i)=>{
      return {
        x: e.createTime,
        y1: e.count,
        y2: line2.data[i] && line2.data[i].count || 0
      }
    })
    let exprot_params = {
      startTime:line1_params.startTime,
      endTime:line1_params.endTime,
      categoryId1:line1_params.categoryId,
      areaId1:line1_params.areaId && line1_params.areaId[line1_params.areaId.length-1],
      group1:line1_params.group,
      schoolId1:line1_params.schoolId,
      categoryId2:line2_params.categoryId,
      areaId2:line2_params.areaId && line2_params.areaId[line2_params.areaId.length-1],
      group2:line2_params.group,
      schoolId2:line2_params.schoolId,
    };
    for (const key in exprot_params) {
      const element = exprot_params[key];
      if (!element) {
        delete exprot_params[key]
      }
    }
    return (
        <Card bordered={false}>
            <Spin spinning={loading}>  
                <div>
                    <span className={styles.total}>
                        <p className={styles.total_title}>截止当前总量</p>
                        <div className={styles.total_num}>{line1.totalCount}</div>
                    </span>
                </div>
                <Row gutter={12} style={{marginTop:48}}>
                  <Col span={6}>
                    <FormItem
                        {...formProps}
                        label="赛项"
                        >
                        {getFieldDecorator(`params[0][categoryId]`,{
                          initialValue:line1_params.categoryId
                        })(
                            <Select onChange={(val)=>this.categoryChange(val,1,6)} style={{width:'100%'}}>
                                {
                                  category_list.map(e=><Select.Option key={e.id} value={e.id}><Tooltip title={e.name}>{e.name}</Tooltip></Select.Option>)
                                }
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                  <Col span={6}>
                    <FormItem
                        {...formProps}
                        label="区域"
                        >
                        {getFieldDecorator(`params[0][areaId]`,{
                          initialValue:line1_params.areaId
                        })(
                          <Cascader  allowClear={false} placeholder='' onChange={(value)=>this.cascaderChange(value,1)} loadData={(selectedOptions)=>this.loadCascaderData(selectedOptions,1)} options={word_areaOption.line1} style={{width:'100%'}} />
                        )}
                    </FormItem>
                  </Col>
                  <Col span={3}>
                    <FormItem
                        labelCol={{span: 7}}
                        wrapperCol={{ span:  17}}
                        label="组别"
                        >
                        {getFieldDecorator(`params[0][group]`,{
                          initialValue:line1_params.group
                        })(
                          <Select onChange={(value)=>this.groupChange(value,1)}  style={{width:'100%'}}>
                            {
                              word_groupOption.line1.map(e=>{
                                if(e === 1){
                                  return <Option key={e} value={e}>小学</Option>
                                }else if(e === 2){
                                  return <Option key={e} value={e}>初中</Option>
                                }else if(e === 3){
                                  return <Option key={e} value={e}>高中</Option>
                                }else if(e === 4){
                                  return <Option key={e} value={e}>中学</Option>
                                }
                              })
                            }
                          </Select>
                        )}
                    </FormItem>
                  </Col>
                  <Col span={5}>
                    <FormItem
                        {...formProps}
                        label="学校"
                        >
                        {getFieldDecorator(`params[0][schoolId]`,{
                          initialValue:line1_params.schoolId
                        })(
                            <Select onChange={(val)=>this.schoolChange(val,1)} style={{width:'100%'}}>
                              {
                                word_schoolOption.line1.map(e=>{
                                    return <Option key={e.id} value={e.id}>
                                              {e.name}
                                          </Option>
                                })
                              }
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                </Row>
                <Row gutter={12}>
                  <Col span={6}>
                    <FormItem
                        {...formProps}
                        label="赛项"
                        >
                        {getFieldDecorator(`params[1][categoryId]`,{
                          initialValue:line2_params.categoryId
                        })(
                            <Select onChange={(val)=>this.categoryChange(val,2,6)}  style={{width:'100%'}}>
                              {
                                category_list.map(e=><Select.Option key={e.id} value={e.id}><Tooltip title={e.name}>{e.name}</Tooltip></Select.Option>)
                              }
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                  <Col span={6}>
                    <FormItem
                        {...formProps}
                        label="区域"
                        >
                        {getFieldDecorator(`params[1][areaId]`,{
                          initialValue:line2_params.areaId
                        })(
                          <Cascader  allowClear={false} placeholder='' onChange={(value)=>this.cascaderChange(value,2)} loadData={(selectedOptions)=>this.loadCascaderData(selectedOptions,2)} options={word_areaOption.line2} style={{width:'100%'}} />
                        )}
                    </FormItem>
                  </Col>
                  <Col span={3}>
                    <FormItem
                        labelCol={{span: 7}}
                        wrapperCol={{ span:  17}}
                        label="组别"
                        >
                        {getFieldDecorator(`params[1][group]`,{
                          initialValue:line2_params.group
                        })(
                          <Select onChange={(value)=>this.groupChange(value,2)}  style={{width:'100%'}}>
                            {
                              word_groupOption.line2.map(e=>{
                                if(e === 1){
                                  return <Option key={e} value={e}>小学</Option>
                                }else if(e === 2){
                                  return <Option key={e} value={e}>初中</Option>
                                }else if(e === 3){
                                  return <Option key={e} value={e}>高中</Option>
                                }else if(e === 4){
                                  return <Option key={e} value={e}>中学</Option>
                                }
                              })
                            }
                          </Select>
                        )}
                    </FormItem>
                  </Col>
                  <Col span={5}>
                    <FormItem
                        {...formProps}
                        label="学校"
                        >
                        {getFieldDecorator(`params[1][schoolId]`,{
                          initialValue:line2_params.schoolId
                        })(
                            <Select onChange={(val)=>this.schoolChange(val,2)} style={{width:'100%'}}>
                              {
                                word_schoolOption.line2.map(e=>{
                                    return <Option key={e.id} value={e.id}>
                                              {e.name}
                                          </Option>
                                })
                              }
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                </Row>
                <div style={{marginTop:24,width:600}}>
                  <Row gutter={16}>
                    <Col span={7}>
                      <FormItem
                        wrapperCol={{ span:  24}}
                        >
                        {getFieldDecorator('startTime', {
                            rules: [{ required: true, message: '开始时间不能为空' }],
                            initialValue:moment(line1_params.startTime)
                        })(
                            <DatePicker disabledDate={this.disabledStartDate} onChange={this.onStartChange} format='YYYY/MM/DD' placeholder="请选择开始时间"/>
                        )}
                    </FormItem>
                    </Col>
                    <Col span={7}>
                      <FormItem
                       wrapperCol={{ span:  24}}
                        >
                        {getFieldDecorator('endTime', {
                            rules: [{ required: true, message: '结束时间不能为空' }],
                            initialValue:moment(line1_params.endTime)
                        })(
                            <DatePicker disabledDate={this.disabledEndDate} onChange={this.onEndChange} format='YYYY/MM/DD' placeholder="请选择结束时间"/>
                        )}
                      </FormItem>
                    </Col>
                    <Col span={3}>
                      <Button loading={loading} onClick={this.handleSubmit} style={{marginTop:3}} type="primary">统计</Button>
                    </Col>
                    <Col span={3}>
                      <Button loading={loading} onClick={this.handleReset} style={{marginTop:3}} type="primary">重置</Button>
                    </Col>
                    <Col span={3}>
                      <a href={`/api/stats/word/export?${stringify(exprot_params)}`}><Button loading={loading} style={{marginTop:3}}>导出</Button></a>
                    </Col>
                  </Row>
                </div>
                <div>
                  <LineChart
                    height={300}
                    data={chartData}
                    titleMap={{ y1: '数量1', y2: '数量2' }}
                  />
                </div>
            </Spin>
        </Card>
    )
  }
}
