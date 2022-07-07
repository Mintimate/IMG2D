<template>
  <div class="column">
    <div class="columns">
      <div class="column tile is-ancestor">
        <div class="tile is-vertical">
          <div class="tile">
            <div class="tile is-parent is-vertical">
              <article class="tile is-child box">
                <b-field
                  label="电话绑定"
                  message="电话绑定后，你可以使用短信登录系统">
                  <p class="subtitle control" v-if="userInfo.userTele==''||userInfo.userTele==undefined">未设置</p>
                  <p class="subtitle control" v-else>{{ userInfo.userTele }}</p>
                </b-field>
                <button class="button is-primary is-small is-rounded"
                        @click="cardModal('Tele')">
                  {{ userInfo.userTele == '' || userInfo.userTele == undefined ? "绑定" : "修改" }}
                </button>
                <b-modal v-model="changeTeleCardModalActive" width="25rem">
                  <tele-form v-on:updateFinish="updateFinish" />
                </b-modal>
              </article>
              <article class="tile is-child box">
                <b-field
                  label="QQ绑定"
                  message="QQ绑定后，你可以使用QQ一键登录系统">
                  <p class="subtitle control" v-if="userInfo.userQqOpenid==''||userInfo.userQqOpenid==undefined">未绑定</p>
                  <p class="subtitle control" v-else>已经绑定</p>
                </b-field>
                <QQForm v-on:updateFinish="updateFinish"/>
              </article>
            </div>
            <div class="tile is-parent is-vertical">
              <article class="tile is-child box">
                <b-field
                  class="mb-6 pb-2"
                  label="密码修改"
                  message="如果你习惯密码登录，并想修改密码">
                </b-field>
                <button class="button is-danger is-small is-rounded"
                        @click="cardModal('Passwd')">
                  修改密码
                </button>
                <b-modal v-model="changePasswordCardModalActive" width="25rem">
                  <change-password v-on:updateFinish="updateFinish" />
                </b-modal>
              </article>
              <article class="tile is-child box">
                <b-field
                  label="账号认证"
                  message="专业用户，专属认证铭牌">
                  <p class="subtitle control">{{userInfo.userRole==''||userInfo.userRole==undefined ? '未认证':userInfo.userRole}}</p>
                </b-field>
                <button class="button is-black is-small is-rounded"
                        @click="cardModal('Ident')">
                  申请认证
                </button>
                <b-modal v-model="changeIdentCardModalActive" width="25rem">
                  <ident v-on:updateFinish="updateFinish"/>
                </b-modal>
              </article>

            </div>
          </div>
          <div class="tile is-parent">
            <article class="tile is-child box">
              <!-- Put any content you want -->
            </article>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import teleForm from "@/components/userCenter/changeSeniorInfo/teleForm"
import changePassword from "@/components/userCenter/changeSeniorInfo/changePassword"
import QQForm from "@/components/userCenter/changeSeniorInfo/QQForm"
import ident from "@/components/userCenter/changeSeniorInfo/ident"
import {get} from "@/untils/request"
export default {
  name: "thirdLoginInfo",
  components:{teleForm,QQForm,changePassword,ident},
  data() {
    return {
      userInfo: {
        userTele: null,
        userQqOpenid:null,
        userRole:null
      },
      changeTeleCardModalActive:false,
      changePasswordCardModalActive:false,
      changeIdentCardModalActive:false
    }
  },
  mounted() {
    this.initMyData()
  },
  methods:{
    initMyData(){
      get("/img2d/userCenter/loggedUser/seniorInfo")
      .then(({data})=>{
        if (data.flag){
          this.userInfo=data.data
        }
      })
    },

    updateFinish(Mode){
      if (Mode=="Finish"){
        this.initMyData()
      }
      this.changeTeleCardModalActive=false
      this.changePasswordCardModalActive=false
      this.changeIdentCardModalActive=false
    },

    cardModal(value) {
      if (value=="Tele") {
        this.changeTeleCardModalActive = true
      }
      else if(value=="Passwd"){
        this.changePasswordCardModalActive=true
      }
      else {
        this.changeIdentCardModalActive=true
      }
    }
  },

}
</script>

<style scoped>

</style>
