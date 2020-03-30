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
@connect(({ users, loading }) => ({
  users,
  loading: loading.models.users,
}))
export default class Add extends PureComponent{
    state = {
      confirmDirty:false,
    };

    handleSubmit = () => {
        const { dispatch,form:{validateFields}, match:{params:{id}}  } = this.props;
        validateFields((err, values) => {
          if (!err) {
            delete values.confirm;
            dispatch({
                type:'users/fetch_password_reset',
                payload:{
                 ...values,
                 id:id
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
    const { form:{ getFieldDecorator }, loading } = this.props;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="新密码"
                    >
                    {getFieldDecorator('password', {
                        rules: [
                          { required: true, message: '新密码不能为空' },
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
                <div style={{textAlign:'center'}}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/users/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
