import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Drawer, Form, DatePicker, Input } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';
import moment from 'moment';

const formProps = {
  labelCol:{span: 8},
  wrapperCol:{ span: 16 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ sponsors, loading }) => ({
  sponsors,
  loading: loading.models.sponsors,
}))
export default class Sponsors extends PureComponent{
  state={
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
    const { dispatch,form:{validateFields},sponsors:{ sponsors_params } } = this.props;
    const self = this;
    validateFields((err, values) => {
      if (!err) {
        if(values.startTime){
          values.startTime = moment(values.startTime).format('YYYY/MM/DD HH:mm:ss');
        }
        if(values.endTime){
          values.endTime = moment(values.endTime).format('YYYY/MM/DD HH:mm:ss');
        }
        self.get_data({
          ...values,
          count:sponsors_params.count,
          start:0
        });
      }
    })
  }

  handleReset = () => {
    const { dispatch,form:{resetFields},sponsors:{ sponsors_params } } = this.props;
    this.get_data({
      count:sponsors_params.count,
      start:0
    });
    resetFields();
  }

  componentDidMount(){
    const { dispatch, sponsors:{ sponsors_params } } = this.props;
    this.get_data(sponsors_params);
  }

  get_data = params => {
    const { dispatch } = this.props;
    dispatch({
      type:'sponsors/fetch_sponsors_list',
      payload:params
    })
  }

  handleDel = id => {
    const { dispatch } = this.props;
    Modal.confirm({
      title: '提示?',
      content: '确认删除吗？',
      okText:'确定',
      cancelText:'取消',
      onOk() {
        dispatch({
          type:'sponsors/fetch_sponsors_del',
          payload:{
            id:id
          }
        })
      },
    });
  }

  handlePageChange = ({current}) => {
    const {sponsors:{ sponsors_params },dispatch} = this.props;
    const start = (current-1)*sponsors_params.count;
    this.get_data({
      ...sponsors_params,
      start:start
    });
  }

  render(){
    const columns = [{
      title: '赞助商URL',
      dataIndex: 'url',
      key: 'url',
    }, {
      title: '图片预览',
      dataIndex: 'path',
      key: 'path',
      render:val=><img src={val} style={{width:60,height:45}}/>
    }, {
      title: '创建人',
      dataIndex: 'creator',
      key: 'creator',
    }, {
      title: '更新时间',
      dataIndex: 'updateTime',
      key: 'updateTime',
      width:180
    }, {
      title: '操作',
      dataIndex: 'id',
      key: 'id',
      width:60,
      render:val=>(<span style={{display: 'flex',justifyContent: 'space-around', color:'#005d7e'}}>
        <Tooltip title="修改"><Icon onClick={()=>router.push(`/sponsors/list/edit/${val}`)} style={{cursor:'pointer'}} type="form" /></Tooltip> 
        <Tooltip title="删除"><Icon onClick={()=>this.handleDel(val)} style={{cursor:'pointer'}} type="delete" /></Tooltip> 
      </span>)
    }];

    const { sponsors:{sponsors_list, sponsors_params }, loading, form:{ getFieldDecorator } } = this.props;
    const { value, totalCount } = sponsors_list;
    const { start, count, url, startTime, endTime, creator } = sponsors_params;
    const paginationProps = {
      pageSize: count,
      total: totalCount,
      current: start/count+1,
    };
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
                    label="赞助商URL"
                    >
                    {getFieldDecorator('url', {
                      initialValue:url
                    })(
                        <Input placeholder="请填写URL"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="创建人"
                    >
                    {getFieldDecorator('creator', {
                      initialValue:creator
                    })(
                        <Input placeholder="请填写创建人"/>
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
            <div className='header'>
               <Button onClick={()=>router.push(`/sponsors/list/add`)} type="primary" icon="plus">新增赞助商</Button>
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
