<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改用户基础信息</p>
    </header>
    <section class="modal-card-body">
      <div class="columns m-2">
        <div class="column is-8">
          <b-field
            label="用户名"
            message="用户名不支持修改">
            <b-input disabled :value="userInfo.username==undefined? '' : userInfo.username" placeholder="用户" expanded></b-input>
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
          <b-field label="个人主页">
            <b-input name="space" v-model="userInfo.userSpace" placeholder="个人主页" expanded>
            </b-input>
          </b-field>
          <b-field label="身份认证"
                   message="请转至'高级信息'处修改">
            <b-input disabled name="token" v-model="userInfo.userRole" expanded>
            </b-input>
          </b-field>
        </div>
        <div class="column" style="text-align: center">
          <div class="columns">
            <div class="column">
              <figure class="image">
                <img class="is-rounded" :src="cutAvatar">
              </figure>
            </div>
          </div>
          <div class="not-display-mobile">
            <ImgCutter
              :boxWidth="500"
              :boxHeight="400"
              rate="1:1"
              file-type="png"
              :imgMove="true"
              v-on:cutDown="cutDown">
              <template #open>
                <button class="button is-warning is-light">选择图片</button>
              </template>
            </ImgCutter>
          </div>
        </div>
      </div>
    </section>
    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        @click="submitNew"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import ImgCutter from 'vue-img-cutter'
import {post} from '@/untils/request'
import {failTips, successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "updateUserBase",
  components: {ImgCutter},
  props: ["newUserInfo"],
  data() {
    return {
      oldUserAvatar:"",
      cutAvatar:""
    }
  },
  computed:{
    userInfo(){
      if (this.newUserInfo.length==1){
        let newUserinfo={
          id:this.newUserInfo[0].id,
          username:this.newUserInfo[0].username,
          userEmail:this.newUserInfo[0].userEmail,
          userRole:this.newUserInfo[0].userRole,
          isBan:this.newUserInfo[0].isBan,
          userAvatarPath:this.newUserInfo[0].userAvatarPath,
          userTele:this.newUserInfo[0].userTele,
          userSignature:this.newUserInfo[0].userSignature,
          userSpace:this.newUserInfo[0].userSpace
        }
        return newUserinfo
      }
      else{
        return {
          id:"ERROR",
          username:"ERROR",
          userEmail:"ERROR",
          userRole:"ERROR",
          isBan:"ERROR",
          userAvatarPath:"ERROR",
          userTele:"ERROR",
          userSignature:"ERROR",
          userSpace:"ERROR",
        }
      }
    }
  },
  mounted() {
    this.oldUserAvatar=this.userInfo.userAvatarPath
    this.cutAvatar=this.userInfo.userAvatarPath
  },
  methods: {
    cutDown(file) {
      this.userInfo.userAvatarPath = file.dataURL
      this.cutAvatar=file.dataURL
    },
    submitNew() {
      if (this.userInfo.userAvatarPath == this.oldUserAvatar) {
        this.userInfo.userAvatarPath = null
      }
      post("/img2d/userCenter/loggedUser/updateBaseInfo",this.userInfo)
        .then(({data}) => {
          if (data.flag) {
            successTips("更新成功!!!")
            this.$emit("finish")
          } else {
            failTips("数据更新失败，请检查是否登录")
          }
        })
        .catch(() => {
          failTips("数据更新失败，请检查是否登录")
        })
    },
  },
}
</script>

<style scoped>

</style>
