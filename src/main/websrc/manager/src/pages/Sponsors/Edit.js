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
@connect(({ sponsors, loading }) => ({
  sponsors,
  loading: loading.models.sponsors,
}))
export default class Edit extends PureComponent{
  componentDidMount(){
    const { dispatch, match:{params:{id}}, form:{setFieldsValue} } = this.props;
    dispatch({
      type:'sponsors/fetch_sponsors_item',
      payload:{
       id:id
      },
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
        const { dispatch,form:{validateFields}, match:{params:{id}} } = this.props;
        validateFields((err, values) => {
          if (!err) {
            dispatch({
                type:'sponsors/fetch_sponsors_upd',
                payload:{
                 ...values,
                 path: values.path[0].response.value,
                 id:id
                },
              })
          }
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, match:{params:{id}}, sponsors:{ sponsors_item }, } = this.props;
    const item = sponsors_item[id] || {};
    const { url, path } = item;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="赞助商URL"
                    >
                    {getFieldDecorator('url', {
                        sponsors: [{ required: true, message: '赞助商URL不能为空' }],
                        initialValue:url
                    })(
                        <Input placeholder="请填写赞助商URL"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="图片"
                    >
                    {getFieldDecorator('path', {
                        sponsors: [{ required: true, message: '请上传图片' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                        initialValue: path && [{
                          uid: '-1',
                          name: path.split('/')[path.split('/').length-1],
                          status: 'done',
                          response:{
                              value:path
                          },
                          url:path
                        }] || undefined,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            listType="picture-card"
                            action={`/api/file/upload?fileType=5`}
                            accept=".jpg,.jpeg,.png"
                            beforeUpload={(e)=>this.beforeUpload(e,'path')}
                            onRemove={this.fileMove}
                        >
                            <Button size="small" type="primary">
                                上传
                            </Button>
                        </Upload>         
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/sponsors/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
