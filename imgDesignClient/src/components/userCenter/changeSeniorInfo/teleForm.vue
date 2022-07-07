<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">更改/绑定手机号码</p>
    </header>
    <section class="modal-card-body">
      <b-field label="电话号码">
        <b-input
          v-model="newTelephone"
          placeholder="输入你的电话号码"
          required>
        </b-input>
      </b-field>
      <b-button
        :disabled="!verifyButton.canClick"
        @click="getVerifyCodes"
        size="is-small">
        {{ verifyButton.content }}
      </b-button>
      <div v-show="showVerifyInput">
        <hr/>
        <b-field label="验证码">
          <b-input
            v-model="verifyCodes"
            placeholder="输入你的验证码"
            required>
          </b-input>
        </b-field>
      </div>
    </section>
    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('updateFinish')"
      />
      <b-button
        :disabled="!submitButton"
        @click="handleSubmit"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import {get, post} from "@/untils/request";
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";

export default {
  name: "teleForm",
  data() {
    return {
      verifyButton: {
        content: '发送验证码',   // 按钮里显示的内容
        totalTime: 60,           //记录具体倒计时时间
        canClick: false  //添加canClick
      },
      verifyCodes: "",
      showVerifyInput: false,
      newTelephone: "",
    }
  },
  watch: {
    newTelephone: function (newValue) {
      if (/^1[3-9]\d{9}$/.test(newValue)) {
        this.verifyButton.canClick = true
      } else {
        this.verifyButton.canClick = false
      }
    }
  },
  computed: {
    submitButton() {
      if (this.verifyCodes == "" || this.verifyCodes == null) {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    getVerifyCodes() {
      if (!this.verifyButton.canClick) return   //改动的是这两行代码
      this.verifyButton.canClick = false
      this.verifyButton.content = this.verifyButton.totalTime + 's后可重新发送'
      get("/userCenter/loggedUser/getVerifyCodes", {"telephone": this.newTelephone})
        .then(({data}) => {
          if (data.flag){
            successTips("短信发送成功，5分钟内有效")
          }
          else{
            warningTips("未知错误，请重试")
          }
        })
      this.showVerifyInput = true
      let clock = window.setInterval(() => {
        this.verifyButton.totalTime--
        this.verifyButton.content = this.verifyButton.totalTime + 's后可重新发送'
        if (this.verifyButton.totalTime < 0) {
          window.clearInterval(clock)
          this.verifyButton.content = '重新发送验证码'
          this.verifyButton.totalTime = 60
          this.verifyButton.canClick = true   //这里重新开启
        }
      }, 1000)
    },
    handleSubmit() {
      post("/userCenter/loggedUser/updateTelePhone",
        "telephone=" + this.newTelephone + "&verifyCodes=" + this.verifyCodes)
        .then(({data}) => {
          if (data.flag) {
            successTips("验证成功，之后可以用短信登录啦")
          }
          else {
            failTips("验证码错误，请重新绑定")
          }
        })
        .finally(()=>{
          this.$emit('updateFinish', 'Finish')
        })
    }
  }
}
</script>

<style scoped>

</style>
