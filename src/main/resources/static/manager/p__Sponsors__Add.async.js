(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[60],{lNFi:function(e,a,t){"use strict";t.r(a),t.d(a,"default",function(){return I});t("IzEo");var n,r,o,l,i=t("bx4M"),s=(t("DZo9"),t("8z0m")),p=(t("+L6B"),t("2/Rp")),c=t("jehZ"),u=t.n(c),f=(t("5NDa"),t("5rEg")),d=t("p0pE"),m=t.n(d),h=(t("miYZ"),t("tsqr")),g=t("2Taf"),v=t.n(g),y=t("vZ4D"),b=t.n(y),E=t("l4Ni"),w=t.n(E),L=t("ujKo"),F=t.n(L),U=t("MhPg"),j=t.n(U),k=(t("y8nQ"),t("Vl3Y")),C=t("q1tI"),M=t.n(C),R=t("MuoO"),q=t("usdK"),A={labelCol:{span:4},wrapperCol:{span:8}},D=k["a"].Item,I=(n=k["a"].create(),r=Object(R["connect"])(function(e){var a=e.sponsors,t=e.loading;return{sponsors:a,loading:t.models.sponsors}}),n(o=r((l=function(e){function a(){var e,t;v()(this,a);for(var n=arguments.length,r=new Array(n),o=0;o<n;o++)r[o]=arguments[o];return t=w()(this,(e=F()(a)).call.apply(e,[this].concat(r))),t.normFile=function(e){return Array.isArray(e)?e:e.file.status?e&&e.fileList:e&&e.fileList&&e.fileList.filter(function(e){return!!e.response})},t.beforeUpload=function(e,a){var n=t.props.form.getFieldValue,r=n(a);if(r&&1===r.length)return h["a"].warning("\u53ea\u80fd\u4e0a\u4f201\u4e2a\u6587\u4ef6\uff01"),!1},t.fileMove=function(e){var a=t.props.dispatch;return e.response&&a({type:"global/fetch_file_del",payload:{path:e.response.value}}),!0},t.handleSubmit=function(){var e=t.props,a=e.dispatch,n=e.form.validateFields;n(function(e,t){e||a({type:"sponsors/fetch_sponsors_add",payload:m()({},t,{path:t.path[0].response.value})})})},t}return j()(a,e),b()(a,[{key:"render",value:function(){var e=this,a=this.props,t=a.form.getFieldDecorator,n=a.loading;return M.a.createElement(i["a"],{bordered:!1},M.a.createElement(i["a"],null,M.a.createElement(D,u()({},A,{label:"\u8d5e\u52a9\u5546URL"}),t("url",{rules:[{required:!0,message:"\u8d5e\u52a9\u5546URL\u4e0d\u80fd\u4e3a\u7a7a"}]})(M.a.createElement(f["a"],{placeholder:"\u8bf7\u586b\u5199\u8d5e\u52a9\u5546URL"}))),M.a.createElement(D,u()({},A,{label:"\u56fe\u7247"}),t("path",{rules:[{required:!0,message:"\u8bf7\u4e0a\u4f20\u56fe\u7247"}],valuePropName:"fileList",getValueFromEvent:this.normFile})(M.a.createElement(s["a"],{name:"file",showUploadList:{showPreviewIcon:!1},listType:"picture-card",action:"/api/file/upload?fileType=5",accept:".jpg,.jpeg,.png",beforeUpload:function(a){return e.beforeUpload(a,"path")},onRemove:this.fileMove},M.a.createElement(p["a"],{size:"small",type:"primary"},"\u4e0a\u4f20")))),M.a.createElement("div",{style:{textAlign:"center"}},M.a.createElement(p["a"],{loading:n,onClick:this.handleSubmit,type:"primary"},"\u786e\u5b9a"),M.a.createElement(p["a"],{onClick:function(){q["a"].push("/sponsors/list")},style:{marginLeft:48}},"\u53d6\u6d88"))))}}]),a}(C["PureComponent"]),o=l))||o)||o)}}]);