(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[27],{NwUL:function(e,a,t){"use strict";t.r(a),t.d(a,"default",function(){return O});t("IzEo");var n,r,l,i,o=t("bx4M"),c=(t("+L6B"),t("2/Rp")),m=(t("OaEy"),t("2fM7")),s=t("jehZ"),u=t.n(s),d=(t("5NDa"),t("5rEg")),p=t("p0pE"),f=t.n(p),g=t("2Taf"),h=t.n(g),y=t("vZ4D"),v=t.n(y),E=t("l4Ni"),b=t.n(E),w=t("ujKo"),k=t.n(w),D=t("MhPg"),_=t.n(D),C=(t("y8nQ"),t("Vl3Y")),I=t("q1tI"),V=t.n(I),M=t("MuoO"),N=t("usdK"),q={labelCol:{span:8},wrapperCol:{span:8}},F=C["a"].Item,O=(n=C["a"].create(),r=Object(M["connect"])(function(e){var a=e.manager,t=e.loading;return{manager:a,loading:t.models.manager}}),n(l=r((i=function(e){function a(){var e,t;h()(this,a);for(var n=arguments.length,r=new Array(n),l=0;l<n;l++)r[l]=arguments[l];return t=b()(this,(e=k()(a)).call.apply(e,[this].concat(r))),t.state={confirmDirty:!1},t.handleSubmit=function(){var e=t.props,a=e.dispatch,n=e.form.validateFields,r=e.match.params.id;n(function(e,t){e||(t.roleIds=t.roleIds.map(function(e){return e.key}),a({type:"manager/fetch_manager_upd",payload:f()({},t,{id:r})}))})},t.checkConfirm=function(e,a,n){var r=t.props.form;a&&a!==r.getFieldValue("password")?n("\u4e24\u6b21\u8f93\u5165\u5bc6\u7801\u4e0d\u4e00\u81f4!"):n()},t.checkPassword=function(e,a,n){var r=t.props.form;a&&t.state.confirmDirty&&r.validateFields(["confirm"],{force:!0}),n()},t.handleConfirmBlur=function(e){var a=e.target.value;t.setState({confirmDirty:t.state.confirmDirty||!!a})},t}return _()(a,e),v()(a,[{key:"componentDidMount",value:function(){var e=this.props,a=e.dispatch,t=e.match.params.id;a({type:"manager/fetch_manager_item",payload:{id:t}}),a({type:"manager/fetch_role_list",payload:{start:0,count:1e4}})}},{key:"render",value:function(){var e=this.props,a=e.form.getFieldDecorator,t=e.loading,n=e.manager,r=n.role_list,l=n.manager_item,i=e.match.params.id,s=l[i]||{},p=s.name,f=s.loginName,g=s.role,h=void 0===g?[]:g;return V.a.createElement(o["a"],{bordered:!1},V.a.createElement(o["a"],null,V.a.createElement(F,u()({},q,{label:"\u7ba1\u7406\u5458\u540d\u79f0"}),a("name",{rules:[{required:!0,message:"\u7ba1\u7406\u5458\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:p})(V.a.createElement(d["a"],{placeholder:"\u8bf7\u586b\u5199\u7ba1\u7406\u5458\u540d\u79f0"}))),V.a.createElement(F,u()({},q,{label:"\u7ba1\u7406\u5458\u767b\u5f55\u540d"}),a("loginName",{rules:[{required:!0,message:"\u767b\u5f55\u540d\u4e0d\u80fd\u4e3a\u7a7a"},{pattern:/(?:^1[3456789]|^9[28])\d{9}$/,message:"\u8bf7\u8f93\u5165\u6b63\u786e\u624b\u673a\u53f7"}],initialValue:f})(V.a.createElement(d["a"],{placeholder:"\u8bf7\u586b\u5199\u624b\u673a\u53f7\u7801"}))),V.a.createElement(F,u()({},q,{label:"\u9009\u62e9\u89d2\u8272"}),a("roleIds",{rules:[{required:!0,message:"\u89d2\u8272\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:h.map(function(e){return{label:e.name,key:e.id}})})(V.a.createElement(m["a"],{labelInValue:!0,style:{width:"100%"},mode:"multiple",placeholder:"\u8bf7\u9009\u62e9\u89d2\u8272"},r.map(function(e){return V.a.createElement(m["a"].Option,{value:e.id,key:e.id},e.name)})))),V.a.createElement("div",{style:{textAlign:"center"}},V.a.createElement(c["a"],{loading:t,onClick:this.handleSubmit,type:"primary"},"\u786e\u5b9a"),V.a.createElement(c["a"],{onClick:function(){N["a"].push("/manager/list")},style:{marginLeft:48}},"\u53d6\u6d88"))))}}]),a}(I["PureComponent"]),l=i))||l)||l)}}]);