import React, { PureComponent } from 'react';
import { Card, Row, Col, Form, Input, Upload, Button, Modal, message, Spin } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';

const formProps = {
  labelCol:{span: 6},
  wrapperCol:{ span: 12 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ advert, loading }) => ({
  advert,
  loading: loading.models.advert,
}))
export default class Advert extends PureComponent{
  state = {
    visible:false,
    data:{},
    isEdit:false
  }

  closeModal = () => {
    const { form:{resetFields} } = this.props;
    resetFields();
    this.setState({
      visible:false
    })
  }

  openModal = () => {
    this.setState({
      visible:true
    })
  }

  componentDidMount(){
    const { dispatch } = this.props;
    dispatch({
      type:'advert/fetch_advert_list',
    })
  }

  normFile = (e) => {
    if (Array.isArray(e)) {
      return e;
    }
    if (e.file.status) {
      return e && e.fileList;
    }else{
      return e && e.fileList && e.fileList.filter(el=>{
        if(el.response){
          return true;
        }else{
          return false;
        }
      });
    }
  };

  beforeUpload = (file,name) => {
      const { form: {getFieldValue} } = this.props;
      const value = getFieldValue(name);
      if(value && value.length===1){
        message.warning(`只能上传1个文件！`);
        return false;
      }
  }

  fileMove = (file) => {
    const { dispatch } = this.props;
    if(file.response){
      dispatch({
        type:'global/fetch_file_del',
        payload:{
        path:file.response.value
        },
      })
    }
    return true;
  }

  handleOk = () => {
    const { dispatch,form:{validateFields} } = this.props;
    const { isEdit } = this.state;
    const self = this;
    validateFields((err, values) => {
      if (!err) {
        values.imagePath = values.imagePath[0].response.value;
        if(isEdit){
          dispatch({
              type:'advert/fetch_advert_upd',
              payload:{
                ...values,
              },
              callback:()=>{
                self.closeModal()
              }
            })
        }else{
          delete values.id;
          dispatch({
              type:'advert/fetch_advert_add',
              payload:{
              ...values,
              },
              callback:()=>{
                self.closeModal()
              }
            })
        }
      }
    })
  }

  delAdvert = (id) => {
    const { dispatch } = this.props;
    Modal.confirm({
      title: '提示?',
      content: '确认删除吗？',
      okText:'确定',
      cancelText:'取消',
      onOk() {
        dispatch({
          type:'advert/fetch_advert_del',
          payload:{
            id:id
          }
        })
      },
    });
  }

  addAdvert = () => {
    this.setState({
      isEdit:false,
      data:{},
      visible:true
    })
  }

  editAdvert = (data) => {
    this.setState({
      isEdit:true,
      data:data,
      visible:true
    })
  }

  render(){
    const { form:{ getFieldDecorator }, loading, advert:{ advert_list, advert_item } } = this.props;
    const { visible, data, isEdit } = this.state;
    const advert_type1 = advert_list.filter(e=>e.type===1);
    const advert_type2 = advert_list.filter(e=>e.type===2);
    const advert_type3 = advert_list.filter(e=>e.type===3);
    return (
        <Card bordered={false}>
        <Spin spinning={loading}>
            <Modal
              title={isEdit?"修改广告":"新建广告"}
              visible={visible}
              onOk={this.handleOk}
              confirmLoading={loading}
              onCancel={this.closeModal}
            >
              <FormItem
                {...formProps}
                label="外链地址"  
              >
                {getFieldDecorator('url', {
                    // rules: [{ required: true, message: '外链地址不能为空' }],
                    initialValue:data.url
                })(
                  <Input placeholder="请输入外链地址"/>
                )}
              </FormItem>
              <FormItem
                    {...formProps}
                    label="图片"
                    >
                    {getFieldDecorator('imagePath', {
                        rules: [{ required: true, message: '请上传图片' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                        initialValue:data.imagePath && [{
                          uid: '-1',
                          name: data.imagePath.split('/')[data.imagePath.split('/').length-1],
                          status: 'done',
                          response:{
                              value:data.imagePath
                          }
                        }] || undefined,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            listType="picture-card"
                            action={`/api/file/upload?fileType=5`}
                            accept=".jpg,.jpeg,.png"
                            beforeUpload={(e)=>this.beforeUpload(e,'imagePath')}
                            onRemove={this.fileMove}
                        >
                            <Button size="small" type="primary">
                                上传
                            </Button>
                        </Upload>         
                    )}
                </FormItem>
                <FormItem
                  {...formProps}
                >
                  {getFieldDecorator('id', {
                      initialValue:data.id
                  })(
                    <Input type="hidden"/>
                  )}
                </FormItem>
            </Modal>
            <Row gutter={16} style={{borderBottom:'1px solid #d9d9d9'}}>
              <Col span={12}>
                <div style={{textAlign:'center',padding:24,color:'#005d7e',fontWeight:600}}>
                  项目基本信息
                </div>
                <Row gutter={16} style={{marginBottom:24}}>
                    <Col span={6}>
                      <div style={{textAlign:'right',padding:'6px 12px'}}>
                          外链地址 :
                      </div>
                    </Col>
                    <Col span={12}>
                    {
                      advert_type1[0] && advert_type1[0].url && <div style={{padding:'6px 12px',overflow:'auto',border:'1px solid #d9d9d9',height:120,background:'#f4f4f4',borderRadius:4,wordBreak: 'break-all'}}>
                                                                {advert_type1[0] && advert_type1[0].url}
                                                              </div>
                    }
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={6}>
                      <div style={{textAlign:'right',padding:'6px 12px'}}>
                          图片 :
                      </div>
                    </Col>
                    <Col span={12}>
                      <div>
                        <img style={{width:'100%'}} src={advert_type1[0] && advert_type1[0].imagePath} alt=""/>
                      </div>
                    </Col>
                </Row>
                <div style={{textAlign:'center',padding:24}}>
                  <Button onClick={()=>this.editAdvert(advert_type1[0])} type="primary">修改</Button>
                </div>
              </Col>
              <Col span={12}>
                <div style={{textAlign:'center',padding:24,color:'#005d7e',fontWeight:600}}>
                  提交项目状态
                </div>
                <Row gutter={16} style={{marginBottom:24}}>
                    <Col span={6}>
                      <div style={{textAlign:'right',padding:'6px 12px'}}>
                          外链地址 :
                      </div>
                    </Col>
                    <Col span={12}>
                    {
                      advert_type2[0] && advert_type2[0].url && <div style={{padding:'6px 12px',height:120,overflow:'auto',border:'1px solid #d9d9d9',background:'#f4f4f4',borderRadius:4,wordBreak: 'break-all'}}>
                                                                {advert_type2[0] && advert_type2[0].url}
                                                              </div>
                    }
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={6}>
                      <div style={{textAlign:'right',padding:'6px 12px'}}>
                          图片 :
                      </div>
                    </Col>
                    <Col span={12}>
                      <div>
                        <img style={{width:'100%'}} src={advert_type2[0] && advert_type2[0].imagePath} alt=""/>
                      </div>
                    </Col>
                </Row>
                <div style={{textAlign:'center',padding:24}}>
                  <Button onClick={()=>this.editAdvert(advert_type2[0])} type="primary">修改</Button>
                </div>
              </Col>
            </Row>
            <div style={{textAlign:'center',padding:24,color:'#005d7e',fontWeight:600}}>
                  首页广告
            </div>
            <div style={{padding:24}}>
                <Button onClick={this.addAdvert} type="primary">新建广告</Button>
            </div>
            <Row gutter={16}>
              {
                advert_type3.map(e=>{
                  return (
                      <Col key={e.id} span={6}>
                        <div>
                          <img style={{width:'100%'}} src={e.imagePath} alt=""/>
                        </div>
                        <div style={{textAlign:'center',padding:24}}>
                          <Button onClick={()=>this.delAdvert(e.id)}>删除</Button>
                          <Button style={{marginLeft:24}} onClick={()=>this.editAdvert(e)} type="primary">修改</Button>
                        </div>
                      </Col>
                  )
                })
              }
            </Row>
            </Spin>
        </Card>
    )
  }
}
