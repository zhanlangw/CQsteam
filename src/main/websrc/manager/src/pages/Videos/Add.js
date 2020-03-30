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
@connect(({ videos, loading }) => ({
  videos,
  loading: loading.models.videos,
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
                type:'videos/fetch_videos_add',
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
                    label="视频标题"
                    >
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: '视频标题不能为空' }],
                    })(
                        <Input placeholder="请填写视频标题"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="视频文件"
                    >
                    {getFieldDecorator('path', {
                        rules: [{ required: true, message: '请上传视频文件' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            action={`/api/file/upload?fileType=5`}
                            accept=".mp4"
                            beforeUpload={(e)=>this.beforeUpload(e,'path')}
                            onRemove={this.fileMove}
                        >
                            <Button size="small" type="primary">
                                上传
                            </Button>
                            <span style={{marginLeft:24}}>支持视频格式为MP4</span>
                        </Upload>         
                    )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button loading={loading} onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/videos/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
