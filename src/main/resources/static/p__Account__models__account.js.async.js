(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[12],{arIw:function(e,t,r){"use strict";var a=r("TqRt");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var n=a(r("MVZn")),s=a(r("o0o1"));r("miYZ");var u=a(r("tsqr")),c=r("dCQc"),o={namespace:"account",state:{userinfo:{idCard:"",name:"",telephone:""},project:[]},effects:{fetch_change_password:s.default.mark(function e(t,r){var a,n,o,p;return s.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,o=r.put,e.next=4,n(c.change_password,a);case 4:if(p=e.sent,e.prev=5,0!==p.status){e.next=12;break}return u.default.success("\u4fee\u6539\u6210\u529f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55!"),e.next=10,o({type:"user/logout"});case 10:e.next=17;break;case 12:if(1018!==p.status){e.next=16;break}return e.next=15,o({type:"user/logout"});case 15:return e.abrupt("return");case 16:u.default.error(p.message);case 17:e.next=22;break;case 19:e.prev=19,e.t0=e["catch"](5),u.default.error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 22:case"end":return e.stop()}},e,this,[[5,19]])}),fetch_project_del:s.default.mark(function e(t,r){var a,n,o,p;return s.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,o=r.put,e.next=4,n(c.project_del,a);case 4:if(p=e.sent,e.prev=5,0!==p.status){e.next=11;break}return e.next=9,o({type:"fetch_project"});case 9:e.next=16;break;case 11:if(1018!==p.status){e.next=15;break}return e.next=14,o({type:"user/logout"});case 14:return e.abrupt("return");case 15:u.default.error(p.message);case 16:e.next=21;break;case 18:e.prev=18,e.t0=e["catch"](5),u.default.error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 21:case"end":return e.stop()}},e,this,[[5,18]])}),fetch_project:s.default.mark(function e(t,r){var a,n,o,p;return s.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,o=r.put,e.next=4,n(c.project,a);case 4:if(p=e.sent,e.prev=5,0!==p.status){e.next=11;break}return e.next=9,o({type:"project",payload:p.value});case 9:e.next=16;break;case 11:if(1018!==p.status){e.next=15;break}return e.next=14,o({type:"user/logout"});case 14:return e.abrupt("return");case 15:u.default.error(p.message);case 16:e.next=21;break;case 18:e.prev=18,e.t0=e["catch"](5),u.default.error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 21:case"end":return e.stop()}},e,this,[[5,18]])}),fetch_userinfo:s.default.mark(function e(t,r){var a,n,o,p;return s.default.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,o=r.put,e.next=4,n(c.userinfo,a);case 4:if(p=e.sent,e.prev=5,0!==p.status){e.next=11;break}return e.next=9,o({type:"userinfo",payload:p.value});case 9:e.next=16;break;case 11:if(1018!==p.status){e.next=15;break}return e.next=14,o({type:"user/logout"});case 14:return e.abrupt("return");case 15:u.default.error(p.message);case 16:e.next=21;break;case 18:e.prev=18,e.t0=e["catch"](5),u.default.error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 21:case"end":return e.stop()}},e,this,[[5,18]])})},reducers:{project:function(e,t){var r=t.payload;return(0,n.default)({},e,{project:r})},userinfo:function(e,t){var r=t.payload;return(0,n.default)({},e,{userinfo:r})}}};t.default=o}}]);