import React, { PureComponent, Fragment } from 'react';
import { Card, Table, Button, Icon, } from "antd";
import MyTag from "./MyTag";

export default class ButtonSelect extends PureComponent{
    constructor(props) {
        super(props);
    
        const value = this.props.value || 1;
        this.state = {
            value:value
        };
      }

    componentWillReceiveProps(nextProps) {
        // Should be a controlled component.
        if ('value' in nextProps) {
            const value = nextProps.value;
            this.setState({
                value:value
            });
        }
    }

    handleCurrencyChange = (currency) => {
        const status = currency ? 1 : 0;
        if (!('value' in this.props)) {
          this.setState({
            value:status
          });
        }
        this.triggerChange(status);
      }

    triggerChange = (changedValue) => {
        // Should provide an event to pass value to Form.
        const onChange = this.props.onChange;
        if (onChange) {
          onChange(changedValue);
        }
    }

    render(){
        const { value } = this.state;
        const { okText, noText } = this.props;
        return(
            <MyTag 
                okText={okText}
                value={value}
                noText={noText}
                onChange={this.handleCurrencyChange}
            />
        )
    }
}
