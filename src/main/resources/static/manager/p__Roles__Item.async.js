(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[52],{rPys:function(e,a,n){"use strict";n.r(a),n.d(a,"default",function(){return J});n("IzEo");var r,t,l,o=n("bx4M"),i=(n("+L6B"),n("2/Rp")),s=n("jehZ"),c=n.n(s),u=(n("5NDa"),n("5rEg")),d=n("2Taf"),p=n.n(d),m=n("vZ4D"),f=n.n(m),h=n("l4Ni"),b=n.n(h),E=n("ujKo"),g=n.n(E),v=n("MhPg"),y=n.n(v),w=(n("y8nQ"),n("Vl3Y")),k=(n("ozfa"),n("MJZm")),M=n("q1tI"),C=n.n(M),D=n("MuoO"),N=n("usdK"),j=n("erN1"),q=(k["a"].TreeNode,{labelCol:{span:4},wrapperCol:{span:8}}),I=w["a"].Item,J=(r=w["a"].create(),t=Object(D["connect"])(function(e){var a=e.role,n=e.loading;return{role:a,loading:n.models.role}}),r(l=t(l=function(e){function a(){return p()(this,a),b()(this,g()(a).apply(this,arguments))}return y()(a,e),f()(a,[{key:"componentDidMount",value:function(){var e=this.props,a=e.dispatch,n=e.match.params.id;a({type:"role/fetch_role_item",payload:{id:n}})}},{key:"render",value:function(){var e=this.props,a=e.form.getFieldDecorator,n=(e.loading,e.role.role_item),r=e.match.params.id,t=n[r]||{},l=t.name,s=t.permissions,d=void 0===s?[]:s;return C.a.createElement(o["a"],{bordered:!1},C.a.createElement(o["a"],null,C.a.createElement(I,c()({},q,{label:"\u89d2\u8272\u540d\u79f0"}),a("name",{roles:[{required:!0,message:"\u89d2\u8272\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:l})(C.a.createElement(u["a"],{disabled:!0,placeholder:"\u8bf7\u586b\u5199\u89d2\u8272\u540d\u79f0"}))),C.a.createElement(I,c()({},q,{label:"\u89d2\u8272\u6743\u9650"}),a("permissions",{roles:[{required:!0,message:"\u89d2\u8272\u6743\u9650\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:d.map(function(e){return e.interfaceUrl})})(C.a.createElement(j["a"],{disabled:!0}))),C.a.createElement("div",{style:{textAlign:"center"}},C.a.createElement(i["a"],{onClick:function(){N["a"].push("/roles/list")}},"\u8fd4\u56de"))))}}]),a}(M["PureComponent"]))||l)||l)}}]);