(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[26],{OgmY:function(e,t,a){e.exports={login:"login___2BfbF"}},Y5yc:function(e,t,a){"use strict";var l=a("TqRt"),r=a("284h");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0,a("sRBo");var n=l(a("kaz8")),u=l(a("MVZn")),o=l(a("lwsE")),d=l(a("W8MJ")),i=l(a("a1gu")),s=l(a("Nsbk")),f=l(a("7W2i"));a("vbVf");var c,g,m,p=l(a("KKfN")),h=r(a("q1tI")),v=l(a("OgmY")),y=l(a("mOP9")),E=a("MuoO"),w=p.default.UserName,b=p.default.Password,k=p.default.Submit,x=(c=(0,E.connect)(function(e){var t=e.user,a=e.loading;return{user:t,loading:a.models.user}}),c((m=function(e){function t(){var e,a;(0,o.default)(this,t);for(var l=arguments.length,r=new Array(l),n=0;n<l;n++)r[n]=arguments[n];return a=(0,i.default)(this,(e=(0,s.default)(t)).call.apply(e,[this].concat(r))),a.handleSubmit=function(e,t){e||a.props.dispatch({type:"user/login",payload:(0,u.default)({},t)})},a.changeAutoLogin=function(e){var t=e.target.checked;a.props.dispatch({type:"user/changeAutoLogin",payload:{autoLogin:t}})},a}return(0,f.default)(t,e),(0,d.default)(t,[{key:"render",value:function(){var e=this.props,t=e.user.autoLogin,a=e.loading,l=localStorage.getItem("user")&&JSON.parse(localStorage.getItem("user")),r=l&&l.username,u=l&&l.password;return h.default.createElement("div",{className:v.default.login,style:{minHeight:400,height:"calc(100vh - ".concat(318,"px")}},h.default.createElement("div",{style:{width:380,height:360,background:"#fff",borderRadius:5,float:"right",marginRight:"10%",position:"relative",top:"50%",transform:"translateY(-50%)"}},h.default.createElement(p.default,{onSubmit:this.handleSubmit},h.default.createElement("div",{style:{padding:"12px 48px 0"}},h.default.createElement("div",{style:{fontSize:18,textAlign:"center",marginBottom:12,color:"#666"}},"\u767b\u5f55"),h.default.createElement(w,{defaultValue:r,name:"username",placeholder:"\u8bf7\u8f93\u5165\u7528\u6237\u540d",rules:[{required:!0,message:"\u8bf7\u8f93\u5165\u7528\u6237\u540d"}]}),h.default.createElement(b,{defaultValue:u,name:"password",placeholder:"\u8bf7\u8f93\u5165\u5bc6\u7801",rules:[{required:!0,message:"\u8bf7\u8f93\u5165\u5bc6\u7801"}],style:{cursor:"pointer"},type:"eye-o"})),h.default.createElement("div",{style:{overflow:"hidden",padding:"0 48px"}},h.default.createElement("span",{style:{float:"left"}},h.default.createElement(n.default,{defaultChecked:t,onChange:this.changeAutoLogin},"\u8bb0\u4f4f\u5bc6\u7801")),h.default.createElement(y.default,{to:"/user/resetPassword",style:{float:"right"},href:""},"\u5fd8\u8bb0\u5bc6\u7801?")),h.default.createElement("div",{style:{padding:"0 48px"}},h.default.createElement(k,{loading:a},"\u767b\u5f55"),h.default.createElement("div",{style:{textAlign:"center",marginBottom:12}},"\u8fd8\u6ca1\u6709\u8d26\u53f7\uff1f",h.default.createElement(y.default,{className:v.default.register,to:"/user/register"},"\u7acb\u5373\u6ce8\u518c"))))))}}]),t}(h.PureComponent),g=m))||g);t.default=x}}]);