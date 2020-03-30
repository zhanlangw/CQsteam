export default [
  // user
  {
    path: '/user',
    component: '../layouts/UserLayout',
    routes: [
      { path: '/user', redirect: '/user/login' },
      { path: '/user/login', name: '登录', component: './User/Login' },
      {
        component: '404',
      },
    ],
  },
  // app
  {
    path: '/',
    component: '../layouts/BasicLayout',
    Routes: ['src/pages/Authorized'],
    routes: [
      { path: '/', redirect: '/match/list' },
      {
        path: '/match',
        name: '比赛类别',
        icon: 'icon-leibie',
        routes: [
          {
            path: '/match/list',
            name: '类别列表',
            component: './Match/List',
          },
          {
            path: '/match/list/item/:id',
            name: '类别详情',
            component: './Match/Item',
            hideInMenu:true
          },
          {
            path: '/match/list/add',
            name: '新增类别',
            component: './Match/Add',
            hideInMenu:true
          },
          {
            path: '/match/list/edit/:id',
            name: '修改类别',
            component: './Match/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        path: '/project',
        icon: 'icon-dasai',
        name: '大赛管理',
        routes: [
          {
            path: '/project/list',
            name: '项目列表',
            component: './Project/List',
          },
          {
            path: '/project/list/item/:id',
            name: '项目详情',
            component: './Project/Item',
            hideInMenu:true
          },
          {
            path: '/project/list/edit/:id',
            name: '修改项目',
            component: './Project/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        path: '/export',
        icon: 'icon-daochu',
        name: '智能导出',
        component: './Export',
      },
      {
        path: '/statistics',
        name: '数据统计',
        icon: 'icon-tongji',
        routes: [
          {
            path: '/statistics/enter',
            name: '报名数据',
            component: './Statistics/Enter',
          },
          {
            path: '/statistics/register',
            name: '注册数据',
            component: './Statistics/Register',
          },
          {
            path: '/statistics/visitor',
            name: '访客数据',
            component: './Statistics/Visitor',
          },
          {
            path: '/statistics/word',
            name: '提交word数据',
            component: './Statistics/Word',
          },
          {
            path: '/statistics/document',
            name: '最终确认文档数据',
            component: './Statistics/Document',
          },
          {
            path: '/statistics/award',
            name: '获奖等级统计',
            component: './Statistics/Award',
          },
        ],
      },
      {
        name: 'banner管理',
        icon: 'icon-banner',
        path: '/banner',
        routes: [
          {
            path: '/banner/list',
            name: 'banner列表',
            component: './Banner/List',
          },
          {
            path: '/banner/list/add',
            name: '新增banner',
            component: './Banner/Add',
            hideInMenu:true
          },
          {
            path: '/banner/list/edit/:id',
            name: '修改banner',
            component: './Banner/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '新闻动态',
        icon: 'icon-xinwen',
        path: '/news',
        routes: [
          {
            path: '/news/list',
            name: '新闻列表',
            component: './News/List',
          },
          {
            path: '/news/list/item/:id',
            name: '新闻详情',
            component: './News/Item',
            hideInMenu:true
          },
          {
            path: '/news/list/add',
            name: '新增新闻',
            component: './News/Add',
            hideInMenu:true
          },
          {
            path: '/news/list/edit/:id',
            name: '修改新闻',
            component: './News/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '活动日历',
        icon: 'icon-rili',
        path: '/activity',
        routes: [
          {
            path: '/activity/list',
            name: '事件列表',
            component: './Activity/List',
          },
          {
            path: '/activity/list/add',
            name: '新增事件',
            component: './Activity/Add',
            hideInMenu:true
          },
          {
            path: '/activity/list/edit/:id',
            name: '修改事件',
            component: './Activity/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '比赛结果发布',
        icon: 'icon-ERP_daibandangqianzhuangtai',
        path: '/result',
        routes: [
          {
            path: '/result/list',
            name: '结果列表',
            component: './Result/List',
          },
          {
            path: '/result/list/item/:id',
            name: '结果详情',
            component: './Result/Item',
            hideInMenu:true
          },
          {
            path: '/result/list/add',
            name: '新增结果',
            component: './Result/Add',
            hideInMenu:true
          },
          {
            path: '/result/list/edit/:id',
            name: '修改结果',
            component: './Result/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '大赛简介管理',
        icon: 'icon-zhidao',
        path: '/abstract',
        routes: [
          {
            path: '/abstract',
            name: '简介详情',
            component: './Abstract',
          },
          {
            path: '/abstract/edit/:id',
            name: '修改简介',
            component: './Abstract/Edit',
            hideInMenu:true
          },
        ]
      },
      {
        name: '大赛规则管理',
        icon: 'icon-guize',
        path: '/rules',
        routes: [
          {
            path: '/rules/list',
            name: '规则列表',
            component: './Rules/List',
          },
          {
            path: '/rules/list/item/:id',
            name: '规则详情',
            component: './Rules/Item',
            hideInMenu:true
          },
          {
            path: '/rules/list/add',
            name: '新增规则',
            component: './Rules/Add',
            hideInMenu:true
          },
          {
            path: '/rules/list/edit/:id',
            name: '修改规则',
            component: './Rules/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '参赛须知管理',
        icon: 'icon-zuzhi',
        path: '/attention',
        routes: [
          {
            path: '/attention',
            name: '参赛须知详情',
            component: './Attention',
          },
          {
            path: '/attention/edit',
            name: '修改参赛须知',
            component: './Attention/Edit',
            hideInMenu:true
          },
        ]
      },
      {
        name: '用户管理',
        icon: 'icon-yonghu',
        path: '/users',
        routes: [
          {
            path: '/users/list',
            name: '用户列表',
            component: './Users/List',
          },
          {
            path: '/users/list/reset/:id',
            name: '重置密码',
            component: './Users/Reset',
            hideInMenu:true
          },
        ],
      },
      {
        name: '角色管理',
        icon: 'icon-juese',
        path: '/roles',
        routes: [
          {
            path: '/roles/list',
            name: '角色列表',
            component: './Roles/List',
          },
          {
            path: '/roles/list/item/:id',
            name: '角色详情',
            component: './Roles/Item',
            hideInMenu:true
          },
          {
            path: '/roles/list/add',
            name: '新增角色',
            component: './Roles/Add',
            hideInMenu:true
          },
          {
            path: '/roles/list/edit/:id',
            name: '修改角色',
            component: './Roles/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '管理员管理',
        icon: 'icon-Management',
        path: '/manager',
        routes: [
          {
            path: '/manager/list',
            name: '管理员列表',
            component: './Manager/List',
          },
          {
            path: '/manager/list/reset/:id',
            name: '重置密码',
            component: './Manager/Reset',
            hideInMenu:true
          },
          {
            path: '/manager/list/add',
            name: '新增管理员',
            component: './Manager/Add',
            hideInMenu:true
          },
          {
            path: '/manager/list/edit/:id',
            name: '修改管理员',
            component: './Manager/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '广告管理',
        icon: 'icon-shebei',
        path: '/advert',
        component: './Advert',
      },
      {
        name: '视频管理',
        icon: 'video-camera',
        path: '/videos',
        routes: [
          {
            path: '/videos/list',
            name: '视频列表',
            component: './Videos/List',
          },
          {
            path: '/videos/list/add',
            name: '新增视频',
            component: './Videos/Add',
            hideInMenu:true
          },
          {
            path: '/videos/list/edit/:id',
            name: '修改视频',
            component: './Videos/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '赞助商管理',
        icon: 'icon-ziliaobaosong',
        path: '/sponsors',
        routes: [
          {
            path: '/sponsors/list',
            name: '赞助商列表',
            component: './Sponsors/List',
          },
          {
            path: '/sponsors/list/add',
            name: '新增赞助商',
            component: './Sponsors/Add',
            hideInMenu:true
          },
          {
            path: '/sponsors/list/edit/:id',
            name: '修改赞助商',
            component: './Sponsors/Edit',
            hideInMenu:true
          },
        ],
      },
      {
        name: '错误',
        icon: 'warning',
        path: '/exception',
        hideInMenu: true,
        routes: [
          {
            path: '/exception/403',
            name: '抱歉，你无权访问该页面',
            component: './Exception/403',
          },
          {
            path: '/exception/404',
            name: '抱歉，你访问的页面不存在',
            component: './Exception/404',
          },
          {
            path: '/exception/500',
            name: '抱歉，服务器出错了',
            component: './Exception/500',
          },
        ],
      },
      {
        component: '404',
      },
    ],
  },
];
