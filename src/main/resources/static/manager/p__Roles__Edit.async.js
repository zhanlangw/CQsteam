(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[51],{vvCy:function(e,a,n){"use strict";n.r(a),n.d(a,"default",function(){return Z});n("IzEo");var t,r,i,l,o=n("bx4M"),s=(n("+L6B"),n("2/Rp")),c=n("jehZ"),p=n.n(c),u=(n("5NDa"),n("5rEg")),m=n("p0pE"),d=n.n(m),f=n("2Taf"),h=n.n(f),v=n("vZ4D"),y=n.n(v),E=n("l4Ni"),g=n.n(E),b=n("ujKo"),w=n.n(b),k=n("MhPg"),C=n.n(k),M=(n("y8nQ"),n("Vl3Y")),_=(n("ozfa"),n("MJZm")),D=n("q1tI"),N=n.n(D),j=n("MuoO"),q=n("usdK"),I=n("erN1"),J=(_["a"].TreeNode,{labelCol:{span:4},wrapperCol:{span:8}}),V=M["a"].Item,Z=(t=M["a"].create(),r=Object(j["connect"])(function(e){var a=e.role,n=e.loading;return{role:a,loading:n.models.role}}),t(i=r((l=function(e){function a(){var e,n;h()(this,a);for(var t=arguments.length,r=new Array(t),i=0;i<t;i++)r[i]=arguments[i];return n=g()(this,(e=w()(a)).call.apply(e,[this].concat(r))),n.handleSubmit=function(){var e=n.props,a=e.dispatch,t=e.form.validateFields,r=e.match.params.id;t(function(e,n){e||a({type:"role/fetch_role_upd",payload:d()({},n,{permissions:n.permissions.filter(function(e){return e.startsWith("/api/")}),id:r})})})},n}return C()(a,e),y()(a,[{key:"componentDidMount",value:function(){var e=this.props,a=e.dispatch,n=e.match.params.id;a({type:"role/fetch_role_item",payload:{id:n}})}},{key:"render",value:function(){var e=this.props,a=e.form.getFieldDecorator,n=(e.loading,e.role.role_item),t=e.match.params.id,r=n[t]||{},i=r.name,l=r.permissions,c=void 0===l?[]:l;return N.a.createElement(o["a"],{bordered:!1},N.a.createElement(o["a"],null,N.a.createElement(V,p()({},J,{label:"\u89d2\u8272\u540d\u79f0"}),a("name",{roles:[{required:!0,message:"\u89d2\u8272\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:i})(N.a.createElement(u["a"],{placeholder:"\u8bf7\u586b\u5199\u89d2\u8272\u540d\u79f0"}))),N.a.createElement(V,p()({},J,{label:"\u89d2\u8272\u6743\u9650"}),a("permissions",{roles:[{required:!0,message:"\u89d2\u8272\u6743\u9650\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:c.map(function(e){return e.interfaceUrl})})(N.a.createElement(I["a"],null))),N.a.createElement("div",{style:{textAlign:"center"}},N.a.createElement(s["a"],{onClick:this.handleSubmit,type:"primary"},"\u786e\u5b9a"),N.a.createElement(s["a"],{onClick:function(){q["a"].push("/roles/list")},style:{marginLeft:48}},"\u53d6\u6d88"))))}}]),a}(D["PureComponent"]),i=l))||i)||i)}}]);