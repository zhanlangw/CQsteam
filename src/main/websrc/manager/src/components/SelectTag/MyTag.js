import React, { PureComponent, Fragment } from 'react';
import { Card, Table, Button, Icon, } from "antd";

export default class MyTag extends PureComponent{
    state={
        status:undefined
    }

    componentWillReceiveProps(nextProps) {
        // Should be a controlled component.
        if ('value' in nextProps) {
            const value = nextProps.value;
            this.setState({
                status:value
            });
        }
    }

    handleChange = (status) => {
        status ? true : false;
        const { onChange } = this.props;
        onChange(status);
        this.setState({
            status
        })
    }

    render(){
        let { status } = this.state;
        status ? true :false;
        const { okText, noText } = this.props;
        return(
            <div>
                <Button size="small" type={(status!==undefined && status) ? 'primary' : ''} style={{width: 85}} onClick={() => {this.handleChange(!status)}}>{okText || '是'}</Button>
                <Button size="small" type={(status!==undefined && !status) ? 'primary' : ''} style={{marginLeft: 30, width: 85}} onClick={() => {this.handleChange(!status)}}>{noText || '否'}</Button>
            </div>
        )
    }
}
