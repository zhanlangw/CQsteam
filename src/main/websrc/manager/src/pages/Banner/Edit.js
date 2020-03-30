import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Select, Tooltip, Modal, Form, Input, Col, Row, Upload, message, InputNumber } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

const formProps = {
    labelCol:{span: 4},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ banner, loading }) => ({
  banner,
  loading: loading.models.banner,
}))
export default class Edit extends PureComponent{
    state = {
        type:1
    }

    componentDidMount(){
      const { dispatch,match:{params:{id}} } = this.props;
      dispatch({
        type:'banner/fetch_banner_item',
        payload:{
          id:id
        },
      })
    }

    onChange = (type) => {
        this.setState({
            type
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

    handleSubmit = () => {
        const { dispatch,form:{validateFields},match:{params:{id}} } = this.props;
        const { type } = this.state;
        validateFields((err, values) => {
          if (!err) {
            dispatch({
                type:'banner/fetch_banner_upd',
                payload:{
                 ...values,
                 smallImagePath: values.smallImagePath[0].response.value,
                 bigImagePath: values.bigImagePath[0].response.value,
                 type:type,
                 id:id
                },
              })
          }
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, banner:{ banner_item }, match:{params:{id}} } = this.props;
    const item = banner_item[id] || {};
    const { title, number, bigImagePath, smallImagePath, url, type } = item;
    const addonBefore = <Select onChange={this.onChange} defaultValue={type || 1} style={{ width: 80 }}>
                            <Select.Option value={1}>内链</Select.Option>
                            <Select.Option value={2}>外链</Select.Option>
                        </Select>
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="标题"
                    >
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: '标题不能为空' }],
                        initialValue:title
                    })(
                        <Input placeholder="请填写标题"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="位置编号"
                    >
                    {getFieldDecorator('number', {
                        rules: [{ required: true, message: '位置编号不能为空' }],
                        initialValue:number
                    })(
                        <InputNumber style={{width:'100%'}} placeholder="请填写位置编号"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="大图"
                    >
                    {getFieldDecorator('bigImagePath', {
                        rules: [{ required: true, message: '请上传大图' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                        initialValue:bigImagePath && [{
                          uid: '-1',
                          name: bigImagePath.split('/')[bigImagePath.split('/').length-1],
                          status: 'done',
                          response:{
                              value:bigImagePath
                          }
                        }] || undefined,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            listType="picture-card"
                            action={`/api/file/upload?fileType=5`}
                            accept=".jpg,.jpeg,.png"
                            beforeUpload={(e)=>this.beforeUpload(e,'bigImagePath')}
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
                    label="小图"
                    >
                    {getFieldDecorator('smallImagePath', {
                        rules: [{ required: true, message: '请上传小图' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                        initialValue:smallImagePath && [{
                          uid: '-1',
                          name: smallImagePath.split('/')[smallImagePath.split('/').length-1],
                          status: 'done',
                          response:{
                              value:smallImagePath
                          }
                        }] || undefined,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            listType="picture-card"
                            action={`/api/file/upload?fileType=5`}
                            accept=".jpg,.jpeg,.png"
                            beforeUpload={(e)=>this.beforeUpload(e,'smallImagePath')}
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
                    label="跳转地址"
                    >
                    {getFieldDecorator('url', {
                        initialValue:url
                    })(
                        <Input style={{marginTop:5}} placeholder="请填写跳转地址" addonBefore={addonBefore}/>
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/banner/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
