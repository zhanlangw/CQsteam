(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[25],{"QPk/":function(e,t,a){"use strict";var r=a("TqRt");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var s=r(a("o0o1"));a("miYZ");var u=r(a("tsqr")),n=r(a("MVZn")),l=a("dCQc"),c={namespace:"result",state:{result_list:{totalCount:0,value:[]},result_params:{start:0,count:10}},effects:{fetch_result:s.default.mark(function e(t,a){var r,c,o,p;return s.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return r=t.payload,c=a.call,o=a.put,e.next=4,c(l.result,r);case 4:if(p=e.sent,e.prev=5,0!==p.status){e.next=16;break}if(0!==p.value.value.length||0===r.start){e.next=12;break}return e.next=10,o({type:"fetch_result",payload:(0,n.default)({},r,{start:r.start-10})});case 10:e.next=14;break;case 12:return e.next=14,o({type:"result",payload:{response:p.value,params:r}});case 14:e.next=17;break;case 16:u.default.error(p.message);case 17:e.next=22;break;case 19:e.prev=19,e.t0=e["catch"](5),u.default.error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 22:case"end":return e.stop()}},e,this,[[5,19]])})},reducers:{result:function(e,t){var a=t.payload;return(0,n.default)({},e,{result_list:a.response,result_params:(0,n.default)({},a.params)})}}};t.default=c}}]);