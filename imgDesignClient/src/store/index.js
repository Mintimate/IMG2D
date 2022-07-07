import Vue from 'vue'
import Vuex from 'vuex'
import Storage from "@/untils/storage"//引入

Vue.use(Vuex)
let apiHost

switch (process.env.NODE_ENV) {
  /**
   * 半弃用：Nginx使用反代理、开发使用nodejs的目录代理；代理后端API为/img2d开头
   */
  case 'development':
    apiHost = "http://localhost:8088"  //开发环境url
    break
  case 'production':
    apiHost = "/img2d"  //生产环境url
    break
}

// Token-持久化验证
const USER_KEY = 'UserToken'
const USER_ROLE = 'UserRole'
const USER_NAME = 'UserName'
const USER_AVATAR_PATH = 'UserAvatarPath'
const USER_ID = 'UserId'

export default new Vuex.Store({
  state: {
    // 通过 || 设定，如果localStorage没有数据，就返回null空对象
    /**
     * Token
     */
    logged: Storage.get(USER_KEY),
    /**
     * 角色等级
     */
    role: Storage.get(USER_ROLE),
    /**
     * 用户昵称
     */
    loggedUserName: Storage.get(USER_NAME),
    /**
     * 用户头像地址
     */
    loggedUserAvatarPath:Storage.get(USER_AVATAR_PATH),
    /**
     * 用户ID
     */
    loggedUserId:Storage.get(USER_ID),
    // 后端API获取数据
    apiHost: apiHost
  },
  mutations: {
    // Token-修改更新用户信息
    updateUser(state, data) {
      // 1. vuex做更新，使得有响应式
      state.logged = data.userToken
      state.role = data.userRole
      state.loggedUserName = data.username
      state.loggedUserAvatarPath="/img2d"+data.userAvatarPath
      state.loggedUserId=data.id
      // 2. localStorage做持久更新
      Storage.set(USER_KEY, state.logged, 1000*60*60*24*2)
      Storage.set(USER_ROLE, state.role,1000*60*60*24*2)
      Storage.set(USER_NAME, state.loggedUserName,1000*60*60*24*2)
      Storage.set(USER_AVATAR_PATH, state.loggedUserAvatarPath,1000*60*60*24*2)
      Storage.set(USER_ID,state.loggedUserId,1000*60*60*24*2)

    },
    // Token-清除用户信息
    clearUser(state) {
      // 1. vuex做清除，使得有响应式
      state.logged = null
      state.role = null
      state.loggedUserName = null
      state.loggedUserAvatarPath=null
      // 2. localStorage持久化清除
      localStorage.removeItem(USER_KEY)
      localStorage.removeItem(USER_ROLE)
      localStorage.removeItem(USER_NAME)
      localStorage.removeItem(USER_AVATAR_PATH)
    }
  },
  actions: {},
  modules: {},
})
