(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[36],{QI7n:function(e,t,a){"use strict";a.r(t),a.d(t,"default",function(){return j});a("IzEo");var n,r,l,o,i=a("bx4M"),s=(a("+L6B"),a("2/Rp")),p=a("jehZ"),c=a.n(p),u=(a("5NDa"),a("5rEg")),d=(a("DZo9"),a("8z0m")),f=(a("Pwec"),a("CtXQ")),m=a("p0pE"),h=a.n(m),v=(a("miYZ"),a("tsqr")),y=a("2Taf"),b=a.n(y),g=a("vZ4D"),w=a.n(g),E=a("l4Ni"),k=a.n(E),C=a("ujKo"),L=a.n(C),x=a("MhPg"),F=a.n(x),M=(a("y8nQ"),a("Vl3Y")),q=a("q1tI"),I=a.n(q),N=a("MuoO"),P=a("usdK"),U=a("yEr3"),_=a.n(U),z=(a("Lzxq"),a("JSaN"),a("4mNk")),A=a.n(z),D=a("Psm9");_.a.use(A()({defaultColumns:5,defaultRows:3}));var T={labelCol:{span:4},wrapperCol:{span:8}},V=M["a"].Item,j=(n=M["a"].create(),r=Object(N["connect"])(function(e){var t=e.news,a=e.loading;return{news:t,loading:a.models.news}}),n(l=r((o=function(e){function t(){var e,a;b()(this,t);for(var n=arguments.length,r=new Array(n),l=0;l<n;l++)r[l]=arguments[l];return a=k()(this,(e=L()(t)).call.apply(e,[this].concat(r))),a.normFile=function(e){return Array.isArray(e)?e:e.file.status?e&&e.fileList:e&&e.fileList&&e.fileList.filter(function(e){return!!e.response})},a.beforeUpload=function(e,t){var n=a.props.form.getFieldValue,r=n(t);if(r&&1===r.length)return v["a"].warning("\u53ea\u80fd\u4e0a\u4f201\u4e2a\u6587\u4ef6\uff01"),!1},a.fileMove=function(e){var t=a.props.dispatch;return e.response&&t({type:"global/fetch_file_del",payload:{path:e.response.value}}),!0},a.handleSubmit=function(){var e=a.props,t=e.dispatch,n=e.form.validateFields;n(function(e,a){if(!e){var n=h()({},a,{content:a.content.toHTML()});a.path&&a.path[0]&&(n.path=a.path[0].response.value),t({type:"news/fetch_news_add",payload:h()({},n)})}})},a.uploadHandler=function(e){var t=a.props,n=t.dispatch,r=t.form,l=r.getFieldValue,o=r.setFieldsValue;if(!e.file)return!1;var i=new FormData,s=l("content");i.append("file",e.file),n({type:"global/fetch_file_upload",payload:i,params:{fileType:5},callback:function(e){s=D["ContentUtils"].insertMedias(s,[{type:"IMAGE",url:e}]),o({content:s})}})},a}return F()(t,e),w()(t,[{key:"render",value:function(){var e=this,t=[{key:"antd-uploader",type:"component",component:I.a.createElement(d["a"],{accept:"image/*",showUploadList:!1,customRequest:this.uploadHandler},I.a.createElement("button",{type:"button",className:"control-item button upload-button","data-title":"\u63d2\u5165\u56fe\u7247"},I.a.createElement(f["a"],{type:"picture",theme:"filled"})))}],a=this.props,n=a.form.getFieldDecorator;a.loading;return I.a.createElement(i["a"],{bordered:!1},I.a.createElement(i["a"],null,I.a.createElement(V,c()({},T,{label:"\u6807\u9898"}),n("title",{rules:[{required:!0,message:"\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a"}]})(I.a.createElement(u["a"],{placeholder:"\u8bf7\u586b\u5199\u6807\u9898"}))),I.a.createElement(V,c()({},T,{label:"\u9644\u4ef6"}),n("path",{valuePropName:"fileList",getValueFromEvent:this.normFile})(I.a.createElement(d["a"],{name:"file",showUploadList:{showPreviewIcon:!1},action:"/api/file/upload?fileType=5",beforeUpload:function(t){return e.beforeUpload(t,"path")},onRemove:this.fileMove},I.a.createElement(s["a"],{size:"small",type:"primary"},"\u4e0a\u4f20")))),I.a.createElement(V,{labelCol:{span:4},wrapperCol:{span:17},label:"\u5185\u5bb9"},n("content",{validateTrigger:"onBlur",rules:[{required:!0,validator:function(e,t,a){t.isEmpty()?a("\u8bf7\u8f93\u5165\u5185\u5bb9"):a()}}]})(I.a.createElement(_.a,{contentStyle:{height:500,border:"1px solid #e8e8e8"},controls:["undo","redo","separator","font-size","line-height","letter-spacing","separator","text-color","bold","italic","underline","strike-through","separator","superscript","subscript","remove-styles","emoji","separator","text-indent","text-align","separator","headings","list-ul","list-ol","blockquote","code","separator","link","separator","hr","clear","separator","table"],extendControls:t,media:{accepts:{video:!1,audio:!1},externals:{video:!1,audio:!1,embed:!1}},placeholder:"\u8bf7\u8f93\u5165\u5185\u5bb9"}))),I.a.createElement("div",{style:{textAlign:"center"}},I.a.createElement(s["a"],{onClick:this.handleSubmit,type:"primary"},"\u786e\u5b9a"),I.a.createElement(s["a"],{onClick:function(){P["a"].push("/news/list")},style:{marginLeft:48}},"\u53d6\u6d88"))))}}]),t}(q["PureComponent"]),l=o))||l)||l)}}]);