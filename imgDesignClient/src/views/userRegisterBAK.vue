<template>
  <div class="is-vcentered is-centered">
    <div class="box mt-6">
      <div class="columns">
        <div class="column is-8 is-offset-2 ">
          <div class="container mb-6">
            <div class="columns">
              <div class="column">
                <h3 class="is-size-3 has-text-weight-bold mb-2 has-text-centered">用户注册</h3>
                <div class="box">
                  <form>
                    <b-field
                        label="Email"
                        :type="userEmailInput"
                        message="请输入注册账户邮箱。">
                      <b-input type="email"
                               v-model="userEmail"
                               maxlength="30">
                      </b-input>
                    </b-field>
                    <b-field
                        label="Password"
                        :type="userPasswdInput"
                        message="密码进行多次加密，请放心设置。"
                    >
                      <b-input type="password"
                               v-model="userPasswd"
                               password-reveal>
                      </b-input>
                    </b-field>
                    <b-field
                        label="Verify Your Password"
                        :type="userPasswdInput"
                        message=""
                    >
                      <b-input type="password"
                               v-model="userPasswd"
                               password-reveal>
                      </b-input>
                    </b-field>
                    <div class="buttons mt-3">
                      <b-button type="is-primary" expanded>注册</b-button>
                    </div>
                    <div class="field">
                      <div class="control has-text-centered">
                        <a class="has-text-weight-bold" href="/users/password/new">已经用账户？点此登录:-)</a>
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
export default {
  name: "userRegister",
  data() {
    return {
      userEmailInput:"",
      userEmail:"",
      userPasswdInput:"",
      userPasswd:"",
      rememberME: "不要记住我嗷",
    }
  }
}
</script>

<style scoped>

</style>