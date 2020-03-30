import React, { PureComponent } from 'react';
import { Card, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, Upload, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';
import BraftEditor from 'braft-editor';
import 'braft-editor/dist/index.css';
import 'braft-extensions/dist/table.css';
import Table from 'braft-extensions/dist/table';
import { ContentUtils } from 'braft-utils';

BraftEditor.use(Table({
  defaultColumns: 5,
  defaultRows: 3
}))

const formProps = {
    labelCol:{span: 4},
    wrapperCol:{ span: 8 },
};
const FormItem = Form.Item;

@Form.create()
@connect(({ rules, loading }) => ({
  rules,
  loading: loading.models.rules,
}))
export default class Edit extends PureComponent{
    componentDidMount(){
        const { dispatch, match:{params:{id}}, form:{setFieldsValue} } = this.props;
        dispatch({
          type:'rules/fetch_rule_item',
          payload:{
           id:id
          },
          callback:(data)=>{
            setFieldsValue({
                content: BraftEditor.createEditorState(data.content)
            })
          }
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
                type:'rules/fetch_rule_upd',
                payload:{
                 ...values,
                 content: values.content.toHTML(),
                 imagePath: values.imagePath[0].response.value,
                 path: values.path[0].response.value,
                 id:id
                },
              })
          }
        })
    }

    uploadHandler = (param) => {
        const { dispatch,form:{getFieldValue,setFieldsValue} } = this.props;
        if (!param.file) {
          return false
        }
        const data = new FormData();
        let editorState = getFieldValue('content');
        data.append('file',param.file);
        dispatch({
            type:'global/fetch_file_upload',
            payload:data,
            params:{
                fileType:5
            },
            callback:(url)=>{
                editorState = ContentUtils.insertMedias(editorState, [{
                    type: 'IMAGE',
                    url: url
                }])
                setFieldsValue({content:editorState});
            }
          })
      }
      
  render(){
    const extendControls = [
        {
          key: 'antd-uploader',
          type: 'component',
          component: (
            <Upload
              accept="image/*"
              showUploadList={false}
              customRequest={this.uploadHandler}
            >
              {/* 这里的按钮最好加上type="button"，以避免在表单容器中触发表单提交，用Antd的Button组件则无需如此 */}
              <button type="button" className="control-item button upload-button" data-title="插入图片">
                <Icon type="picture" theme="filled" />
              </button>
            </Upload>
          )
        }
      ]
      
    const { form:{ getFieldDecorator }, loading, rules:{ rule_item }, match:{params:{id}} } = this.props;
    const item = rule_item[id] || {};
    const { title, content, path, imagePath, number } = item;
    return (
      <Card bordered={false}>
            <Card>
                <FormItem
                    {...formProps}
                    label="规则标题"
                    >
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: '规则标题不能为空' }],
                        initialValue:title
                    })(
                        <Input placeholder="请填写规则标题"/>
                    )}
                </FormItem>
                <FormItem
                    labelCol={{span: 4}}
                    wrapperCol={{span: 17}}
                    label="规则内容"
                    >
                    {getFieldDecorator('content', {
                        validateTrigger: 'onBlur',
                        rules: [{
                            required: true,
                            validator: (_, value, callback) => {
                            if (value.isEmpty()) {
                                callback('请输入规则内容')
                            } else {
                                callback()
                            }
                            }
                        }],
                        })(
                        <BraftEditor
                            contentStyle={{height:500,border:'1px solid #e8e8e8'}}
                            controls = {[
                              'undo', 'redo', 'separator',
                              'font-size', 'line-height', 'letter-spacing', 'separator',
                              'text-color', 'bold', 'italic', 'underline', 'strike-through', 'separator',
                              'superscript', 'subscript', 'remove-styles', 'emoji',  'separator', 'text-indent', 'text-align', 'separator',
                              'headings', 'list-ul', 'list-ol', 'blockquote', 'code', 'separator',
                              'link', 'separator', 'hr', 'clear','separator',
                              'table'
                          ]}
                            extendControls={extendControls}
                            media={{
                                accepts:{
                                    video:false,
                                    audio:false,
                                },
                                externals:{
                                    video:false,
                                    audio:false,
                                    embed:false
                                }
                            }}
                            placeholder="请输入规则内容"
                        />
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
                        <Input placeholder="请填写位置编号"/>
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
                        initialValue: imagePath && [{
                            uid: '-1',
                            name: imagePath.split('/')[imagePath.split('/').length-1],
                            status: 'done',
                            response:{
                                value:imagePath
                            },
                            url:imagePath
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
                    label="附件"
                    >
                    {getFieldDecorator('path', {
                        rules: [{ required: true, message: '请上传附件' }],
                        valuePropName: 'fileList',
                        getValueFromEvent:this.normFile,
                        initialValue:path && [{
                            uid: '-1',
                            name: path.split('/')[path.split('/').length-1],
                            status: 'done',
                            response:{
                                value:path
                            }
                          }] || undefined,
                    })(
                        <Upload                   
                            name="file"
                            showUploadList={{showPreviewIcon:false}}
                            action={`/api/file/upload?fileType=5`}
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
                    <Button onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/rules/list')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
