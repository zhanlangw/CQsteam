import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, Upload, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

const formProps = {
    labelCol:{span: 8},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ result, loading }) => ({
  result,
  loading: loading.models.result,
}))
export default class Add extends PureComponent{
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
        const { dispatch,form:{validateFields} } = this.props;
        validateFields((err, values) => {
          if (!err) {
            dispatch({
                type:'result/fetch_result_add',
                payload:{
                 ...values,
                 path: values.path[0].response.value,
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
                    label="结果标题"
                    >
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: '标题不能为空' }],
                    })(
                        <Input placeholder="请填写标题"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="附件"
                    >
                    {getFieldDecorator('path', {
                        rules: [{ required: true, message: '请上传附件' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            action={`/api/file/upload?fileType=5`}
                            beforeUpload={(e)=>this.beforeUpload(e,'path')}
                            onRemove={this.fileMove}
                            accept=".doc,.docx,.pdf"
                        >
                            <Button size="small" type="primary">
                                上传
                            </Button>
                        </Upload>         
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/result/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
