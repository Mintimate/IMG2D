<template>
  <div id="app">
    <div class="myNav">
      <b-navbar
        class="is-spaced"
        :fixed-top=true
      >
        <template #brand>
          <b-navbar-item
            tag="router-link"
            :to="{ path: '/' }">
            <img
              :src="logoIMG"
              alt="IMG2D"
            >
          </b-navbar-item>
        </template>
        <template #start>
          <b-navbar-item tag="router-link"
                         :to="{path:'/'}">
            主页
          </b-navbar-item>
          <b-navbar-dropdown label="图库">
            <b-navbar-item
              tag="router-link"
              :to="{ path: '/getIMGByClassify' }"
            >
              按分类
            </b-navbar-item>
            <b-navbar-item
              tag="router-link"
              :to="{path:'/getIMGByTag'}">
              按标签
            </b-navbar-item>
          </b-navbar-dropdown>
          <b-navbar-item
            tag="router-link"
            :to="{path:'/processIMG'}"
          >
            在线加工
          </b-navbar-item>
          <b-navbar-item
            tag="router-link"
            :to="{ path: '/discussIndex' }"
          >
            小圈
          </b-navbar-item>
        </template>
        <template #end>
          <template v-if="!logged">
            <div class="buttons">
              <b-navbar-item tag="div">
                <b-button
                  tag="router-link"
                  :to="{ path: '/register' }"
                  icon-left="account-plus-outline"
                  type="is-dark"
                  class="button is-light"
                  label="注册"
                />
                <b-button
                  tag="router-link"
                  :to="{ path: '/login' }"
                  icon-left="account"
                  class="button is-light"
                  label="登录"
                />
              </b-navbar-item>
            </div>
          </template>
          <template v-else>
            <div class="bell-not-display-mobile">
              <b-tooltip :label="'您有'+alertInfoCount+'条消息需要确认'"
                         position="is-bottom"
                         :animated="false">
                <b-navbar-item tag="router-link"
                               :to="{path:'/userCenter/alertInfo'}">
                  <b-icon
                    icon="bell"></b-icon>
                  <span v-show="alertInfoCount!=0"
                        class="info-badge">{{ alertInfoCount }}</span>
                </b-navbar-item>
              </b-tooltip>
            </div>
            <user-center-button></user-center-button>
          </template>
        </template>
      </b-navbar>
    </div>
    <!--    <my-index/>-->
    <section class="section">
      <router-view></router-view>
    </section>
    <footer class="footer ">
      <div class="content has-text-centered">
        <p>
          <strong>IMG2D</strong> Power by <a href="https://www.mintimate.cn">Mintimate</a>.<br/>
          The Online Tool,Help You Deal With IMG,And Discuss Them.
        </p>
      </div>
    </footer>
  </div>
</template>
<script>
import userCenterButton from "@/components/userCenterButton"
import {get} from "@/untils/request"

export default {
  name: "App",
  components: {userCenterButton},
  data() {
    return {
      logoIMG: require("@/assets/logo/newLogo.png"),
      alertInfoCount: 0
    }
  },
  methods: {
    getUserAlertInfo() {
      get("/img2d/userAlertInfo/getHavenReadInfo").then(({data}) => {
        this.alertInfoCount = data.data
      })
    }
  },
  computed: {
    logged: function () {
      if (this.$store.state.logged == null)
        return false;
      else {
        this.getUserAlertInfo()
        return true;
      }
    }
  }
}
</script>
<style scoped>
section {
  padding: 1rem 0.2rem;
}

@media screen and (min-width: 1024px) {
  section {
    padding: 1rem 3rem;
  }
}

@media screen and (max-width: 1024px) {
  .bell-not-display-mobile {
    display: none;
  }
}

nav {
  backdrop-filter: blur(3px);
  background-color: rgba(255, 255, 255, 0.6);
}

.navbar.is-spaced {
  padding: 0.3rem 0.5rem;
}

.info-badge {
  position: absolute;
  left: 50%;
  top: 5px;
  z-index: 2;
  box-sizing: border-box;
  padding: 0 5px;
  min-width: 16px;
  height: 16px;
  border-radius: 8px;
  font-size: 12px;
  line-height: 16px;
  text-align: center;
  color: #fff;
  background-color: #00a4ff;
  font-weight: 500;
  font-family: Helvetica Neue;
}
</style>
