<template>
  <section class="is-vcentered is-centered">
    <div class="box">
      <div class="columns">
        <div class="column is-8 is-offset-2 ">
          <div class="container mb-6">
            <div class="columns">
              <div class="column">
                <h3 class="is-size-3 has-text-weight-bold mb-2 has-text-centered">用户注册</h3>
                <div class="box">
                  <section>
                    <b-loading :is-full-page="false" v-model="isLoading" :can-cancel="true"></b-loading>
                    <b-steps
                        v-model="activeStep"
                        mobile-mode="minimalist">
                      <b-step-item style="height: 12rem" step="1" label="设置账户">
                        <b-field
                            label="设置邮箱"
                            :type="tips.emailState"
                            :message="tips.emailTips"
                        >
                          <b-input
                              v-model="newUser.userEmail"
                              maxlength="30">
                          </b-input>
                        </b-field>
                        <b-field
                            label="设置用户名"
                            type="password"
                            message="请设置用户名（该用户名作为唯一识别，不可重复）"
                        >
                          <b-input
                              v-model="newUser.username"
                          >
                          </b-input>
                        </b-field>
                      </b-step-item>

                      <b-step-item style="height: 12rem" step="2" label="设置密码"
                      >
                        <b-field
                            label="请设置密码"
                            :type="tips.passwordState"
                            :message="tips.passwordTips"
                        >
                          <b-input type="password"
                                   v-model="newUser.password"
                                   maxlength="25"
                                   password-reveal>
                          </b-input>
                        </b-field>
                        <b-field
                            label="请再输入一遍密码"
                            :type="tips.verifyPasswordState"
                            :message="tips.verifyPasswordTips"
                        >
                          <b-input type="password"
                                   v-model="VerifyPasswd"
                                   password-reveal>
                          </b-input>
                        </b-field>
                      </b-step-item>

                      <b-step-item style="height: 12rem" :step="3" label="确认信息" disabled>
                        <b-field
                            label="用户名："
                            :type="tips.checkUsernameState"
                            :message="tips.checkUsernameTips"
                        >
                          <b-input disabled
                                   v-model="newUser.username"
                          >
                          </b-input>
                        </b-field>
                        <b-field
                            label="邮箱："
                            :type="tips.checkEmailState"
                            :message="tips.checkEmailTips"
                        >
                          <b-input disabled
                                   v-model="newUser.userEmail"
                          >
                          </b-input>
                        </b-field>
                      </b-step-item>

                      <template
                          #navigation="{previous, next}">
                        <b-button
                            outlined
                            :disabled="previous.disabled"
                            @click.prevent="previous.action">
                          上一步
                        </b-button>
                        <b-button
                            outlined
                            type="is-success"
                            :disabled="next.disabled"
                            @click.prevent="next.action">
                          下一步
                        </b-button>
                      </template>
                    </b-steps>
                  </section>
                  <div class="box">
                    <b-button
                        expanded
                        outlined
                        :disabled="!readySubmit"
                        type="is-success"
                        @click="handleSubmit">
                      提交
                    </b-button>
                  </div>
                  <div class="field">
                    <div class="control has-text-centered">
                      <router-link class="has-text-weight-bold" to="/login">已经用账户？点此登录:-)</router-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </section>
</template>

<script>
import {post,get} from "@/untils/request";
import {successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "userRegister",
  data() {
    return {
      hasHandler:false,
      // 新用户信息
      newUser: {
        userEmail: "",
        username: "",
        password: ""
      },
      tips: {
        // 邮件验证
        emailTips: "该邮箱将作为登录账号。",
        emailState: "",
        // 密码验证
        passwordTips: "密码进行多次加密，请放心设置。",
        passwordState: "",
        // 确认密码验证
        verifyPasswordTips: "请在此确认输入的密码",
        verifyPasswordState: "",
        checkUsernameTips: "",
        checkUsernameState: "",
        checkEmailTips: "",
        checkEmailState: "",
      },
      activeStep: 0,
      userEmailInput: "",
      VerifyPasswd: "",
      rememberME: "不要记住我嗷",
      isLoading: false
    }
  },
  watch: {
    'newUser.userEmail': function () {
      this.checkEmail()
    },
    'newUser.password': function () {
      this.checkPassword()
    },
    VerifyPasswd: function () {
      this.checkPassword()
    },
    activeStep: function (before) {
      if (before == 2 && this.newUser.userEmail != "") {
        this.checkRepeat()
      }
    }
  },
  methods: {
    checkEmail() {
      const regEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
      if (regEmail.test(this.newUser.userEmail)) {
        // 合法的邮箱
        this.tips.emailState = "is-success";
        this.tips.emailTips = "邮箱格式合法嗷";
      } else {
        this.tips.emailState = "is-danger";
        this.tips.emailTips = "邮箱格式不合法嗷";
      }
    },
    checkPassword() {
      const regPasswd = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,25}$/;
      if (regPasswd.test(this.newUser.password)) {
        // 合法的密码
        this.tips.passwordState = "is-success";
        this.tips.passwordTips = "密码强度符合要求";
      } else {
        this.tips.passwordState = "is-danger";
        this.tips.passwordTips = "密码必须包含大小写字母和数字，可使用特殊字符";
      }
      if (this.newUser.password != this.VerifyPasswd) {
        this.tips.verifyPasswordState = "is-warning";
        this.tips.verifyPasswordTips = "两次密码不一致，请检查";
      } else {
        this.tips.verifyPasswordState = "is-success";
        this.tips.verifyPasswordTips = "Good~两次密码都一样";
      }
    },
    handleSubmit() {
      this.isLoading = true
      post("/img2d/userCenter/register", this.newUser)
          .then(({data})=> {
                if (data.flag) {
                  successTips("请前往邮箱内完成注册验证操作")
                  this.hasHandler=true
                }
          })
      this.isLoading = false;
    },
    checkRepeat() {
      this.isLoading = true;
      get("/img2d/userCenter/CheckUser/"+this.newUser.username+"/"+this.newUser.userEmail)
          .then(response => {
            if (response.data == 0) {
              this.tips.checkEmailState = "is-success"
              this.tips.checkUsernameState = "is-success"
              this.tips.checkUsernameTips = "用户名可用。"
              this.tips.checkEmailTips = "用户邮箱可用。"
            } else if (response.data == 1) {
              this.tips.checkUsernameState = "is-warning"
              this.tips.checkUsernameTips = "用户名已存在，请重新取名。"
            } else if (response.data == 2) {
              this.tips.checkEmailState = "is-warning"
              this.tips.checkEmailTips = "用户邮箱已经存在，请重新选择邮箱。"
            } else {
              this.tips.checkEmailState = "is-warning"
              this.tips.checkUsernameState = "is-warning"
              this.tips.checkUsernameTips = "用户名已存在，请重新取名。"
              this.tips.checkEmailTips = "用户邮箱已经存在，请重新选择邮箱。"
            }
          })
      this.isLoading = false
    }
  },
  computed: {
    readySubmit() {
      if (this.hasHandler==true){
        return false;
      }
      if (this.activeStep != 2 || Object.values(this.tips).indexOf('') != -1)
        return false;
      if (Object.values(this.tips).indexOf('is-danger') != -1 || Object.values(this.tips).indexOf('is-warning') != -1)
        return false;
      return true;
    }
  }
}
</script>

<style scoped>

</style>
