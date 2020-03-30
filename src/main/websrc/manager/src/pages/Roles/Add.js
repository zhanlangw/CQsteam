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
export default class Add extends PureComponent{
    handleSubmit = () => {
        const { dispatch,form:{validateFields} } = this.props;
        validateFields((err, values) => {
          if (!err) {
            dispatch({
                type:'role/fetch_role_add',
                payload:{
                 ...values,
                 permissions:values.permissions.filter(e=>e.startsWith('/api/'))
                },
              })
          }
        })
    }
    
  render(){
    const { form:{ getFieldDecorator }, loading } = this.props;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="角色名称"
                    >
                    {getFieldDecorator('name', {
                        rules: [{ required: true, message: '角色名称不能为空' }],
                    })(
                        <Input placeholder="请填写角色名称"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="角色权限"
                    >
                    {getFieldDecorator('permissions', {
                        rules: [{ required: true, message: '角色权限不能为空' }],
                    })(
                      <CheckTree />
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/roles/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
