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
@connect(({ abstract, loading }) => ({
  abstract,
  loading: loading.models.abstract,
}))
export default class Edit extends PureComponent{
    componentDidMount(){
        const { dispatch, form:{setFieldsValue} } = this.props;
        dispatch({
          type:'abstract/fetch_introduction_item',
          callback:(data)=>{
            setFieldsValue({
                content: BraftEditor.createEditorState(data.content)
            })
          }
        })
    }

    handleSubmit = () => {
        const { dispatch,form:{validateFields}, match:{params:{id}} } = this.props;
        validateFields((err, values) => {
          if (!err) {
            dispatch({
                type:'abstract/fetch_introduction_upd',
                payload:{
                 ...values,
                 content: values.content.toHTML(),
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
    const { form:{ getFieldDecorator }, loading, abstract:{ introduction_item } } = this.props;
    const { title, content } = introduction_item;
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
                    labelCol={{span: 4}}
                    wrapperCol={{span: 17}}
                    label="内容"
                    >
                    {getFieldDecorator('content', {
                        validateTrigger: 'onBlur',
                        rules: [{
                            required: true,
                            validator: (_, value, callback) => {
                            if (value.isEmpty()) {
                                callback('请输入内容')
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
                            placeholder="请输入内容"
                        />
                        )}
                </FormItem>
                <div style={{textAlign:'center'}}>
                    <Button onClick={this.handleSubmit} type="primary">确定</Button>
                    <Button onClick={()=>{router.push('/abstract')}} style={{marginLeft:48}}>取消</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
