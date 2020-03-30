import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Select, Drawer, Input } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

const formProps = {
  labelCol:{span: 4},
  wrapperCol:{ span: 20 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ exports, loading }) => ({
  exports,
  loading: loading.models.exports,
}))
export default class Export extends PureComponent{
  componentDidMount(){
    const { dispatch, exports:{ exports_params } } = this.props;
    this.get_data(exports_params);
  }

  state = {
    export_modal:false,
    query:false,
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

  handleSubmit = () => {
    const { dispatch,form:{validateFields}, exports:{ exports_params } } = this.props;
    const self = this;
    validateFields(['name','status'],(err, values) => {
      if (!err) {
        self.get_data({
          ...values,
          count:exports_params.count,
          start:0
        });
      }
    })
  }

  handleReset = () => {
    const { dispatch,form:{resetFields}, exports:{ exports_params } } = this.props;
    this.get_data({
      count:exports_params.count,
      start:0
    });
    resetFields();
  }
  
  openExportModal = () => {
    this.setState({
      export_modal:true
    })
  }

  closeExportModal = () => {
    this.setState({
      export_modal:false
    })
  }

  get_data = params => {
    const { dispatch } = this.props;
    dispatch({
      type:'exports/fetch_exports_list',
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
          type:'exports/fetch_exports_del',
          payload:{
            id:id
          }
        })
      },
    });
  }

  handlePageChange = ({current}) => {
    const {exports:{ exports_params },dispatch} = this.props;
    const start = (current-1)*exports_params.count;
    this.get_data({
      ...exports_params,
      start:start
    });
  }

  handleOk = () => {
    const { dispatch,form:{validateFields} } = this.props;
      const self = this;
      validateFields(['type'],(err, values) => {
        if (!err) {
          dispatch({
            type:'exports/fetch_exports_add',
            payload:{
              ...values
            },
            callback:()=>{
              self.closeExportModal();
             }
          })
        }
      })
  }

  render(){
    const columns = [{
      title: '状态',
      dataIndex: 'status',
      key: 'status',
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width:180
    }, {
      title: '下载链接',
      dataIndex: 'downloadUrl',
      key: 'downloadUrl',
      render:val=><a href={val}>{val}</a>
    }, {
      title: '操作',
      dataIndex: 'id',
      key: 'id',
      render:val=>(<span style={{display: 'flex',justifyContent: 'space-around', color:'#005d7e'}}>
        <Tooltip title="删除"><Icon onClick={()=>this.handleDel(val)} style={{cursor:'pointer'}} type="delete" /></Tooltip> 
      </span>)
    }];

    const { exports:{exports_list, exports_params }, loading, form:{ getFieldDecorator } } = this.props;
    const { export_modal } = this.state;
    const { value, totalCount } = exports_list;
    const { start, count, status } = exports_params;
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
                {/* <FormItem
                    {...formProps}
                    label="项目名称"
                    >
                    {getFieldDecorator('name', {
                      initialValue:name
                    })(
                        <Input placeholder="请填写名称"/>
                    )}
                </FormItem> */}
                <FormItem
                    {...formProps}
                    label="状态"
                    >
                    {getFieldDecorator('status', {
                            initialValue:status
                        })(
                            <Select style={{width:'100%'}} placeholder="请选择状态">
                                 <Select.Option value={1}>
                                  正在导出
                                </Select.Option>     
                                <Select.Option value={2}>
                                  导出成功
                                </Select.Option>     
                                <Select.Option value={3}>
                                导出失败
                                </Select.Option>     
                            </Select>
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
              title="智能导出"
              visible={export_modal}
              onOk={this.handleOk}
              width={600}
              onCancel={this.closeExportModal}
            >
              <FormItem
                    {...formProps}
                    label="导出方式"
                    >
                    {getFieldDecorator('type', {
                        initialValue:1
                    })(
                        <Select style={{width:'100%'}}>
                            <Select.Option value={1}>按赛项、省份、区县、组别、学校、项目的6级目录结构组织数据</Select.Option>
                            <Select.Option value={2}>按赛项、省份、组别、项目的4级目录结构组织数据</Select.Option>
                        </Select>
                    )}
                </FormItem>
            </Modal>
            <div className='header'>
               <Button onClick={this.openExportModal} type="primary">智能导出</Button>
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
