<template>
  <section>
    <div class="columns is-centered">
      <div class="column">
        <div class="columns  is-centered">
          <div class="column is-one-quarter">
            <div class="border-radius">
              <router-link
                to="/userCenter/userVIP"
                :class="choseButton=='userVIP'?'is-light':'is-ghost'"
                class="button is-ghost is-fullwidth">我的会员
              </router-link>
              <router-link
                to="/userCenter/baseInfo"
                :class="choseButton=='baseInfo'?'is-light':'is-ghost'"
                class="button is-fullwidth">基本资料
              </router-link>
              <router-link
                to="/userCenter/thirdLogin"
                :class="choseButton=='thirdLogin'?'is-light':'is-ghost'"
                class="button is-ghost is-fullwidth">账号设置
              </router-link>
              <router-link
                to="/userCenter/userHistory"
                :class="choseButton=='userHistory'?'is-light':'is-ghost'"
                class="button is-fullwidth">上传历史
              </router-link>
              <router-link
                to="/userCenter/alertInfo"
                :class="choseButton=='alertInfo'?'is-light':'is-ghost'"
                class="button is-fullwidth">消息提醒
              </router-link>
            </div>
          </div>
          <router-view>
          </router-view>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "userCenter",
  data(){
    return{
      choseButton:"baseInfo"
    }
  },
  mounted() {
    let path=this.$route.path.split("/")
    this.changeButton(path[2])
  },
  watch:{
    '$route.path':{
      handler:function (newVal){
        this.changeButton(newVal.split("/")[2])
      }
    }
  },
  methods: {
    WaitingDev() {
      this.$buefy.toast.open({
        duration: 5000,
        message: `嘿嘿，被你发现了～这个功能还在制作～`,
        type: 'is-warning',
        pauseOnHover: true
      })
    },
    changeButton(newChose) {
      if(newChose==undefined){
        newChose="userVIP"
      }
      this.choseButton = newChose
    }
  }
}
</script>

<style scoped>
.card {
  border-radius: 12px
}
.border-radius {
  border-radius: 12px;
  /*border: 1px solid;*/
  border-image: linear-gradient(45deg, gold, deeppink) 1;
  clip-path: inset(0px round 10px);
  animation: huerotate 6s infinite linear;
  filter: hue-rotate(360deg);
}

@keyframes huerotate {
  0% {
    filter: hue-rotate(0deg);
  }
  100% {
    filter: hue-rotate(360deg);
  }
}

</style>
