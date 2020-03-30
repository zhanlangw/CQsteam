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
            dispatch({
                type:'result/fetch_result_upd',
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
    const { form:{ getFieldDecorator }, loading, result:{ result_item }, match:{params:{id}} } = this.props;
    const item = result_item[id] || {};
    const { title, path, fileName } = item;
    return (
      <Card bordered={false}>
            <Card>
                <Row gutter={12} style={{marginBottom:24}}>
                    <Col span={10}>
                        <div style={{textAlign:'right'}}>
                            结果标题 :
                        </div>
                    </Col>
                    <Col span={8}>
                        {title}
                    </Col>
                </Row>
                <Row gutter={12} style={{marginBottom:24}}>
                    <Col span={10}>
                        <div style={{textAlign:'right'}}>
                            附件 :
                        </div>
                    </Col>
                    <Col span={8}>
                        <a href={path}>{fileName}</a>
                    </Col>
                </Row>
                <div style={{textAlign:'center'}}>
                    <Button onClick={()=>{router.push('/result/list')}}>返回</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
