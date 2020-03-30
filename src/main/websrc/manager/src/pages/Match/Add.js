import React, { Component } from 'react';
import { Spin, Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, message, Checkbox, Radio, DatePicker, InputNumber } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';
import moment from 'moment';

const CheckboxGroup = Checkbox.Group;
const RadioGroup = Radio.Group;
const formProps = {
    labelCol:{span: 8},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ match, global, loading }) => ({
  match,
  area:global.area,
  loading: loading.models.match,
}))
export default class Add extends Component{
    state = {
        checkedList:[],
        confirmDirty:false,
        checkAll:false,
        indeterminate:false,
    }

    componentDidMount(){
        const { dispatch } = this.props;
        dispatch({
            type:'global/fetch_area',
        })
    }

    onChange = checkedList =>{
        this.setState({
            checkedList
        })
    }
    
    checkAll = (e) => {
        const {form:{setFieldsValue},area} = this.props;
        const checkAll = e.target.checked;
        this.setState({
            indeterminate:false,
            checkAll:checkAll
        })
        if(checkAll){
            setFieldsValue({
                areaIds:area.map(e=>e.key)
            })
        }else{
            setFieldsValue({
                areaIds:undefined
            })
        }
    }

    areaIdsChange = (checkedList) => {
        const {form:{setFieldsValue},area} = this.props;
        if(checkedList.length === area.length){
            this.setState({
                indeterminate:false,
                checkAll:true
            })
        }else{
            this.setState({
                indeterminate:true,
                checkAll:false
            })
        }
        setFieldsValue({
            areaIds:checkedList
        })
    }

    checkMin = (rule, value, callback) => {
        const form = this.props.form;
        if (value && value < form.getFieldValue('minMember')) {
          callback('最大数量不能小于最小数量!');
        } else {
            if (value && this.state.confirmDirty) {
                form.validateFields(['minMember'], { force: true });
              }
              callback();
        }
      };
    
      checkMax = (rule, value, callback) => {
        const form = this.props.form;
        if (value && value > form.getFieldValue('maxMember')) {
            callback('最小数量不能大于最大数量!');
          } else {
            // if (value && this.state.confirmDirty) {
            //     form.validateFields(['maxMember'], { force: true });
            //   }
              callback();
          }
      };

      handleConfirmBlur = (e) => {
        const value = e.target.value;
        this.setState({ confirmDirty: this.state.confirmDirty || !!value });
      }

    handleSubmit = () => {
        const { dispatch,form:{validateFields} } = this.props;
        validateFields((err, values) => {
          if (!err) {
              delete values.match;
              values.signUpTime = moment(values.signUpTime).format('YYYY/MM/DD HH:mm:ss');
              values.stages.map(e=>{
                  e.endTime = moment(e.endTime).format('YYYY/MM/DD HH:mm:ss');
              })
            dispatch({
                type:'match/fetch_match_add',
                payload:{
                 ...values,
                },
              })
          }
        })
    }

    disabledEndDate = (current) => {
        const { form:{getFieldValue} } = this.props;
        const startTime = getFieldValue('signUpTime');
        return current && current < moment(startTime).endOf('day');
    }

  render(){
    const { form:{ getFieldDecorator }, loading, area } = this.props;
    const { checkedList, checkAll, indeterminate } = this.state;
    const matchs = checkedList.sort().map((e,i)=>{
        const type = e===1?'初赛':e===2?'复赛':'决赛';
        return (
            <div key={e}>
                <p  style={{textAlign:'center',color:'#005d7e'}}>
                    <span>
                        {type}
                    </span>
                </p>
                <FormItem
                    {...formProps}
                    label="材料截止时间"
                    >
                    {getFieldDecorator(`stages[${i}][endTime]`, {
                        rules: [{ required: true, message: '材料截止时间不能为空' }],
                    })(
                        <DatePicker showTime disabledDate={this.disabledEndDate}  style={{width:'100%'}} format="YYYY/MM/DD HH:mm:ss" placeholder="请选择材料截止时间"/>
                    )}
                </FormItem>
                <Row gutter={10}>
                    <Col span={8}>
                        <div style={{textAlign:'right',height:40,lineHeight:'40px',color:'rgba(0, 0, 0, 0.85)'}}>
                            <span className="ant-form-item-required">
                                材料数量配置(最多) :
                            </span>
                        </div>
                    </Col>
                    <Col span={8}>
                        <Row>
                            <Col span={12}>
                                <FormItem
                                    labelCol={{span: 7}}
                                    wrapperCol={{ span: 17 }}
                                    label="PPT"
                                    >
                                    {getFieldDecorator(`stages[${i}][pptNum]`, {
                                        rules: [{ required: true, message: '数量不能为空' }],
                                    })(
                                        <InputNumber min={0}/>
                                    )}
                                </FormItem>
                            </Col>
                            <Col span={12}>
                                <FormItem
                                    labelCol={{span: 7}}
                                    wrapperCol={{ span: 17 }}
                                    label="文件"
                                    >
                                    {getFieldDecorator(`stages[${i}][docNum]`, {
                                        rules: [{ required: true, message: '数量不能为空' }],
                                    })(
                                        <InputNumber min={0} />
                                    )}
                                </FormItem>
                            </Col>
                        </Row>
                        <Row>
                            <Col span={12}>
                                <FormItem
                                    labelCol={{span: 7}}
                                    wrapperCol={{ span: 17 }}
                                    label="视频"
                                    >
                                    {getFieldDecorator(`stages[${i}][videoNum]`, {
                                        rules: [{ required: true, message: '数量不能为空' }],
                                    })(
                                        <InputNumber min={0}/>
                                    )}
                                </FormItem>
                            </Col>
                            <Col span={12}>
                                <FormItem
                                    labelCol={{span: 7}}
                                    wrapperCol={{ span: 17 }}
                                    label="图片"
                                    >
                                    {getFieldDecorator(`stages[${i}][imageNum]`, {
                                        rules: [{ required: true, message: '数量不能为空' }],
                                    })(
                                        <InputNumber min={0} />
                                    )}
                                </FormItem>
                            </Col>
                        </Row>
                    </Col>
                </Row>
                <FormItem
                    labelCol={{span: 7}}
                    wrapperCol={{ span: 17 }}
                    >
                    {getFieldDecorator(`stages[${i}][type]`, {
                       initialValue:e
                    })(
                        <Input type="hidden"/>
                    )}
                </FormItem>
            </div>
        )
    })
    return (
      <Card bordered={false}>
        <Spin spinning={loading}>
            <Card>
                <FormItem
                    {...formProps}
                    label="类别名称"
                    >
                    {getFieldDecorator('name', {
                        rules: [{ required: true, message: '类别名称不能为空' }],
                    })(
                        <Input placeholder="请填写类别名称"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="简称"
                    >
                    {getFieldDecorator('abbreviation', {
                        rules: [{ required: true, message: '简称不能为空' }],
                    })(
                        <Input placeholder="请填写简称"/>
                    )}
                </FormItem>
                <Row gutter={10}>
                    <Col span={8}>
                        <div style={{textAlign:'right',height:40,lineHeight:'40px',color:'rgba(0, 0, 0, 0.85)'}}>
                            <span className="ant-form-item-required">
                                成员数量配置 :
                            </span>
                        </div>
                    </Col>
                    <Col span={4}>
                        <FormItem
                            labelCol={{span: 7}}
                            wrapperCol={{ span: 17 }}
                            label="最少"
                            >
                            {getFieldDecorator('minMember', {
                                rules: [{ required: true, message: '最少数量不能为空' },{
                                    validator: this.checkMax,
                                }],
                            })(
                                <InputNumber onBlur={this.handleConfirmBlur} min={1}/>
                            )}
                        </FormItem>
                    </Col>
                    <Col span={4}>
                        <FormItem
                            labelCol={{span: 7}}
                            wrapperCol={{ span: 17 }}
                            label="最多"
                            >
                            {getFieldDecorator('maxMember', {
                                rules: [{ required: true, message: '最多数量不能为空' },{
                                    validator: this.checkMin,
                                }],
                            })(
                                <InputNumber onBlur={this.handleConfirmBlur} min={1} />
                            )}
                        </FormItem>
                    </Col>
                </Row>
                <FormItem
                    {...formProps}
                    label="组别配置"
                    >
                    {getFieldDecorator('group', {
                        rules: [{ required: true, message: '组别配置不能为空' }],
                    })(
                        <Checkbox.Group style={{ width: "100%", marginTop:9 }}>
                            <Row>
                                <Col span={6}><Checkbox value={1}>小学</Checkbox></Col>
                                <Col span={6}><Checkbox value={2}>初中</Checkbox></Col>
                                <Col span={6}><Checkbox value={3}>高中</Checkbox></Col>
                                <Col span={6}><Checkbox value={4}>中学</Checkbox></Col>
                            </Row>
                        </Checkbox.Group>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="省份配置"
                    >
                    <div style={{
                                    height: 200,
                                    overflow: 'auto',
                                    paddingLeft: 48,
                                    border: '1px solid #eee',
                                    borderRadius:5
                            }}>
                            <Row style={{marginLeft:-24}}>
                                <Col span={24}><Checkbox checked={checkAll} indeterminate={indeterminate} onChange={this.checkAll} value='*'>全选</Checkbox></Col>
                            </Row>
                            {getFieldDecorator('areaIds', {
                                rules: [{ required: true, message: '省份配置不能为空' }],
                            })(
                                <Checkbox.Group onChange={this.areaIdsChange} style={{ width: "100%", marginTop:-6 }}>
                                    <Row>
                                        
                                        {
                                            area.map(e=><Col style={{marginTop:8}} key={e.key} span={24}><Checkbox value={e.key}>{e.title}</Checkbox></Col>)
                                        }
                                    </Row>
                                </Checkbox.Group>
                            )}
                    </div>
                </FormItem>
                <FormItem
                    labelCol={{span: 8}}
                    wrapperCol={{ span: 16 }}
                    label="选择需要配置的选项"
                    >
                    {getFieldDecorator('paramSetting', {
                        rules: [{ required: true, message: '选项配置不能为空' }],
                    })(
                        <Checkbox.Group style={{ width: "100%", marginTop:9 }}>
                            <Row>
                                <Col span={24}>
                                    <Checkbox value={1}>是否需要选择成员身份</Checkbox>
                                    <Checkbox value={2}>是否需要选择教授学科</Checkbox>
                                    <Checkbox value={3}>是否需要填写指导老师</Checkbox>
                                </Col>
                                <Col span={24} style={{marginTop:12}}> 
                                    <Checkbox value={4}>是否需要填写项目名称，项目简介</Checkbox>
                                    <Checkbox value={5}>是否需要填写护照号码、父母姓名、父母手机号、地址</Checkbox>
                                </Col>
                            </Row>
                        </Checkbox.Group>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="手机号"
                    >
                    {getFieldDecorator('telephoneType', {
                        rules: [{ required: true, message: '手机号选择不能为空' }],
                    })(
                        <Radio.Group style={{ width: "100%"}}>
                            <Radio value={1}>学生家长手机号</Radio>
                            <Radio value={2}>手机号</Radio>
                        </Radio.Group>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="报名开始时间"
                    >
                    {getFieldDecorator('signUpTime', {
                        rules: [{ required: true, message: '报名开始时间不能为空' }],
                    })(
                        <DatePicker showTime style={{width:'100%'}} format="YYYY/MM/DD HH:mm:ss" placeholder="请选择报名开始时间"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="比赛阶段"
                    >
                    {getFieldDecorator('match', {
                        rules: [{ required: true, message: '比赛阶段不能为空' }],
                    })(
                        <Checkbox.Group onChange={this.onChange} style={{ width: "100%", marginTop:9 }}>
                            <Row>
                                <Col span={6}><Checkbox value={1}>初赛</Checkbox></Col>
                                <Col span={6}><Checkbox value={2}>复赛</Checkbox></Col>
                                <Col span={6}><Checkbox value={3}>决赛</Checkbox></Col>
                            </Row>
                        </Checkbox.Group>
                    )}
                </FormItem>
                {matchs}
                <div style={{textAlign:'center'}}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/match/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Spin>
     </Card>
    )
  }
}
