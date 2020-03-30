import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, DatePicker, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import moment from 'moment';
import router from 'umi/router';

const formProps = {
    labelCol:{span: 8},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ activity, loading }) => ({
  activity,
  loading: loading.models.activity,
}))
export default class Add extends PureComponent{
  state = {
    startValue: null,
    endValue: null,
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
        const { dispatch,form:{validateFields} } = this.props;
        validateFields((err, values) => {
          if (!err) {
            dispatch({
                type:'activity/fetch_activity_add',
                payload:{
                 ...values,
                 startTime: moment(values.startTime).format("YYYY/MM/DD HH:mm:ss"),
                 endTime: moment(values.endTime).format("YYYY/MM/DD HH:mm:ss"),
                },
              })
          }
        })
    }

  
  render(){
    const { form:{ getFieldDecorator }, loading } = this.props;
    const { startValue, endValue } = this.state;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="事件标题"
                    >
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: '事件标题不能为空' }],
                    })(
                        <Input placeholder="请填写事件标题"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="开始时间"
                    >
                    {getFieldDecorator('startTime', {
                        rules: [{ required: true, message: '开始时间不能为空' }],
                    })(
                        <DatePicker style={{width:'100%'}} disabledDate={this.disabledStartDate} onChange={this.onStartChange} showTime format="YYYY/MM/DD HH:mm:ss" placeholder="请选择开始时间"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="截止时间"
                    >
                    {getFieldDecorator('endTime', {
                        rules: [{ required: true, message: '截止时间不能为空' }],
                    })(
                        <DatePicker style={{width:'100%'}} disabledDate={this.disabledEndDate} onChange={this.onEndChange} showTime format="YYYY/MM/DD HH:mm:ss" placeholder="请选择截止时间"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="阶段"
                    >
                    {getFieldDecorator('stage', {
                        rules: [{ required: true, message: '阶段不能为空' }],
                    })(
                        <Input placeholder="请填写阶段"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="地址"
                    >
                    {getFieldDecorator('address', {
                        rules: [{ required: true, message: '地址不能为空' }],
                    })(
                        <Input placeholder="请填写地址"/>
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/activity/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
