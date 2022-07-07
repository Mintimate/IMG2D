import Vue from 'vue'
import VueRouter from 'vue-router'
import myIndex from '../views/index/myIndex.vue'
import {warningTips} from "@/untils/tipsInfoUntil";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    meta: {
      title: "专攻图片，极致体验--IMG2D"
    },
    component: myIndex
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: "登录◡ ヽ(`Д´)ﾉ ┻━┻--IMG2D"
    },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/user/userLogin.vue')
  },
  {
    path: '/register',
    name: 'register',
    meta: {
      title: "注册账号--IMG2D"
    },
    component: () => import('@/views/user/userRegister.vue')
  },
  {
    path: '/checkData/:workMode/:value',
    name: 'checkInfo',
    meta: {
      title: "校验信息--IMG2D"
    },
    component: () => import('@/views/info/checkData.vue')
  },
  {
    path: '/userSpace/:userId',
    name: 'UserSpace',
    meta: {
      title: "用户个人空间--IMG2D"
    },
    component: () => import('@/views/user/userSpace/space.vue')
  },
  {
    path: '/userSpace/:userId/:type',
    name: 'UserSpaceDetail',
    meta: {
      title: "用户个人空间详情--IMG2D"
    },
    component: () => import('@/views/user/userSpace/spaceDetail.vue')
  },
  {
    path: '/getIMGByClassify/:classifyId?',
    name: 'getIMGByClassify',
    meta: {
      title: "分类--IMG2D"
    },
    component: () => import('@/views/imageHost/GetIMGByClassify.vue')
  },
  {
    path: '/getIMGByTag/:tag?',
    name: 'getIMGByClassify',
    meta: {
      title: "图片标签--IMG2D"
    },
    component: () => import('@/views/imageHost/GetIMGByTag.vue')
  },
  {
    path: '/discussIndex/:page?',
    name: 'discussIndex',
    meta: {
      title: "讨论广场--IMG2D"
    },
    component: () => import('../views/discussCircle/display/discussIndex.vue')
  },
  {
    path: '/findArticle/:articleType',
    name: 'discussIndex',
    props: true,
    meta: {
      title: "分类查看文章--IMG2D"
    },
    component: () => import('../views/discussCircle/display/findArticle.vue')
  },
  {
    path: '/searchArticle',
    name: 'searchArticle',
    meta: {
      title: "搜索文章--IMG2D"
    },
    component: () => import('@/views/discussCircle/display/searchResult.vue')
  },
  {
    path: '/discuss/newPost',
    name: "writeNewPost",
    meta: {
      title: "发送新文章--IMG2D"
    },
    component: () => import('@/views/discussCircle/post/writeNewPost')
  },
  {
    path: '/discuss/fixPost/:articleId?',
    name: "reWritePost",
    meta: {
      title: "重新编辑文章--IMG2D"
    },
    component: () => import('@/views/discussCircle/post/reWritePost')
  },
  {
    path: '/imgDetail/:imgID',
    name: 'imgDetail',
    meta: {
      title: "图片详情--IMG2D"
    },
    component: () => import('../views/imageHost/getIMGDetail.vue'),
    props: true
  },
  {
    path: '/postDetail/:postID',
    name: 'post',
    meta: {
      title: "文章详情--IMG2D"
    },
    component: () => import('@/views/discussCircle/post/postDetail.vue'),
    props: true
  },
  {
    path: '/processIMG',
    name: 'processIMG',
    meta: {
      title: "图片处理--IMG2D"
    },
    component: () => import('../views/processIMG.vue')
  },
  {
    path: '/audit',
    name: 'audit',
    component: () => import('../views/admin/audit.vue'),
    children: [
      {
        path: '/',
        component: () => import('../views/admin/secondary/auditInterface.vue'),
        meta: {
          title: "系统设置--管理员"
        },
      },
      {
        path: 'auditIMG',
        component: () => import('../views/admin/secondary/auditIMG.vue'),
        meta: {
          title: "图片审核--管理员"
        },
      },
      {
        path: 'auditIMGClassify',
        component: () => import('../views/admin/secondary/auditIMGClassify.vue'),
        meta: {
          title: "图片分类审核--管理员"
        },
      },
      {
        path: 'auditPost',
        component: () => import('../views/admin/secondary/auditPost.vue'),
        meta: {
          title: "文章审核--管理员"
        },
      },
      {
        path: 'auditPostCategory',
        component: () => import('../views/admin/secondary/auditPostCategory.vue'),
        meta: {
          title: "文章分类管理--管理员"
        },
      },
      {
        path: 'auditUser',
        component: () => import('../views/admin/secondary/auditUser.vue'),
        meta: {
          title: "用户修改--管理员"
        },
      },
      {
        path: 'auditUserIdent',
        component: () => import('../views/admin/secondary/auditUserIdent.vue'),
        meta: {
          title: "用户认证请求--管理员"
        },
      },
      {
        path: 'auditImageComment',
        component: () => import('../views/admin/secondary/auditCommentImage.vue'),
        meta: {
          title: "图片评论管理--管理员"
        },
      },
      {
        path: 'auditArticleComment',
        component: () => import('../views/admin/secondary/auditCommentArticle.vue'),
        meta: {
          title: "文章评论管理--管理员"
        },
      },
    ],
  },
  {
    path: '/userCenter',
    component: () => import('../views/user/userCenter.vue'),
    children: [
      {
        path: '/',
        component: () => import('../components/userCenter/userVIP'),
        meta: {
          title: "用户会员中心--IMG2D"
        },
      },
      {
        path: 'userVIP',
        component: () => import('../components/userCenter/userVIP'),
        meta: {
          title: "用户会员中心--IMG2D"
        },
      },
      {
        path: 'baseInfo',
        component: () => import('../components/userCenter/baseInfo'),
        meta: {
          title: "用户中心--IMG2D"
        },
      },
      {
        path: 'thirdLogin',
        component: () => import('../components/userCenter/thirdLoginInfo'),
        meta: {
          title: "第三方登录绑定--IMG2D"
        },
      },
      {
        path: 'userHistory',
        component: () => import('../components/userCenter/userHistory'),
        meta: {
          title: "上传历史--IMG2D"
        },
      },
      {
        path: 'alertInfo',
        component: () => import('../components/userCenter/alertInfo'),
        meta: {
          title: "限时信息提醒--IMG2D"
        },
      },
    ]
  },
  {
    path: "/account/updateIMG",
    name: "updateIMG",
    component: () => import('../views/userUploadIMG')
  },
  {
    path: "/test",
    name: "test",
    component: () => import('../components/markdown/writePostSideBar')
  }
]
const router = new VueRouter({
  mode: 'history',
  routes
})
//设置页面tittle
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next();
})
router.beforeEach((to, from, next) => {
  const after = ["getIMGByClassify","writeNewPost","searchArticle"];
  if (after.indexOf(to.name) != -1) {
    if (localStorage.getItem("UserToken") != null) {
      next()
    } else {
      warningTips("请先登录嗷◡ ヽ(`Д´)ﾉ ┻━┻")
      next("/login")
    }
  } else
    next()
})
export default router
