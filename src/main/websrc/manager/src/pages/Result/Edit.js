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
export default class Edit extends PureComponent{
  componentDidMount(){
    const { dispatch, match:{params:{id}} } = this.props;
    dispatch({
      type:'result/fetch_result_item',
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
            let params = {
              ...values,
              id:id
            };
            if(values.path && values.path[0]){
              params.path = values.path[0].response.value;
            }
            dispatch({
                type:'result/fetch_result_upd',
                payload:{
                 ...params,
                },
              })
          }
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, result:{ result_item }, match:{params:{id}} } = this.props;
    const item = result_item[id] || {};
    const { title, path } = item;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="结果标题"
                    >
                    {getFieldDecorator('title', {
                        result: [{ required: true, message: '标题不能为空' }],
                        initialValue:title
                    })(
                        <Input placeholder="请填写标题"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="附件"
                    >
                    {getFieldDecorator('path', {
                        result: [{ required: true, message: '请上传附件' }],
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
