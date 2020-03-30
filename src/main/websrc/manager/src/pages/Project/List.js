import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Select, Spin, Input, Radio, Drawer, DatePicker, message, Cascader } from 'antd';
import Link from 'umi/link';
import moment from 'moment';
import _ from 'lodash';
import { connect } from 'dva';
import router from 'umi/router';
import { stringify } from 'qs';
import SelectTag from '@/components/SelectTag';

const group = ['小学','初中','高中','中学'];
const formProps = {
  labelCol:{span: 8},
  wrapperCol:{ span: 16 },
};
const FormItem = Form.Item;
const Option = Select.Option;

@Form.create()
@connect(({ project, loading }) => ({
  project,
  loading: loading.effects['project/fetch_project_list'],
}))
export default class Project extends PureComponent{
  state = {
    import_modal:false,
    query:false,
    startValue: null,
    endValue: null,
  }

  showDrawer = () => {
    this.setState({
      query: true,
    });
  };

  closeDrawer = () => {
    this.setState({
      query: false,
    });
  };

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

  handleSubmit = () => {
    const { dispatch,form:{validateFields}, project:{ project_params } } = this.props;
    const self = this;
    validateFields(['number','name','category','group','district','school','docFlag','submitFlag','prize','startTime','endTime'],(err, values) => {
      if (!err) {
        if(values.startTime){
          values.startTime = moment(values.startTime).format('YYYY/MM/DD HH:mm:ss');
        }
        if(values.endTime){
          values.endTime = moment(values.endTime).format('YYYY/MM/DD HH:mm:ss');
        }
        if(values.category){
          values.category = values.category.label;
        }
        self.get_data({
          ...values,
          count:project_params.count,
          start:0
        });
      }
    })
  }

  handleReset = () => {
    const { dispatch,form:{resetFields}, project:{ project_params } } = this.props;
    this.get_data({
      count:project_params.count,
      start:0
    });
    resetFields();
  }

handleOk = () => {
  const { dispatch,form:{validateFields}, project:{ project_params } } = this.props;
    const self = this;
    validateFields(['categoryId', 'stage'],(err, values) => {
      if (!err) {
        const formData = new FormData();
        formData.append("file", self.fileInputElement.input.files[0]);
        dispatch({
          type:'project/fetch_import_result',
          payload:formData,
          params:{
            stage:values.stage,
            categoryId:values.categoryId
          },
          callback:()=>{
            self.get_data(project_params);
            self.closeImportModal();
           }
        })
      }
    })
}

  categoryTypeChange = (value) => {
    const { dispatch } = this.props;
    dispatch({
      type:'project/fetch_category_stage',
      payload:{
        id:value
      },
    })
  }

  openMessage = () => {
    const { dispatch, project:{ project_params } } = this.props;
    Modal.confirm({
      title: '提示',
      content: '确认给以下列表项发送短信提醒吗？',
      okText: '确认',
      cancelText: '取消',
      onOk:()=>{
        dispatch({
          type:'project/fetch_send_msg',
          payload:{
            ...project_params
          },
        })
      }
    });
  }

  closeImportModal = () => {
    this.setState({
      import_modal:false
    })
  }

  openImportModal = () => {
    this.setState({
      import_modal:true
    })
  }

  componentDidMount(){
    const { dispatch, project:{ project_params, area_tree } } = this.props;
    this.get_data(project_params);
    dispatch({
      type:'project/fetch_category_list',
      payload:{
        start:0,
        count:1000
      }
    }) 
    if(!area_tree.length){
      dispatch({
        type:'project/fetch_area_tree',
        params:{
          notSetData:false
        },
      }) 
    }
  }

  get_data = params => {
    const { dispatch } = this.props;
    dispatch({
      type:'project/fetch_project_list',
      payload:params
    })
  }

  handlePageChange = ({current}) => {
    const {project:{ project_params },dispatch} = this.props;
    const start = (current-1)*project_params.count;
    this.get_data({
      ...project_params,
      start:start
    });
  }

  handleDel = id => {
    const { dispatch } = this.props;
    Modal.confirm({
        title: '提示',
        content: '确定删除此项目吗?',
        okText:'确定',
        cancelText:'取消',
        onOk() {
            dispatch({
                type:'project/fetch_project_del',
                payload:{
                    id:id
                }
              })
        },
        onCancel() {},
    })
}

  loadCascaderData = (selectedOptions) => {
    const { dispatch, project:{ area_tree } } = this.props;
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    dispatch({
      type:'project/fetch_area_tree',
      payload:{
       id:targetOption.value
      },
      params:{
        notSetData:true
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
          type:'project/area_tree',
          payload:[...area_tree],
        })
      }
    })  
  }

  render(){
    const columns = [{
      title: '项目编号',
      dataIndex: 'number',
      key: 'number',
    }, {
      title: '项目名称',
      dataIndex: 'name',
      key: 'name',
    }, {
      title: '赛项',
      dataIndex: 'category',
      key: 'category',
    }, {
      title: '组别',
      dataIndex: 'group',
      key: 'group',
      render:(val)=>group[val-1]
    }, {
      title: '区域',
      dataIndex: 'district',
      key: 'district',
    }, {
      title: '学校',
      dataIndex: 'school',
      key: 'school',
    }, {
      title: '是否已提交word文档',
      dataIndex: 'docFlag',
      key: 'docFlag',
      render:(val)=>val?'是':'否'
    }, {
      title: '是否已最终提交',
      dataIndex: 'submitFlag',
      key: 'submitFlag',
      render:(val)=>val?'是':'否'
    }, {
      title: '获奖等级',
      dataIndex: 'prize',
      key: 'prize',
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    }, {
      title: '提交时间',
      dataIndex: 'submitTime',
      key: 'submitTime',
    }, {
      title: '操作',
      dataIndex: 'id',
      key: 'id',
      width:60,
      render:val=>(<span style={{display: 'flex',justifyContent: 'space-around', color:'#005d7e'}}>
        <Tooltip title="修改"><Icon onClick={()=>router.push(`/project/list/edit/${val}`)} style={{cursor:'pointer'}} type="form" /></Tooltip> 
        <Tooltip title="删除"><Icon onClick={()=>this.handleDel(val)} style={{cursor:'pointer'}} type="delete" /></Tooltip> 
      </span>)
    }];

    const { project:{project_list, project_params, category_stage, category_list, category_type, area_tree }, loading,  form:{ getFieldDecorator } } = this.props;
    const { import_modal } = this.state;
    const { value, totalCount } = project_list;
    const { start, count, category, number, name, group:group_params, district, school, docFlag, submitFlag, prize, startTime ,endTime } = project_params;
    const paginationProps = {
      pageSize: count,
      total: totalCount,
      current: start/count+1,
    };
    const export_params = _.clone(project_params);
    delete export_params.start;
    delete export_params.count;
    const stage = category_stage[category_type] || [];
    return (
      <Card bordered={false} bodyStyle={{padding:'12px 0'}}>
        <Drawer
                title="筛选"
                placement="right"
                width={450}
                onClose={this.closeDrawer}
                visible={this.state.query}
                bodyStyle={{paddingBottom:48}}
              >
                <FormItem
                    {...formProps}
                    label="项目名称"
                    >
                    {getFieldDecorator('name', {
                      initialValue:name
                    })(
                        <Input placeholder="请填写名称"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="项目编号"
                    >
                    {getFieldDecorator('number', {
                      initialValue:number
                    })(
                        <Input placeholder="请填写项目编号"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="赛项"
                    >
                    {getFieldDecorator('category', {
                      initialValue:category && { label: category } || undefined
                    })(
                      <Select labelInValue placeholder="请选择赛项" style={{width:'100%'}}>
                          {
                            category_list.map(e=><Select.Option key={e.id} value={e.id}>{e.name}</Select.Option>)
                          }
                        </Select>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="区域"
                    >
                    {getFieldDecorator('district', {
                      initialValue:district
                    })(
                      <Cascader  allowClear={false} placeholder="请选择区域" loadData={this.loadCascaderData} options={area_tree} style={{width:'100%'}} />
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="组别"
                    >
                    {getFieldDecorator('group', {
                      initialValue:group_params
                    })(
                      <Select placeholder="请选择组别" style={{width:'100%'}}>
                        <Option value={1}>小学</Option>
                        <Option value={2}>初中</Option>
                        <Option value={3}>高中</Option>
                        <Option value={4}>中学</Option>
                      </Select>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="学校"
                    >
                    {getFieldDecorator('school', {
                      initialValue:school
                    })(
                      <Input placeholder="请填写学校"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="获奖等级"
                    >
                    {getFieldDecorator('prize', {
                      initialValue:prize
                    })(
                      <Select style={{width:'100%'}}  placeholder="请选择获奖等级">
                          <Option value='一等奖'>一等奖</Option>
                          <Option value='二等奖'>二等奖</Option>
                          <Option value='三等奖'>三等奖</Option>
                          <Option value='优秀奖'>优秀奖</Option>
                      </Select>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="是否提交Word文档"
                    >
                    {getFieldDecorator('docFlag', {
                      initialValue:docFlag
                    })(
                        <SelectTag />
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="是否最终提交"
                    >
                    {getFieldDecorator('submitFlag', {
                      initialValue:submitFlag
                    })(
                        <SelectTag />
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="开始时间"
                    >
                    {getFieldDecorator('startTime', {
                            initialValue:startTime && moment(startTime) || undefined
                        })(
                            <DatePicker showTime style={{width:'100%'}} disabledDate={this.disabledStartDate} onChange={this.onStartChange} format='YYYY/MM/DD HH:mm:ss' placeholder="请选择开始时间"/>
                        )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="截止时间"
                    >
                    {getFieldDecorator('endTime', {
                            initialValue:endTime && moment(endTime) || undefined
                        })(
                            <DatePicker showTime style={{width:'100%'}} disabledDate={this.disabledEndDate} onChange={this.onEndChange} format='YYYY/MM/DD HH:mm:ss' placeholder="请选择结束时间"/>
                        )}
                </FormItem>
                <div style={{
                  textAlign:'center',
                  position: 'absolute',
                  bottom: 0,
                  left: '50%',
                  zIndex:1,
                  padding:12,
                  width: '100%',
                  transform: 'translateX(-50%)',
                  background:'#fff',
                  borderTop:'1px solid #e8e8e8'
                  }}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">查询</Button>
                    <Button loading={loading} onClick={this.handleReset} style={{marginLeft:48}}>重置</Button>
                </div>
              </Drawer>
            <Modal
              title="比赛结果导入"
              visible={import_modal}
              onOk={this.handleOk}
              onCancel={this.closeImportModal}
            >
              <FormItem
                    {...formProps}
                    label="类别"
                    >
                    {getFieldDecorator('categoryId', {
                        rules: [{ required: true, message: '请选择类别' }],
                    })(
                        <Select onSelect={this.categoryTypeChange} placeholder="请选择类别" style={{width:'100%'}}>
                          {
                            category_list.map(e=><Select.Option key={e.id} value={e.id}>{e.name}</Select.Option>)
                          }
                        </Select>
                    )}
                </FormItem>
                {
                  stage.length && (<FormItem
                                    {...formProps}
                                    label="阶段"
                                    >
                                    {getFieldDecorator('stage', {
                                        rules: [{ required: true, message: '请选择阶段' }],
                                    })(
                                        <Radio.Group>
                                          {
                                            stage.map(e=><Radio key={e.value} value={e.value}>{e.title}</Radio>)
                                          }
                                        </Radio.Group>
                                    )}
                                </FormItem>) || ''
                }
                <FormItem
                    {...formProps}
                    label="Excel文件"
                    >
                    {getFieldDecorator('file', {
                        rules: [{ required: true, message: '请上传Excel文件' }],
                    })(
                      <Input ref={ref=>this.fileInputElement=ref} accept=".xls,.xlsx" type="file"/>
                    )}
                </FormItem>
            </Modal>
            <div className='header'>
                <div>
                  <a href={`/api/project/export?${stringify(export_params)}`}><Button type="primary">导出</Button></a>
                  <Button style={{marginLeft:12}} onClick={this.openImportModal} type="primary">结果批量导入</Button>
                  <Button style={{marginLeft:12}} onClick={this.openMessage} type="primary">短信提醒</Button>
                </div>
               <span onClick={this.showDrawer} className='pointer'><Icon style={{height:20}} type="filter"/> 筛选</span>
            </div>
            <div>
              <Table 
                size="middle"
                loading={loading}
                columns={columns} 
                rowKey={record => record.id}
                onChange={this.handlePageChange}
                dataSource={value} 
                pagination={paginationProps}
              /> 
            </div>
        </Card>
    )
  }
}
