<template>
  <div class="column">
    <div class="columns">
      <div class="column tile is-ancestor">
        <div class="tile is-vertical">
          <div class="tile">
            <div class="tile is-parent is-vertical">
              <article class="tile is-child box">
                <b-steps
                  rounded
                  v-model="activeStep"
                  :has-navigation="false"
                >
                  <b-step-item :clickable="false" label="卡片会员" icon="account-key">
                    <div class="section">
                      <div class="columns is-centered">
                        <div class="column">
                          <b-field>
                            <b-checkbox :value="true"
                                        disabled
                                        type="is-success">
                              恭喜🎉您，成功注册本站会员。
                            </b-checkbox>
                          </b-field>
                          <p>
                            这相当于您刚拿到"卡片机"，可以浏览本站绝大多数内容和体验绝大多数功能
                          </p>
                          <p>
                            如果您还想往上提升，您可以：
                          </p>
                          <b-field class="mt-3">
                            <b-checkbox :value="false"
                                        disabled
                                        type="is-warning">
                              累计积分达到一万 => 数码会员
                            </b-checkbox>
                          </b-field>
                          <b-field class="mt-3">
                          <b-checkbox :value="false"
                                      disabled
                                      type="is-warning">
                            累计积分达到十万 => 单反会员
                          </b-checkbox>
                        </b-field>
                        </div>
                      </div>
                    </div>
                  </b-step-item>
                  <b-step-item  :clickable="false" label="数码会员" icon="account">
                    <div class="section">
                      <div class="columns is-centered">
                        <div class="column">
                          <b-field>
                            <b-checkbox :value="true"
                                        disabled
                                        type="is-success">
                              恭喜🎉您，成功晋级为本站会员"数码会员"
                            </b-checkbox>
                          </b-field>
                          <p>
                            这相当于您买了台数码相机，虽然不如单反光学相机，但是可以满足绝大多数情况下的需求。
                            您上传的图片、文章将优先审核，甚至直接AI快速审核。
                          </p>
                          <p>
                            如果您还想往上提升，您可以：
                          </p>
                          <b-field class="mt-3">
                            <b-checkbox :value="true"
                                        disabled
                                        type="is-warning">
                              累计积分达到一万 => 数码会员
                            </b-checkbox>
                          </b-field>
                          <b-field class="mt-3">
                            <b-checkbox :value="false"
                                        disabled
                                        type="is-warning">
                              累计积分达到十万 => 单反会员
                            </b-checkbox>
                          </b-field>
                        </div>
                      </div>
                    </div>
                  </b-step-item>
                  <b-step-item :clickable="false" label="单反会员" icon="account-plus">
                    <div class="section">
                      <div class="columns is-centered">
                        <div class="column">
                          <b-field>
                            <b-checkbox :value="true"
                                        disabled
                                        type="is-success">
                              恭喜🎉您，成功晋级为本站会员"单反会员"
                            </b-checkbox>
                          </b-field>
                          <p>
                            这意味着，您已经是本站的顶级VIP，您上传的图片还需要审核？那一定是旷世奇作！！！
                            AI检查没有涉及政治和颜色后无需审核。本站所有功能您可以随意使用，并有24*7小时客服。
                          </p>
                          <p>
                            如果您还想往上提升，已经没有什么可以做的了：
                          </p>
                          <b-field class="mt-3">
                            <b-checkbox :value="true"
                                        disabled
                                        type="is-warning">
                              累计积分达到一万 => 数码会员
                            </b-checkbox>
                          </b-field>
                          <b-field class="mt-3">
                            <b-checkbox :value="true"
                                        disabled
                                        type="is-warning">
                              累计积分达到十万 => 单反会员
                            </b-checkbox>
                          </b-field>
                        </div>
                      </div>
                    </div>

                  </b-step-item>
                </b-steps>
                <div class=" level">
                  <div class="level-item has-text-centered">
                    <div>
                      <p class="title">{{userBalance.historyBalance}}</p>
                      <p class="heading">历史累计</p>
                    </div>
                  </div>
                  <div class="level-item has-text-centered">
                    <div>
                      <p class="title">{{userBalance.balance}}</p>
                      <p class="heading">当前积分</p>
                    </div>
                  </div>
                  <div class="level-item p-1">
                    <b-button
                      @click="showChargeToken=!showChargeToken"
                      expanded
                      rounded
                      type="is-danger"
                      size="is-medium"
                      label="钞能力"
                    />
                    <b-modal v-model="showChargeToken" width="25rem">
                      <recharge-token
                        @close="showChargeToken=false"
                        @submit="handlerToken"
                        @chargeFinish='getLatestToken' />
                    </b-modal>
                  </div>
                  <div class="level-item p-1">
                    <b-button
                      @click="showDailySign=!showDailySign"
                      expanded
                      rounded
                      type="is-primary"
                      size="is-medium"
                      label="签到"/>
                    <b-modal v-model="showDailySign" width="25rem">
                      <daily-sign
                        @close="showDailySign=false"
                        @chargeFinish='getLatestToken' />
                    </b-modal>
                  </div>
                </div>
              </article>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import rechargeToken from "@/components/userCenter/token/rechargeToken"
import dailySign from "@/components/userCenter/token/dailySign"
import {successTips} from "@/untils/tipsInfoUntil";
import {get,post} from "@/untils/request";

export default {
  name: "userVIP",
  components:{rechargeToken,dailySign},
  data(){
    return{
      showChargeToken:false,
      showDailySign:false,
      userBalance:{
        historyBalance:"--",
        balance:"--"
      },
      activeStep:0
    }
  },
  mounted() {
    this.$nextTick(this.getLatestToken())
  },
  methods:{
    getLatestToken(){
      get("/img2d/userBalance/getLatest").then(({data})=>{
        this.userBalance=data.data
        if (Number(this.userBalance.historyBalance)>100000){
          this.activeStep=2
        }
        else if (Number(this.userBalance.historyBalance)>10000){
          this.activeStep=1
        }
      })
    },
    handlerToken(newValue){
      this.showChargeToken=false
      successTips("请在新窗口内完成支付宝的支付(*≧ω≦)")
      post("/img2d/alipay/pay",newValue).then(({data})=>{
        if(data.flag){
          // console.log(data.data)
          window.open(data.data)
        }
      })
      this.$buefy.dialog.confirm({
        message: '已经完成支付？现在获取最新数据？',
        cancelText:'并没有',
        confirmText:'已支付',
        onConfirm: () => {
          successTips('正在获取最新数据')
          this.getLatestToken()
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
