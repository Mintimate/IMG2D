<template>
  <b-navbar-dropdown
      aria-role="list"
      position="is-bottom-left"
      right
  >
    <template #label>
      <img :src="$store.state.loggedUserAvatarPath" class="is-rounded"/>
    </template>
    <template v-if="$store.state.role=='SuperAdmin'">
      <b-navbar-item
          tag="router-link"
          :to="{ path: '/audit' }">
        审核中心
      </b-navbar-item>
    </template>
    <b-navbar-item
        tag="router-link"
        :to="{ path: '/userCenter' }">
      个人中心
    </b-navbar-item>
    <b-navbar-item
        tag="router-link"
        :to="{path:'/account/updateIMG'}">
      上传图片
    </b-navbar-item>
    <b-navbar-item
      tag="router-link"
      :to="'/userSpace/'+theUserId">
      个人星球
    </b-navbar-item>
    <b-navbar-item @click="logOut ">
      退出登录
    </b-navbar-item>
  </b-navbar-dropdown>
</template>

<script>
import {successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "userCenterButton",
  data() {
    return {
      isPublic: true,
      username: "Demo",
      avatarIMG: require("@/assets/loading/Loading_avatar.jpg"),
    }
  },
  methods: {
    logOut() {
      this.$store.commit("clearUser")
      successTips("期待您的下次登录⁄(⁄ ⁄ ⁄ω⁄ ⁄ ⁄)⁄")
      this.$nextTick(() => {
        this.$router.replace({path: "/"});
      })
    },
    WaitingDev() {
      this.$buefy.toast.open({
        duration: 5000,
        message: `嘿嘿，被你发现了～这个功能还在制作～`,
        type: 'is-warning',
        pauseOnHover: true
      })
    }
  },
  computed: {
    theUserId(){
      return this.$store.state.loggedUserId
    },
  },
}
</script>

<style scoped>
.is-rounded {
  border-radius: 48px;
}
</style>
