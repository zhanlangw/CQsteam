import React, { PureComponent, Fragment } from 'react';
import { Tree } from "antd";
import { connect } from 'dva';

const { TreeNode } = Tree;
@connect(({ role, loading }) => ({
    role,
    loading: loading.models.role,
}))
export default class CheckTree extends PureComponent{
    constructor(props) {
        super(props);
        const value = this.props.value || [];
        this.state = {
            value:value
        };
      }

    componentDidMount(){
        const { dispatch} = this.props;
        dispatch({
            type:'role/fetch_role_tree',
        })
    }

    componentWillReceiveProps(nextProps) {
        // Should be a controlled component
        if ('value' in nextProps) {
            const value = nextProps.value;
            this.setState({
                value:value
            });
        }
    }

    handleCurrencyChange = (currency) => {
        const status = currency.length ? currency : [];
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

    renderTreeNodes = data => data.map((item) => {
        if (item.children) {
          return (
            <TreeNode title={item.title} key={item.key} dataRef={item}>
              {this.renderTreeNodes(item.children)}
            </TreeNode>
          );
        }
        return <TreeNode {...item} />;
    })

    render(){
        const { value } = this.state;
        const { role:{ role_tree }, disabled} = this.props;
        return(
            <div style={{border:'1px solid #d9d9d9',borderRadius:5,height:300,overflow:'auto'}}>
                <Tree
                    checkable
                    disabled={disabled}
                    onCheck={this.handleCurrencyChange}
                    checkedKeys={value}
                >
                    {this.renderTreeNodes(role_tree)}
                </Tree>   
            </div>
        )
    }
}
