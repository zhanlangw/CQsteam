import React, { PureComponent } from 'react';
import { Card, Spin, Form, Input, DatePicker, Select, Cascader, Row, Col, Button, Tooltip } from 'antd';
import styles from './style.less';
import Link from 'umi/link';
import { connect } from 'dva';
import moment from 'moment';
import { LineChart } from '@/components/Charts';
import { List } from 'immutable';
import { stringify } from 'qs';

const Option = Select.Option;
const formProps = {
  labelCol:{span: 4},
  wrapperCol:{ span: 18 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ statistics, loading }) => ({
  statistics,
  loading: loading.models.statistics,
}))
export default class Register extends PureComponent{
  state = {
    startValue: null,
    endValue: null,
  };

  componentDidMount(){
    const { dispatch, statistics:{ register_data_params:{ line1,line2 } } } = this.props;
    dispatch({
      type:'statistics/fetch_register_data',
      payload:{
        ...line1
      },
      params:{
        type:1
      }
    })
  }

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
          type:'statistics/fetch_register_data',
          payload:{
            startTime:moment(values.startTime).format('YYYY/MM/DD'),
            endTime:moment(values.endTime).format('YYYY/MM/DD'),
          },
          params:{
            type:1
          }
        })
      }
    })
  }

  render(){
    const { statistics:{ 
      register_data:{ line1 }, 
      register_data_params:{ line1:line1_params }, 
    },loading, form:{ getFieldDecorator } } = this.props;
    const chartData = line1.data.map((e,i)=>{
      return {
        x: e.createTime,
        y1: e.count,
      }
    })

    return (
        <Card bordered={false}>
            <Spin spinning={loading}>  
                <div>
                    <span className={styles.total}>
                        <p className={styles.total_title}>截止当前总量</p>
                        <div className={styles.total_num}>{line1.totalCount}</div>
                    </span>
                </div>
                <div style={{marginTop:24,width:600}}>
                  <Row gutter={16}>
                    <Col span={8}>
                      <FormItem
                        wrapperCol={{ span:  24}}
                        >
                        {getFieldDecorator('startTime', {
                            rules: [{ required: true, message: '开始时间不能为空' }],
                            initialValue:moment(line1_params.startTime)
                        })(
                            <DatePicker disabledDate={this.disabledStartDate} onChange={this.onStartChange} format='YYYY/MM/DD' placeholder="请选择开始时间"/>
                        )}
                    </FormItem>
                    </Col>
                    <Col span={8}>
                      <FormItem
                       wrapperCol={{ span:  24}}
                        >
                        {getFieldDecorator('endTime', {
                            rules: [{ required: true, message: '结束时间不能为空' }],
                            initialValue:moment(line1_params.endTime)
                        })(
                            <DatePicker disabledDate={this.disabledEndDate} onChange={this.onEndChange} format='YYYY/MM/DD' placeholder="请选择结束时间"/>
                        )}
                      </FormItem>
                    </Col>
                    <Col span={3}>
                      <Button loading={loading} onClick={this.handleSubmit} style={{marginTop:3}} type="primary">统计</Button>
                    </Col>
                    <Col span={3}>
                      <a href={`/api/stats/register/export?${stringify(line1_params)}`}><Button loading={loading} style={{marginTop:3}}>导出</Button></a>
                    </Col>
                  </Row>
                </div>
                <div>
                  <LineChart
                    height={300}
                    data={chartData}
                    titleMap={{ y1: '注册数量' }}
                  />
                </div>
            </Spin>
        </Card>
    )
  }
}
