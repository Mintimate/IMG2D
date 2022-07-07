<template>
  <div class="column">
    <div class="columns m-2">
      <div class="column is-8">
        <b-field
            label="用户名"
            message="用户名不支持修改">
          <b-input disabled :value="userInfo.username" placeholder="用户" expanded></b-input>
        </b-field>
        <b-field
            label="个性签名"
            message="自定义的你个性签名吧～">
          <b-input name="signature" v-model="userInfo.userSignature" placeholder="个性签名"
                   expanded></b-input>
        </b-field>
        <b-field label="电子邮箱">
          <b-input name="email" :value="userInfo.userEmail" placeholder="电子邮箱" expanded></b-input>
        </b-field>
        <b-field label="个人主页"
        :type="InputType.userSpaceType"
        :message="InputType.userSpaceMessage">
          <b-input name="space" v-model="userInfo.userSpace" placeholder="个人主页" expanded></b-input>
        </b-field>
        <b-field label="身份认证"
        message="请转至'账号认证'处修改">
          <b-input disabled name="token" v-model="userInfo.userRole" placeholder="内测用户" expanded></b-input>
        </b-field>
      </div>
      <div class="column" style="text-align: center">
        <div class="columns">
          <div class="column">
            <img width="200rem" class="user-avatar" :src="userInfo.userAvatarPath">
          </div>
        </div>
        <div class="not-display-mobile">
          <ImgCutter
              :boxWidth="500"
              :boxHeight="400"
              rate="1:1"
              file-type="png"
              :imgMove="true"
              :toolBoxOverflow="true"
              v-on:cutDown="cutDown">
            <button class="button is-warning is-light" slot="open">选择图片</button>
          </ImgCutter>
        </div>
      </div>
    </div>
    <div class="columns">
      <div class="column">
        <button :disabled="submitButton" @click="submitNew" class="button is-primary is-light is-rounded is-fullwidth">
          提交修改
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ImgCutter from 'vue-img-cutter'
import {get,post} from "@/untils/request"
import {successTips,failTips} from "@/untils/tipsInfoUntil";

export default {
  name: "baseInfo",
  components: {ImgCutter},
  data() {
    return {
      submitButton: true,
      show: false,
      userInfoOld: {},
      userInfo: {
        username: "",
        userEmail: "",
        userSignature:"",
        userTele: "",
        userSpace: "",
        userRole: "",
        userAvatarPath: ""
      },
      InputType:{
        userSpaceType:"",
        userSpaceMessage:"个人主页，可以为博客地址、个人作品主页" ,
        userTeleType:"",
        userSignatureInfo:"自定义你的个性签名吧～",
        userTeleMessage:"目前支持中国大陆的手机号"
      }
    }
  },
  mounted() {
    this.initUserInfo()
  },
  watch: {
    userInfo: {
      handler() {
        if (JSON.stringify(this.userInfo) == JSON.stringify(this.userInfoOld))
          this.submitButton = true
        else {
          if (!this.isUrl(this.userInfo.userSpace)){
            this.InputType.userSpaceType="is-danger"
            this.InputType.userSpaceMessage="URL格式不正确，请重新输入"
            this.submitButton=true
          }
          else {
            this.InputType.userSpaceType="is-success"
            this.InputType.userSpaceMessage="该值可以嗷(*≧ω≦)"
            this.submitButton = false
          }
        }
      },
      deep: true
    },

  },
  methods: {
    initUserInfo(){
      get("/img2d/userCenter/loggedUser/baseInfo")
          .then(({data}) => {
            if(data.flag){
              this.userInfo=data.data
              this.userInfo.userAvatarPath="/img2d"+this.userInfo.userAvatarPath
              this.userInfoOld = Object.assign({}, this.userInfo);
            }
          })
      .catch(()=>{
        failTips("芜湖，出错了……")
        this.$router.replace({path: "/"});
      })
    },
    cutDown(file) {
      this.imgFile = file
      this.userInfo.userAvatarPath = this.imgFile.dataURL
    },
    submitNew() {
      if(this.userInfo.userAvatarPath==this.userInfoOld.userAvatarPath){
        this.userInfo.userAvatarPath=null
      }
      post( "/img2d/userCenter/loggedUser/updateBaseInfo",
          {
            username: this.userInfo.username,
            userSpace: this.userInfo.userSpace,
            userAvatarPath: this.userInfo.userAvatarPath,
            userSignature: this.userInfo.userSignature
          })
          .then(({data}) => {
            if (data.flag){
              successTips("数据更新完成，载入新的数据")
              this.$store.commit("updateUser", data.data)
            }
            else{
              failTips("数据更新失败，请检查是否登录")
            }
          })
          .catch(() => {
            this.$buefy.toast.open({
              duration: 5000,
              message: `修改失败，请联系客服`,
              type: 'is-danger',
              pauseOnHover: true
            })
          })
      .finally(this.initUserInfo)
    },
    isUrl (url) {
      return url!="" ? /^(?:(http|https|ftp):\/\/)?((?:[\w-]+\.)+[a-z0-9]+)((?:\/[^/?#]*)+)?(\?[^#]+)?(#.+)?$/.test(url) : true
    }
  }
}
</script>

<style scoped>
@media screen and (max-width: 975px) {
  .not-display-mobile {
    display: none;
  }
}
.user-avatar {
  border-radius: 99999px;
  border: 1px solid #636363;
}
</style>
