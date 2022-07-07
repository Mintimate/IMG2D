<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">更改密码</p>
    </header>
    <section class="modal-card-body">
      <form>
        <b-field label="请输入旧密码">
          <b-input
            type="password"
            v-model="oldPassword"
            password-reveal
            placeholder="请输入旧的密码"
            required>
          </b-input>
        </b-field>
        <b-field label="请输入新密码">
          <b-input
            type="password"
            v-model="newPassword"
            password-reveal
            placeholder="请输入新的密码"
            required>
          </b-input>
        </b-field>
        <b-field label="确认新密码"
                 :type="replyPasswordMessage.length!=0?'is-danger':''"
                 :message="replyPasswordMessage">
          <b-input
            type="password"
            password-reveal
            v-model="replyPassword"
            placeholder="请重复输入新的密码"
            required>
          </b-input>
        </b-field>
      </form>
    </section>
    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('updateFinish')"
      />
      <b-button
        :disabled="submitDisabled"
        @click="handlerChangePassword"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import {post} from "@/untils/request";
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";

export default {
  name: "changePassword",
  data() {
    return {
      oldPassword: "",
      replyPassword: "",
      newPassword: "",
    }
  },
  computed: {
    submitDisabled() {
      if (this.oldPassword.length == 0 || this.replyPassword.length == 0 || this.newPassword.length == 0) {
        return true;
      }
      if (this.replyPassword != this.newPassword) {
        return true;
      }
      return false;
    },
    replyPasswordMessage() {
      if (this.replyPassword != this.newPassword) {
        return "两次密码不一样哦，请确认新密码和确认密码。";
      }
      return "";
    }
  },
  methods: {
    handlerChangePassword() {
      if (this.newPassword.length<8){
        warningTips("新密码最少需要8位哦")
        return
      }
      let Password={
        "oldPassword":this.oldPassword,
        "newPassword":this.newPassword
      }
      post("/img2d/userCenter/loggedUser/updateUserPassword",Password).then(({data}) => {
        if (data.flag) {
          successTips("密码更新成功～")
        } else {
          failTips("密码更新失败，请检查原密码是否正确")
        }
      })
      this.$emit("updateFinish")
    }
  }
}
</script>

<style scoped>

</style>
