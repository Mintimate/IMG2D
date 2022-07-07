<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">用户高级信息修改</p>
    </header>
    <section class="modal-card-body">
      <b-field label="用户">
        <b-input disabled :value="newUserinfo.username"></b-input>
      </b-field>
      <b-field label="用户手机号">
        <b-input v-model="newUserinfo.userTele"></b-input>
      </b-field>
      <b-field label="用户电子邮箱">
        <b-input v-model="newUserinfo.userEmail"></b-input>
      </b-field>
      <b-field label="选择认证的身份"
               class="is-fullwidth">
        <b-select
          v-model="newUserinfo.userRole"
          placeholder="选择认证的身份">
          <option value="RegisteredUser">注册用户</option>
          <option value="摄影师">摄影师</option>
          <option value="艺术家">艺术家</option>
          <option value="灵魂画师">灵魂画师</option>
        </b-select>
      </b-field>
    </section>

    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        label="提交"
        @click="uploadSeniorInfo"
        type="is-primary"/>
    </footer>
  </div>

</template>

<script>
import {post} from "@/untils/request";
import {successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "updateUserSenior",
  props:["userinfo"],
  data(){
    return{
      newUserinfo:{
        id: "",
        username: "",
        userEmail: "",
        userRole: "",
        isBan: "",
        userAvatarPath: "",
        userTele: "",
        userSignature: "",
        userSpace: ""
      }
    }
  },
  watch:{
    userinfo:{
      handler(newValue){
        if (newValue!=undefined){
          this.newUserinfo=newValue
        }
      },
      immediate:true
    }
  },
  methods:{
    uploadSeniorInfo(){
      post("/img2d/userCenter/admin/updateSeniorInfo/"+this.newUserinfo.userRole,this.newUserinfo)
      .then(({data})=>{
        if (data.flag){
          successTips("修改成功，获取最新数据")
          this.$emit("finish")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
