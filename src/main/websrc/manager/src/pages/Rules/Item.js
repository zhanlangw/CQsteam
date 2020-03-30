import React, { PureComponent } from 'react';
import { Card, Table, Icon, Button, Tooltip, Modal, Form, Input, Col, Row, Upload, message } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import router from 'umi/router';

@Form.create()
@connect(({ rules, loading }) => ({
  rules,
  loading: loading.models.rules,
}))
export default class Item extends PureComponent{
    componentDidMount(){
        const { dispatch, match:{params:{id}} } = this.props;
        dispatch({
          type:'rules/fetch_rule_item',
          payload:{
           id:id
          },
        })
    }

  render(){
    const { form:{ getFieldDecorator }, loading, rules:{ rule_item }, match:{params:{id}} } = this.props;
    const item = rule_item[id] || {};
    const { title, content, path, imagePath, number } = item;
    return (
      <Card bordered={false}>
            <Card>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        规则标题 :
                    </Col>
                    <Col span={8}>
                        <div style={{border:'1px solid #e8e8e8',padding:5,borderRadius:4,background:'#f4f4f4'}}>
                            {title} 
                        </div>
                    </Col>
                </Row>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        规则内容 :
                    </Col>
                    <Col span={16}>
                        <div style={{border:'1px solid #e8e8e8',padding:5,borderRadius:4,background:'#f4f4f4'}}>
                            <div className="content" dangerouslySetInnerHTML={{__html:content}}/>
                        </div>
                    </Col>
                </Row>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        位置编号 :
                    </Col>
                    <Col span={8}>
                        <div style={{border:'1px solid #e8e8e8',padding:5,borderRadius:4,background:'#f4f4f4'}}>
                            {number}
                        </div>
                    </Col>
                </Row>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        图片 :
                    </Col>
                    <Col span={8}>
                        <img src={imagePath} style={{width:100}} alt=""/>
                    </Col>
                </Row>
                <Row gutter={10} style={{marginBottom:24}}>
                    <Col span={4} style={{textAlign:'right',padding:5}}>
                        附件 :
                    </Col>
                    <Col span={8} style={{padding:5}}>
                        <a href="javascript:;">{path && (path.split('/')[path.split('/').length-1]) || ''}</a>
                    </Col>
                </Row>
                <div style={{textAlign:'center'}}>
                    <Button onClick={()=>{router.push('/rules/list')}}>返回</Button>
                </div>
            </Card>
        </Card>
    )
  }
}
