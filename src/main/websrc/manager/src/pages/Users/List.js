import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Drawer, Form, DatePicker, Input } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

const formProps = {
  labelCol:{span: 6},
  wrapperCol:{ span: 16 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ users, loading }) => ({
  users,
  loading: loading.models.users,
}))
export default class Users extends PureComponent{
  state={
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

  componentDidMount(){
    const { dispatch, users:{ users_params } } = this.props;
    this.get_data(users_params);
  }

  handleSubmit = () => {
    const { dispatch,form:{validateFields},users:{ users_params } } = this.props;
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
          count:users_params.count,
          start:0
        });
      }
    })
  }

  handleReset = () => {
    const { dispatch,form:{resetFields},users:{ users_params } } = this.props;
    this.get_data({
      count:users_params.count,
      start:0
    });
    resetFields();
  }

  get_data = params => {
    const { dispatch } = this.props;
    dispatch({
      type:'users/fetch_users_list',
      payload:params
    })
  }

  handlePageChange = ({current}) => {
    const {users:{ users_params },dispatch} = this.props;
    const start = (current-1)*users_params.count;
    this.get_data({
      ...users_params,
      start:start
    });
  }

  undo = (id,userFlag) => {
    const { dispatch } = this.props;
    Modal.confirm({
      title: '提示?',
      content: '确认禁用此用户吗？',
      okText:'确定',
      cancelText:'取消',
      onOk() {
        dispatch({
          type:'users/fetch_users_disable',
          payload:{
            id:id,
            flag:userFlag
          }
        })
      },
    });
  }

  render(){
    const columns = [{
      title: '登录名',
      dataIndex: 'name',
      key: 'name',
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width:180
    }, {
      title: '操作',
      dataIndex: 'id',
      key: 'id',
      width:60,
      render:(val,record)=>(<span style={{display: 'flex',justifyContent: 'space-around', color:'#005d7e'}}>
        <Tooltip title="重置密码"><Icon onClick={()=>router.push(`/users/list/reset/${val}`)} style={{cursor:'pointer'}} type="reload" /></Tooltip> 
        {
          record.userFlag?<Tooltip title="禁用"><Icon onClick={()=>this.undo(val,record.userFlag)} style={{cursor:'pointer'}} type="stop" /></Tooltip>:<Tooltip title="解除禁用"><Icon onClick={()=>this.undo(val,record.userFlag)} style={{cursor:'pointer'}} type="check-circl" /></Tooltip>
        }
      </span>)
    }];

    const { users:{users_list, users_params }, loading, form:{ getFieldDecorator } } = this.props;
    const { value, totalCount } = users_list;
    const { start, count, name } = users_params;
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
                    label="登录名"
                    >
                    {getFieldDecorator('name', {
                      initialValue:name
                    })(
                        <Input placeholder="请填写登录名"/>
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
               <a href="/api/user/export"><Button type="primary">导出</Button></a>
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
