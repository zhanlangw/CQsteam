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
export default class Award extends PureComponent{
  componentDidMount(){
    const { dispatch, statistics:{ award_data_params:{ line1,line2 } } } = this.props;
    dispatch({
      type:'statistics/fetch_category_list',
      payload:{
        start:0,
        count:10000
      },
    })
    dispatch({
      type:'statistics/fetch_award_data',
      payload:{
        ...line1
      },
      params:{
        type:1
      }
    })
    dispatch({
      type:'statistics/fetch_award_data',
      payload:{
        ...line2
      },
      params:{
        type:2
      }
    })
  }

  categoryChange = (value,type,stage) => {
    const { dispatch,form:{ setFieldsValue }, statistics:{ award_areaOption, award_groupOption, award_data_params }  } = this.props;
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
            ...award_areaOption,
            [`line${type}`]:data.area.map(e=>{
              return {
                value: e.id,
                label: e.name,
                isLeaf:false
              }
            })
          },params:{
            type:3,
          }
        })
        dispatch({
          type:'statistics/changeGroupOption',
          payload:{
            ...award_groupOption,
            [`line${type}`]:data.group
          },params:{
            type:3
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
        ...award_data_params,
        [`line${type}`]:{
          ...award_data_params[`line${type}`],
          categoryId:value,
        }
      },
      params:{
        type:3
      }
    })
  }

  loadCascaderData = (selectedOptions,index) => {
    let { dispatch, statistics:{ award_areaOption } } = this.props;
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
            ...award_areaOption
          },params:{
            type:3
          }
        })
      }
    })  
  }

  cascaderChange = (value,index) => {
    const { dispatch, form:{ setFieldsValue }, statistics:{ award_data_params } } = this.props;
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...award_data_params,
        [`line${index}`]:{
          ...award_data_params[`line${index}`],
          areaId:value,
        }
      },params:{
        type:3
      }
    })
    setFieldsValue({[`params[${index-1}][schoolId]`]:undefined});
    setFieldsValue({[`params[${index-1}][group]`]:undefined});
  }

  groupChange = (value,index) => {
    const { dispatch, form:{ setFieldsValue, getFieldValue }, statistics:{ award_schoolOption, award_data_params } } = this.props;
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
            ...award_schoolOption,
            [`line${index}`]:list
          },params:{
            type:3
          }
        })
        setFieldsValue({[`params[${index-1}][schoolId]`]:undefined});
      }
    })
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...award_data_params,
        [`line${index}`]:{
          ...award_data_params[`line${index}`],
          group:value,
        }
      },params:{
        type:3
      }
    })
  }

  schoolChange = (value,index) => {
    const { dispatch, statistics:{ award_data_params } } = this.props;
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...award_data_params,
        [`line${index}`]:{
          ...award_data_params[`line${index}`],
          schoolId:value,
        }
      },params:{
        type:3
      }
    })
  }

  genderChange = (value,index) => {
    const { dispatch, statistics:{ award_data_params } } = this.props;
    dispatch({
      type:'statistics/changeDataParams',
      payload:{
        ...award_data_params,
        [`line${index}`]:{
          ...award_data_params[`line${index}`],
          gender:value,
        }
      },params:{
        type:3
      }
    })
  }
  
  handleSubmit = () => {
    const { dispatch,form:{validateFields}, statistics:{ award_data_params:{ line1, line2 } } } = this.props;
    validateFields((err, values) => {
      if (!err) {
        dispatch({
          type:'statistics/fetch_award_data',
          payload:{
            ...line1,
          },
          params:{
            type:1
          }
        })
        dispatch({
          type:'statistics/fetch_award_data',
          payload:{
            ...line2,
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
      type:'statistics/fetch_award_data',
      payload:{
      },
      params:{
        type:1
      }
    })
    dispatch({
      type:'statistics/fetch_award_data',
      payload:{
      },
      params:{
        type:2
      }
    })
  }

  render(){
    const { statistics:{ 
      award_data:{ line1,line2 }, 
      award_data_params:{ line1:line1_params,line2:line2_params }, 
      category_list,
      award_category_item:{ line1:line1_category,line2:line2_category },
      award_areaOption,
      award_groupOption,
      award_schoolOption
    },loading, form:{ getFieldDecorator } } = this.props;
    const chartData = line1.map((e,i)=>{
      return {
        x: e.prize,
        y1: e.count,
        y2: line2[i] && line2[i].count || 0
      }
    })

    let exprot_params = {
      categoryId1:line1_params.categoryId,
      areaId1:line1_params.areaId && line1_params.areaId[line1_params.areaId.length-1],
      group1:line1_params.group,
      schoolId1:line1_params.schoolId,
      gender1:line1_params.gender,
      categoryId2:line2_params.categoryId,
      areaId2:line2_params.areaId && line2_params.areaId[line2_params.areaId.length-1],
      group2:line2_params.group,
      schoolId2:line2_params.schoolId,
      gender2:line2_params.gender,
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
                <Row gutter={12} style={{marginTop:24}}>
                  <Col span={6}>
                    <FormItem
                        {...formProps}
                        label="赛项"
                        >
                        {getFieldDecorator(`params[0][categoryId]`,{
                          initialValue:line1_params.categoryId
                        })(
                            <Select onChange={(val)=>this.categoryChange(val,1,3)} style={{width:'100%'}}>
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
                          <Cascader  allowClear={false} placeholder='' onChange={(value)=>this.cascaderChange(value,1)} loadData={(selectedOptions)=>this.loadCascaderData(selectedOptions,1)} options={award_areaOption.line1} style={{width:'100%'}} />
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
                              award_groupOption.line1.map(e=>{
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
                                award_schoolOption.line1.map(e=>{
                                    return <Option key={e.id} value={e.id}>
                                              {e.name}
                                          </Option>
                                })
                              }
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                  <Col span={3}>
                    <FormItem
                        labelCol={{span: 7}}
                        wrapperCol={{ span:  17}}
                        label="性别"
                        >
                        {getFieldDecorator(`params[0][gender]`,{
                          initialValue:line1_params.gender
                        })(
                            <Select onChange={(val)=>this.genderChange(val,1)} style={{width:'100%'}}>
                              <Select.Option value="男">男</Select.Option>
                              <Select.Option value="女">女</Select.Option>
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
                            <Select onChange={(val)=>this.categoryChange(val,2,3)}  style={{width:'100%'}}>
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
                          <Cascader  allowClear={false} placeholder='' onChange={(value)=>this.cascaderChange(value,2)} loadData={(selectedOptions)=>this.loadCascaderData(selectedOptions,2)} options={award_areaOption.line2} style={{width:'100%'}} />
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
                              award_groupOption.line2.map(e=>{
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
                                award_schoolOption.line2.map(e=>{
                                    return <Option key={e.id} value={e.id}>
                                              {e.name}
                                          </Option>
                                })
                              }
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                  <Col span={3}>
                    <FormItem
                        labelCol={{span: 7}}
                        wrapperCol={{ span:  17}}
                        label="性别"
                        >
                        {getFieldDecorator(`params[1][gender]`,{
                          initialValue:line2_params.gender
                        })(
                            <Select  onChange={(val)=>this.genderChange(val,2)} style={{width:'100%'}}>
                                <Select.Option value="男">男</Select.Option>
                                <Select.Option value="女">女</Select.Option>
                            </Select>
                        )}
                    </FormItem>
                  </Col>
                </Row>
                <div style={{marginTop:24,width:600}}>
                  <Row gutter={16}>
                    <Col span={4}>
                      <Button loading={loading} onClick={this.handleSubmit} style={{marginTop:3}} type="primary">统计</Button>
                    </Col>
                    <Col span={4}>
                      <Button loading={loading} onClick={this.handleReset} style={{marginTop:3}} type="primary">重置</Button>
                    </Col>
                    <Col span={4}>
                      <a href={`/api/stats/prize/export?${stringify(exprot_params)}`}><Button loading={loading} style={{marginTop:3}}>导出</Button></a>
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
