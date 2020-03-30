import React, { PureComponent } from 'react';
import { Icon } from 'antd';
import Link from 'umi/link';
import Debounce from 'lodash-decorators/debounce';
import styles from './index.less';
import RightContent from './RightContent';
import BreadcrumbView from '../PageHeader/breadcrumb';
import MenuContext from '@/layouts/MenuContext';

export default class GlobalHeader extends PureComponent {
  componentWillUnmount() {
    this.triggerResizeEvent.cancel();
  }
  /* eslint-disable*/
  @Debounce(600)
  triggerResizeEvent() {
    // eslint-disable-line
    const event = document.createEvent('HTMLEvents');
    event.initEvent('resize', true, false);
    window.dispatchEvent(event);
  }
  toggle = () => {
    const { collapsed, onCollapse } = this.props;
    onCollapse(!collapsed);
    this.triggerResizeEvent();
  };
  render() {
    const { collapsed, isMobile, logo } = this.props;
    return (
      <div className={styles.header}>
        <div className={styles.header_top}>
          {isMobile && (
            <Link to="/" className={styles.logo} key="logo">
              <img src={logo} alt="logo" width="32" />
            </Link>
          )}
          <span className={styles.trigger} onClick={this.toggle}>
            <Icon style={{color:'#fff'}} type={collapsed ? 'menu-unfold' : 'menu-fold'} />
          </span>
          <RightContent {...this.props} />
        </div>
        <div className={styles.breadcrumb}>
          <MenuContext.Consumer>{
            value => 
            {
              return(
                <BreadcrumbView 
                  breadcrumbSeparator=">" 
                  {...value} 
                  linkElement={Link}
                  itemRender={item => {
                    return item.name;
                  }}
                  />
              )
            }
          }</MenuContext.Consumer>
        </div>
      </div>
    );
  }
}
