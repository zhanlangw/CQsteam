(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[56],{LPmI:function(e,t,a){"use strict";a.r(t),a.d(t,"default",function(){return A});a("IzEo");var l,r,n,i,o=a("bx4M"),s=(a("+L6B"),a("2/Rp")),p=a("jehZ"),u=a.n(p),c=(a("5NDa"),a("5rEg")),d=(a("DZo9"),a("8z0m")),m=(a("Pwec"),a("CtXQ")),f=a("p0pE"),h=a.n(f),g=(a("miYZ"),a("tsqr")),v=a("2Taf"),y=a.n(v),b=a("vZ4D"),E=a.n(b),w=a("l4Ni"),P=a.n(w),k=a("ujKo"),L=a.n(k),F=a("MhPg"),C=a.n(F),V=(a("y8nQ"),a("Vl3Y")),q=a("q1tI"),M=a.n(q),x=a("MuoO"),U=a("usdK"),_=a("yEr3"),I=a.n(_),N=(a("Lzxq"),a("JSaN"),a("4mNk")),T=a.n(N),j=a("Psm9");I.a.use(T()({defaultColumns:5,defaultRows:3}));var z={labelCol:{span:4},wrapperCol:{span:8}},D=V["a"].Item,A=(l=V["a"].create(),r=Object(x["connect"])(function(e){var t=e.rules,a=e.loading;return{rules:t,loading:a.models.rules}}),l(n=r((i=function(e){function t(){var e,a;y()(this,t);for(var l=arguments.length,r=new Array(l),n=0;n<l;n++)r[n]=arguments[n];return a=P()(this,(e=L()(t)).call.apply(e,[this].concat(r))),a.normFile=function(e){return Array.isArray(e)?e:e.file.status?e&&e.fileList:e&&e.fileList&&e.fileList.filter(function(e){return!!e.response})},a.beforeUpload=function(e,t){var l=a.props.form.getFieldValue,r=l(t);if(r&&1===r.length)return g["a"].warning("\u53ea\u80fd\u4e0a\u4f201\u4e2a\u6587\u4ef6\uff01"),!1},a.fileMove=function(e){var t=a.props.dispatch;return e.response&&t({type:"global/fetch_file_del",payload:{path:e.response.value}}),!0},a.handleSubmit=function(){var e=a.props,t=e.dispatch,l=e.form.validateFields,r=e.match.params.id;l(function(e,a){e||t({type:"rules/fetch_rule_upd",payload:h()({},a,{content:a.content.toHTML(),imagePath:a.imagePath[0].response.value,path:a.path[0].response.value,id:r})})})},a.uploadHandler=function(e){var t=a.props,l=t.dispatch,r=t.form,n=r.getFieldValue,i=r.setFieldsValue;if(!e.file)return!1;var o=new FormData,s=n("content");o.append("file",e.file),l({type:"global/fetch_file_upload",payload:o,params:{fileType:5},callback:function(e){s=j["ContentUtils"].insertMedias(s,[{type:"IMAGE",url:e}]),i({content:s})}})},a}return C()(t,e),E()(t,[{key:"componentDidMount",value:function(){var e=this.props,t=e.dispatch,a=e.match.params.id,l=e.form.setFieldsValue;t({type:"rules/fetch_rule_item",payload:{id:a},callback:function(e){l({content:I.a.createEditorState(e.content)})}})}},{key:"render",value:function(){var e=this,t=[{key:"antd-uploader",type:"component",component:M.a.createElement(d["a"],{accept:"image/*",showUploadList:!1,customRequest:this.uploadHandler},M.a.createElement("button",{type:"button",className:"control-item button upload-button","data-title":"\u63d2\u5165\u56fe\u7247"},M.a.createElement(m["a"],{type:"picture",theme:"filled"})))}],a=this.props,l=a.form.getFieldDecorator,r=(a.loading,a.rules.rule_item),n=a.match.params.id,i=r[n]||{},p=i.title,f=(i.content,i.path),h=i.imagePath,g=i.number;return M.a.createElement(o["a"],{bordered:!1},M.a.createElement(o["a"],null,M.a.createElement(D,u()({},z,{label:"\u89c4\u5219\u6807\u9898"}),l("title",{rules:[{required:!0,message:"\u89c4\u5219\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:p})(M.a.createElement(c["a"],{placeholder:"\u8bf7\u586b\u5199\u89c4\u5219\u6807\u9898"}))),M.a.createElement(D,{labelCol:{span:4},wrapperCol:{span:17},label:"\u89c4\u5219\u5185\u5bb9"},l("content",{validateTrigger:"onBlur",rules:[{required:!0,validator:function(e,t,a){t.isEmpty()?a("\u8bf7\u8f93\u5165\u89c4\u5219\u5185\u5bb9"):a()}}]})(M.a.createElement(I.a,{contentStyle:{height:500,border:"1px solid #e8e8e8"},controls:["undo","redo","separator","font-size","line-height","letter-spacing","separator","text-color","bold","italic","underline","strike-through","separator","superscript","subscript","remove-styles","emoji","separator","text-indent","text-align","separator","headings","list-ul","list-ol","blockquote","code","separator","link","separator","hr","clear","separator","table"],extendControls:t,media:{accepts:{video:!1,audio:!1},externals:{video:!1,audio:!1,embed:!1}},placeholder:"\u8bf7\u8f93\u5165\u89c4\u5219\u5185\u5bb9"}))),M.a.createElement(D,u()({},z,{label:"\u4f4d\u7f6e\u7f16\u53f7"}),l("number",{rules:[{required:!0,message:"\u4f4d\u7f6e\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a"}],initialValue:g})(M.a.createElement(c["a"],{placeholder:"\u8bf7\u586b\u5199\u4f4d\u7f6e\u7f16\u53f7"}))),M.a.createElement(D,u()({},z,{label:"\u56fe\u7247"}),l("imagePath",{rules:[{required:!0,message:"\u8bf7\u4e0a\u4f20\u56fe\u7247"}],valuePropName:"fileList",getValueFromEvent:this.normFile,initialValue:h&&[{uid:"-1",name:h.split("/")[h.split("/").length-1],status:"done",response:{value:h},url:h}]||void 0})(M.a.createElement(d["a"],{name:"file",showUploadList:{showPreviewIcon:!1},listType:"picture-card",action:"/api/file/upload?fileType=5",accept:".jpg,.jpeg,.png",beforeUpload:function(t){return e.beforeUpload(t,"imagePath")},onRemove:this.fileMove},M.a.createElement(s["a"],{size:"small",type:"primary"},"\u4e0a\u4f20")))),M.a.createElement(D,u()({},z,{label:"\u9644\u4ef6"}),l("path",{rules:[{required:!0,message:"\u8bf7\u4e0a\u4f20\u9644\u4ef6"}],valuePropName:"fileList",getValueFromEvent:this.normFile,initialValue:f&&[{uid:"-1",name:f.split("/")[f.split("/").length-1],status:"done",response:{value:f}}]||void 0})(M.a.createElement(d["a"],{name:"file",showUploadList:{showPreviewIcon:!1},action:"/api/file/upload?fileType=5",beforeUpload:function(t){return e.beforeUpload(t,"path")},onRemove:this.fileMove},M.a.createElement(s["a"],{size:"small",type:"primary"},"\u4e0a\u4f20")))),M.a.createElement("div",{style:{textAlign:"center"}},M.a.createElement(s["a"],{onClick:this.handleSubmit,type:"primary"},"\u786e\u5b9a"),M.a.createElement(s["a"],{onClick:function(){U["a"].push("/rules/list")},style:{marginLeft:48}},"\u53d6\u6d88"))))}}]),t}(q["PureComponent"]),n=i))||n)||n)}}]);