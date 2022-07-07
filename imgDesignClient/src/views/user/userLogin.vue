<template>
  <div class="is-vcentered is-centered">
    <div class="box mt-6">
      <div class="columns">
        <div class="column is-8 is-offset-2 ">
          <div class="container mb-6">
            <div class="columns">
              <div class="column">
                <h3 class="is-size-3 has-text-weight-bold mb-2 has-text-centered">用户登录</h3>
                <div class="box">
                  <form>
                    <b-field
                      label="邮箱/手机号"
                      :type="userEmailInput"
                      message="请输入账户的邮箱或手机号。">
                      <b-input
                        v-model="user.userAccount"
                        maxlength="30">
                      </b-input>
                    </b-field>
                    <b-field
                      label="密码"
                      :type="userPasswdInput"
                      message="请输入注册账户时的密码。"
                    >
                      <b-input type="password"
                               v-model="user.password"
                               password-reveal>
                      </b-input>
                    </b-field>
                    <b-field>
                      <b-tooltip
                        label="哈哈，就是浏览器记住登录啦">
                        <b-switch v-model="rememberME"
                                  size="is-small"
                                  true-value="记住我"
                                  false-value="不要记住我嗷">
                          {{ rememberME }}
                        </b-switch>
                      </b-tooltip>
                    </b-field>

                    <div class="buttons">
                      <Vcode :puzzleScale="0.8" :imgs="Img1" :show="verifyIMG" @success="loginSubmit" @close="onClose"/>
                      <b-button :disabled="disabled" @click="submit" type="is-primary" expanded>登录</b-button>
                    </div>
                    <div class="has-text-right">
                      <div class="buttons">
                        <b-button type="is-info"
                                  size="is-small"
                                  rounded
                                  @click="LoginClick('qq')"
                                  icon-pack="iconfont"
                                  icon-left="icon-qq-circle">
                          <span id="qqLoginBtn"></span>QQ登录
                        </b-button>
                        <b-button type="is-link"
                                  size="is-small"
                                  rounded
                                  @click="smsModal"
                                  icon-pack="iconfont"
                                  icon-left="icon-shouji">
                          短信登录
                        </b-button>
<!--                        <b-button type="is-danger"-->
<!--                                  size="is-small"-->
<!--                                  rounded-->
<!--                                  @click="forgotPassword"-->
<!--                                  icon-pack="iconfont"-->
<!--                                  icon-right="icon-wangjimima">-->
<!--                          忘记密码-->
<!--                        </b-button>-->
                        <b-modal class="has-text-left" v-model="isSmsModalActive" width="25rem">
                          <sms-login v-on:updateFinish="updateFinish"/>
                        </b-modal>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { post} from "@/untils/request"
import {successTips,failTips} from "@/untils/tipsInfoUntil";
import Vcode from "vue-puzzle-vcode";
import smsLogin from "@/components/userEnter/smsLogin"
import Storage from "@/untils/storage";

export default {
  name: "userLogin",
  components: {
    Vcode,
    smsLogin
  },
  data() {
    return {
      verifyIMG: false,
      Img1: [
        require("../../assets/verifyIMG/TinyStart-Small.jpg"),
        require("../../assets/verifyIMG/PathofMiranda_Traning-Small.jpg"),
        require("../../assets/verifyIMG/NoName-Small.jpg"),
        require("../../assets/verifyIMG/poolParty-Small.jpg"),
        require("../../assets/verifyIMG/milkTea-Small.jpg")
      ],
      user: {
        mode: "Email",
        userAccount: "",
        password: "",
      },
      userEmailInput: "",
      userPasswdInput: "",
      isSmsModalActive: false,
      rememberME: "不要记住我嗷",
      thirdLogin: {
        QQ: require("@/assets/login/QQ_login.png")
      }
    }
  },
  mounted() {
    const backState=this.$route.query
    if (backState.state=="success"){
      const userdata={
        username:backState.user,
        userAvatarPath:backState.avatarPath,
        userToken:backState.token,
        userRole:backState.role,
        id:backState.userId,
      }
      if (!Object.values(userdata).some(v=>v==''||v==undefined)){
        successTips("登录成功")
        this.$store.commit("updateUser", userdata)
        this.$nextTick(() => {
          this.$router.replace({path: "/"});
        })
      }
      else{
        return
      }
    }
    else if (backState.state!=undefined){
      failTips("登录失败：使用的QQ并没有绑定任何账号嗷")
    }
  },
  computed: {
    disabled() {
      return this.user.userAccount == "" || this.user.password == ""
    }
  },
  methods: {
    updateFinish() {
      this.isSmsModalActive = false
    },
    LoginClick(value) {
      if (value == "qq") {
        let state=Date.now()
        Storage.remove("QQ_ID")
        // const width = width || 900;
        // const height = height || 540;
        // const left = (window.screen.width - width) / 2;
        // const top = (window.screen.height - height) / 2;
        // const win =window.open(
        //   "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101995772&redirect_uri=https%3A%2F%2Fimg2d.flyinbug.cn/OAuth/qq&state="+state,
        //   "_blank",
        //   "toolbar=yes, location=yes, directories=no, status=no, menubar=yes," +
        //   " scrollbars=yes, resizable=no, copyhistory=yes, left=" +
        //   left + ", top=" + top + ", width=" + width + ", height=" + height);
        window.open(
          "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101995772&redirect_uri=https%3A%2F%2Fimg2d.flyinbug.cn/OAuth/qq&state="+state,
          "_self",);
        // let _this=this
        //监听登录窗口是否关闭,登录成功后 后端返回关闭窗口的代码
        // let listener=setInterval(function(){
        //   //监听窗口是否关闭
        //   if(win.closed){
        //     //进入这个if代表后端验证成功!直接跳转路由
        //     clearInterval(listener);//关闭监听
        //     //跳转路由
        //     let QQ_ID=Storage.get("QQ_ID")
        //     if(QQ_ID==undefined||QQ_ID==""){
        //       _this.$buefy.toast.open({
        //         duration: 5000,
        //         message: `登录失败：操作取消～`,
        //         type: 'is-warning',
        //         pauseOnHover: true
        //       })
        //     } else if(QQ_ID.charAt(QQ_ID.length-1)=="#"){
        //       _this.$buefy.toast.open({
        //         duration: 5000,
        //         message: `登录失败：用户不存在(･_･;`,
        //         type: 'is-danger',
        //         pauseOnHover: true
        //       })
        //     }
        //     else{
        //       get("/img2d/OAuth/loginByQQ",{"openid":QQ_ID})
        //       .then(resp=>{
        //         _this.$store.commit("updateUser", resp.data)
        //         _this.$buefy.toast.open({
        //           duration: 5000,
        //           message: `登录成功，正在跳转到首页～`,
        //           type: 'is-success',
        //           pauseOnHover: true
        //         })
        //         _this.$nextTick(() => {
        //           _this.$router.replace({path: "/"});
        //         })
        //       })
        //       .catch(resp=>{
        //         console.log(resp)
        //         _this.$buefy.toast.open({
        //           duration: 5000,
        //           message: `登录失败～`,
        //           type: 'is-success',
        //           pauseOnHover: true
        //         })
        //       })
        //     }
        //   }
        // },500)
      }
    },
    loginSubmit() {
      post("/img2d/userCenter/loginNormal",this.user).then(({data})=>{
        if(data.flag){
          successTips("登录成功，正在跳转到首页～")
          this.$nextTick(() => {
            this.$router.replace({path: "/"});
          })
          this.$store.commit("updateUser", data.data)
        }
        else{
          failTips("邮箱或密码错误，请重试～")
        }
      })
      this.verifyIMG = false;
    },
    onClose() {
      this.verifyIMG = false;
    },
    submit() {
      this.verifyIMG = true;
    },
    forgotPassword() {
      this.$buefy.toast.open({
        duration: 5000,
        message: `尴尬´д\` ;这部分还没做……请联系：198330181（QQ）`,
        type: 'is-danger',
        pauseOnHover: true
      })
    },
    smsModal() {
      this.isSmsModalActive = true
    }
  },
  watch: {
    'user.userAccount'(AfetrValue) {
      if (/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(AfetrValue)) {
        this.user.mode = "Email"
        this.userEmailInput = "is-success"
      } else if (/^1[3-9]\d{9}$/.test(AfetrValue)) {
        this.user.mode = "Telephone"
        this.userEmailInput = "is-success"
      } else {
        this.userEmailInput = "is-danger"
      }
    }
  }
}
</script>

<style scoped>
@import "https://at.alicdn.com/t/font_2867504_1bdsw63cfa3.css";

.text-center {
  text-align: center;
}
</style>
