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
export default class Edit extends PureComponent{
  componentDidMount(){
    const { dispatch, match:{params:{id}}, form:{setFieldsValue} } = this.props;
    dispatch({
      type:'videos/fetch_videos_item',
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
                type:'videos/fetch_videos_upd',
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
    const { form:{ getFieldDecorator }, loading, match:{params:{id}}, videos:{ videos_item }, } = this.props;
    const item = videos_item[id] || {};
    const { title, path } = item;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="视频标题"
                    >
                    {getFieldDecorator('title', {
                        videos: [{ required: true, message: '视频标题不能为空' }],
                        initialValue:title
                    })(
                        <Input placeholder="请填写视频标题"/>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="视频文件"
                    >
                    {getFieldDecorator('path', {
                        videos: [{ required: true, message: '请上传视频文件' }],
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
