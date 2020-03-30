import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, message, Tree, Upload } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';
import CheckTree from './CheckTree';

const { TreeNode } = Tree;
const formProps = {
    labelCol:{span: 4},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ role, loading }) => ({
  role,
  loading: loading.models.role,
}))
export default class Item extends PureComponent{
    componentDidMount(){
      const { dispatch, match:{params:{id}} } = this.props;
        dispatch({
          type:'role/fetch_role_item',
          payload:{
           id:id
          }
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, role:{ role_item }, match:{params:{id}}} = this.props;
    const item = role_item[id] || {};
    const { name, permissions=[] } = item;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="角色名称"
                    >
                    {getFieldDecorator('name', {
                        roles: [{ required: true, message: '角色名称不能为空' }],
                        initialValue:name
                    })(
                        <Input disabled placeholder="请填写角色名称"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="角色权限"
                    >
                    {getFieldDecorator('permissions', {
                        roles: [{ required: true, message: '角色权限不能为空' }],
                        initialValue:permissions.map(e=>e.interfaceUrl)
                    })(
                      <CheckTree disabled/>
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button onClick={()=>{router.push('/roles/list')}}>返回</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
