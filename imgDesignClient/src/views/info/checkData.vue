<template>
  <section style="min-height: 50vh" class="mt-6 mb-6">
    <div
      :type="isSuccess?'is-success':'is-warning'"
      class="card mt-6 mb-6"
    >
      <header class="card-header">
        <p class="card-header-title">
          {{returnResult.header}}
        </p>
      </header>
      <div class="card-content">
        <div class="content has-text-centered">
          <p>{{ returnResult.body1 }}</p>
          <p>{{ returnResult.body2 }}</p>
        </div>
      </div>
      <footer class="card-footer">
        <router-link to="/login" class="card-footer-item">登录</router-link>
      </footer>
    </div>
  </section>
</template>

<script>
import {post} from "@/untils/request";

export default {
  name: "register.vue",
  data() {
    return {
      isSuccess: true,
      checkParameter: {
        parameter1: "",
        parameter2: ""
      },
      returnResult: {
        header: "",
        body1: "",
        body2: ""
      }
    }
  },
  mounted() {
    this.checkParameter.parameter1 = this.$route.params.workMode
    this.checkParameter.parameter2 = this.$route.params.value
    this.checkRegister()
  },
  methods: {
    checkRegister() {
      post("/img2d/userCenter/registerCheck/" + this.checkParameter.parameter2)
        .then(({data}) => {
          if (data.flag) {
            this.returnResult.header = "恭喜，账号创建完成，账号信息如下(･_･;"
            this.returnResult.body1 = "邮件：" + data.data.userEmail
            this.returnResult.body2 = "用户名：" + data.data.username

          }
          else {
            this.returnResult.header = "账号创建失败，可能原因："
            this.returnResult.body1 = "1. 可能是该链接无效"
            this.returnResult.body2 = "2. 用户已经注册"
          }
        })
    }
  }
}
</script>

<style scoped>

</style>
