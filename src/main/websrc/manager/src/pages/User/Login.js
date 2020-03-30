import React, { Component } from 'react';
import { connect } from 'dva';
import Link from 'umi/link';
import { Checkbox, Alert, Icon } from 'antd';
import Login from '@/components/Login';
import styles from './Login.less';

const { Tab, UserName, Password, Mobile, Captcha, Submit } = Login;

@connect(({ login, loading }) => ({
  login,
  submitting: loading.effects['login/login'],
}))
class LoginPage extends Component {
  handleSubmit = (err, values) => {
    if (!err) {
      const { dispatch } = this.props;
      dispatch({
        type: 'login/login',
        payload: {
          ...values,
        },
      });
    }
  };

  changeAutoLogin = (e) => {
    const autoLogin = e.target.checked;
    this.props.dispatch({
        type: 'login/changeAutoLogin',
        payload: {
          autoLogin:autoLogin,
        },
    });
  }

  render() {
    const { login:{ autoLogin }, submitting } = this.props;
    const userInfo = localStorage.getItem('user') && JSON.parse(localStorage.getItem('user'));
    const username = userInfo && userInfo.username;
    const password = userInfo && userInfo.password;
    return (
      <div className={styles.main}>
        <Login
          onSubmit={this.handleSubmit}
          ref={form => {
            this.loginForm = form;
          }}
        >
            <UserName
              name="username"
              defaultValue={username} 
              placeholder="请输入账号"
              rules={[
                {
                  required: true,
                  message: '账号不能为空',
                },
              ]}
            />
            <Password
              name="password"
              defaultValue={password} 
              placeholder='请输入密码'
              rules={[
                {
                  required: true,
                  message:'密码不能为空',
                },
              ]}
              onPressEnter={e => {
                e.preventDefault();
                this.loginForm.validateFields(this.handleSubmit);
              }}
            />
          <div>
            <Checkbox defaultChecked={autoLogin} onChange={this.changeAutoLogin}>
              记住密码
            </Checkbox>
            {/* <a style={{ float: 'right' }} href="">
              忘记密码？
            </a> */}
          </div>
          <Submit loading={submitting}>
            登录
          </Submit>
        </Login>
      </div>
    );
  }
}

export default LoginPage;
