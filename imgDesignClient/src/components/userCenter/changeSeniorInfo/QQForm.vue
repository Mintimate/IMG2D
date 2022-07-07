<template>
  <button class="button is-info is-small is-rounded"
          @click="LoginClick('qq')">
    绑定/更换绑定
  </button>
</template>
<script>
import {post} from "@/untils/request"
import Storage from "@/untils/storage"
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";

export default {
  name: "QQForm",
  data() {
    return{}
  },
  methods: {
    LoginClick(value) {
      if (value == "qq") {
        let state = Date.now()
        Storage.remove("QQ_ID")
        const width = width || 900;
        const height = height || 540;
        const left = (window.screen.width - width) / 2;
        const top = (window.screen.height - height) / 2;
        const win = window.open(
          "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101995772&redirect_uri=https%3A%2F%2Fimg2d.flyinbug.cn/OAuth/bandingQQ&state=" + state,
          "_blank",
          "toolbar=yes, location=yes, directories=no, status=no, menubar=yes," +
          " scrollbars=yes, resizable=no, copyhistory=yes, left=" +
          left + ", top=" + top + ", width=" + width + ", height=" + height);
        let _this = this
        //监听登录窗口是否关闭,登录成功后 后端返回关闭窗口的代码
        let listener = setInterval(function () {
          //监听窗口是否关闭
          if (win.closed) {
            //进入这个if代表后端验证成功!直接跳转路由
            clearInterval(listener);//关闭监听
            //跳转路由
            let QQ_ID = Storage.get("QQ_ID")
            if (QQ_ID == undefined || QQ_ID == "") {
              warningTips("操作取消(･_･;")
              return
            }
            else if(QQ_ID=="A113"){
              failTips("所选QQ已经被其他用户绑定(*≧ω≦)")
              return
            }
            post("/img2d/userCenter/loggedUser/updateQQ/"+ QQ_ID)
              .then(({data}) => {
                if (data.flag) {
                  successTips("验证成功，之后可以用该QQ登录啦")
                }
                else {
                  failTips("操作失败～～")
                }
                _this.$emit('updateFinish', 'Finish')
              })
          }
        }, 500)
      }
    },
  }
}
</script>

<style scoped>

</style>
