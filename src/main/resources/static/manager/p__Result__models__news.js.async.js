(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[49],{"9QyL":function(e,t,r){"use strict";r.r(t);var a=r("p0pE"),n=r.n(a),s=r("d6i3"),u=r.n(s),c=(r("miYZ"),r("tsqr")),p=r("1l/V"),i=r.n(p),l=r("t3Un"),o=r("Qyje");function f(e){return h.apply(this,arguments)}function h(){return h=i()(u.a.mark(function e(t){return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return e.abrupt("return",Object(l["a"])("/api/result/list?".concat(Object(o["stringify"])(t))));case 1:case"end":return e.stop()}},e)})),h.apply(this,arguments)}function d(e){return w.apply(this,arguments)}function w(){return w=i()(u.a.mark(function e(t){return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return e.abrupt("return",Object(l["a"])("/api/result/item?".concat(Object(o["stringify"])(t))));case 1:case"end":return e.stop()}},e)})),w.apply(this,arguments)}function y(e){return x.apply(this,arguments)}function x(){return x=i()(u.a.mark(function e(t){return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return e.abrupt("return",Object(l["a"])("/api/result/upd",{method:"POST",body:t}));case 1:case"end":return e.stop()}},e)})),x.apply(this,arguments)}function m(e){return v.apply(this,arguments)}function v(){return v=i()(u.a.mark(function e(t){return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return e.abrupt("return",Object(l["a"])("/api/result/add",{method:"POST",body:t}));case 1:case"end":return e.stop()}},e)})),v.apply(this,arguments)}function b(e){return _.apply(this,arguments)}function _(){return _=i()(u.a.mark(function e(t){return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return e.abrupt("return",Object(l["a"])("/api/result/del?".concat(Object(o["stringify"])(t))));case 1:case"end":return e.stop()}},e)})),_.apply(this,arguments)}var k=r("usdK"),O=r("T+dw");t["default"]={namespace:"result",state:{result_list:{totalCount:0,value:[]},result_params:{start:0,count:O["count"]},result_item:{}},effects:{fetch_result_upd:u.a.mark(function e(t,r){var a,n,s;return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,r.put,e.next=4,n(y,a);case 4:s=e.sent;try{0===s.status?(c["a"].success("\u4fee\u6539\u6210\u529f!"),k["a"].push("/result/list")):c["a"].error(s.message)}catch(e){c["a"].error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01")}case 6:case"end":return e.stop()}},e)}),fetch_result_add:u.a.mark(function e(t,r){var a,n,s;return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,r.put,e.next=4,n(m,a);case 4:s=e.sent;try{0===s.status?(c["a"].success("\u65b0\u589e\u6210\u529f!"),k["a"].push("/result/list")):c["a"].error(s.message)}catch(e){c["a"].error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01")}case 6:case"end":return e.stop()}},e)}),fetch_result_del:u.a.mark(function e(t,r){var a,n,s,p,i,l;return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,s=r.put,p=r.select,e.next=4,n(b,a);case 4:return i=e.sent,e.next=7,p(function(e){return e.result});case 7:if(l=e.sent,e.prev=8,0!==i.status){e.next=15;break}return c["a"].success("\u5220\u9664\u6210\u529f!"),e.next=13,s({type:"fetch_result_list",payload:l.result_params});case 13:e.next=16;break;case 15:c["a"].error(i.message);case 16:e.next=21;break;case 18:e.prev=18,e.t0=e["catch"](8),c["a"].error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 21:case"end":return e.stop()}},e,null,[[8,18]])}),fetch_result_list:u.a.mark(function e(t,r){var a,s,p,i;return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,s=r.call,p=r.put,e.next=4,s(f,a);case 4:if(i=e.sent,e.prev=5,0!==i.status){e.next=16;break}if(0!==i.value.value.length||0===a.start){e.next=12;break}return e.next=10,p({type:"fetch_result_list",payload:n()({},a,{start:a.start-O["count"]})});case 10:e.next=14;break;case 12:return e.next=14,p({type:"result_list",payload:{response:i.value,params:a}});case 14:e.next=17;break;case 16:c["a"].error(i.message);case 17:e.next=22;break;case 19:e.prev=19,e.t0=e["catch"](5),c["a"].error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 22:case"end":return e.stop()}},e,null,[[5,19]])}),fetch_result_item:u.a.mark(function e(t,r){var a,n,s,p;return u.a.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return a=t.payload,n=r.call,s=r.put,e.next=4,n(d,a);case 4:if(p=e.sent,e.prev=5,0!==p.status){e.next=11;break}return e.next=9,s({type:"result_item",payload:{response:p.value,id:a.id}});case 9:e.next=12;break;case 11:c["a"].error(p.message);case 12:e.next=17;break;case 14:e.prev=14,e.t0=e["catch"](5),c["a"].error("\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01");case 17:case"end":return e.stop()}},e,null,[[5,14]])})},reducers:{result_item:function(e,t){var r=t.payload,a=e.result_item;return a[r.id]=r.response,n()({},e)},result_list:function(e,t){var r=t.payload;return n()({},e,{result_params:r.params,result_list:r.response})}}}}}]);