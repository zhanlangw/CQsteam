import React, { PureComponent } from 'react';
import { Card, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, Select, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

const formProps = {
    labelCol:{span: 8},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ manager, loading }) => ({
  manager,
  loading: loading.models.manager,
}))
export default class Add extends PureComponent{
    state = {
      confirmDirty:false,
    };

    componentDidMount(){
      const { dispatch } = this.props;
      dispatch({
        type:'manager/fetch_role_list',
        payload:{
          start:0,
          count:10000
        }
      })
    }
  
    handleSubmit = () => {
        const { dispatch,form:{validateFields} } = this.props;
        validateFields((err, values) => {
          if (!err) {
            delete values.confirm;
            dispatch({
                type:'manager/fetch_manager_add',
                payload:{
                 ...values,
                },
              })
          }
        })
    }

    checkConfirm = (rule, value, callback) => {
      const form = this.props.form;
      if (value && value !== form.getFieldValue('password')) {
        callback('两次输入密码不一致!');
      } else {
        callback();
      }
    };
  
    checkPassword = (rule, value, callback) => {
      const form = this.props.form;
      if (value && this.state.confirmDirty) {
        form.validateFields(['confirm'], { force: true });
      }
      callback();
    };

    handleConfirmBlur = (e) => {
      const value = e.target.value;
      this.setState({ confirmDirty: this.state.confirmDirty || !!value });
    }

  render(){
    const { form:{ getFieldDecorator }, loading, manager:{ role_list } } = this.props;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="管理员名称"
                    >
                    {getFieldDecorator('name', {
                        rules: [{ required: true, message: '管理员名称不能为空' }],
                    })(
                        <Input placeholder="请填写管理员名称"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="管理员登录名"
                    >
                    {getFieldDecorator('loginName', {
                        rules: [{ required: true, message: '登录名不能为空' },{ pattern: /(?:^1[3456789]|^9[28])\d{9}$/, message: '请输入正确手机号' },],
                    })(
                        <Input placeholder="请填写手机号码"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="初始密码"
                    >
                    {getFieldDecorator('password', {
                        rules: [
                          { required: true, message: '初始密码不能为空' },
                        {
                            validator: this.checkPassword,
                        },
                        { pattern: /((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))(?!^.*[\u4E00-\u9FA5].*$)^\S{8,18}$/, message: '请输入8-16位的至少包含数字,字母,符号两种类型的密码' },
                        ],
                    })(
                        <Input type="password" placeholder="请输入至少8位，包含字母和数字的密码"/>
                    )}
                </FormItem>
                <FormItem
                  {...formProps}
                  label="确认密码"
                >
                        {getFieldDecorator('confirm', {
                        rules: [
                            {
                            required: true,
                            message: '请确认密码!',
                            },
                            { pattern: /((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))(?!^.*[\u4E00-\u9FA5].*$)^\S{8,18}$/, message: '请输入8-16位的至少包含数字,字母,符号两种类型的密码' },
                            {
                            validator: this.checkConfirm,
                            },
                        ],
                        })(<Input onBlur={this.handleConfirmBlur} type="password" placeholder="请确认密码" />)}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="选择角色"
                    >
                    {getFieldDecorator('roleIds', {
                        rules: [{ required: true, message: '角色不能为空' }],
                    })(
                        <Select style={{ width: '100%' }} mode="multiple" placeholder="请选择角色">
                          {
                            role_list.map(e=>{
                              return <Select.Option value={e.id} key={e.id}>{e.name}</Select.Option>
                            })
                          }
                        </Select>
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/manager/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
