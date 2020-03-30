import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, Upload, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

@Form.create()
@connect(({ attention, loading }) => ({
  attention,
  loading: loading.models.attention,
}))
export default class Attention extends PureComponent{
    componentDidMount(){
        const { dispatch } = this.props;
        dispatch({
          type:'attention/fetch_attention_item',
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, attention:{ attention_item } } = this.props;
    const { title, content, path } = attention_item;
    return (
      <Card bordered={false}>
            <Card>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        标题 :
                    </Col>
                    <Col span={8}>
                        <div style={{border:'1px solid #e8e8e8',padding:5,borderRadius:4,background:'#f4f4f4'}}>
                            {title} 
                        </div>
                    </Col>
                </Row>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        附件 :
                    </Col>
                    <Col span={8} style={{padding:5}}>
                        <a href={path}>{path && (path.split('/')[path.split('/').length-1]) || ''}</a>
                    </Col>
                </Row>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        内容 :
                    </Col>
                    <Col span={16}>
                        <div style={{border:'1px solid #e8e8e8',padding:5,borderRadius:4,background:'#f4f4f4'}}>
                            <div className="content" dangerouslySetInnerHTML={{__html:content}}/>
                        </div>
                    </Col>
                </Row>
                <div style={{textAlign:'center'}}>
                    <Button type="primary" onClick={()=>{router.push('/attention/edit')}}>修改</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
