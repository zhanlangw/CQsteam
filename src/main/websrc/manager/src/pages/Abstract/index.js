import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, Upload, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

@Form.create()
@connect(({ abstract, loading }) => ({
  abstract,
  loading: loading.models.abstract,
}))
export default class Abstract extends PureComponent{
    componentDidMount(){
        const { dispatch } = this.props;
        dispatch({
          type:'abstract/fetch_introduction_item',
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, abstract:{ introduction_item } } = this.props;
    const { title, content,id } = introduction_item;
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
                        内容 :
                    </Col>
                    <Col span={16}>
                        <div style={{border:'1px solid #e8e8e8',padding:5,borderRadius:4,background:'#f4f4f4'}}>
                            <div className="content" dangerouslySetInnerHTML={{__html:content}}/>
                        </div>
                    </Col>
                </Row>
                <div style={{textAlign:'center'}}>
                    <Button type="primary" onClick={()=>{router.push(`/abstract/edit/${id}`)}}>修改</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
