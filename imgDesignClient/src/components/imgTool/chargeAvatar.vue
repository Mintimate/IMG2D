<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">欣赏Ta，支持Ta创作</p>
    </header>
    <section class="modal-card-body">
      <b-field>
        <div class="buttons pr-2">
          <b-button
            :type="tradeRecord.price==100?'is-warning':'is-normal'"
            @click="tradeRecord.price=100"
            label="100积分"/>
          <b-button
            :type="tradeRecord.price==300?'is-warning':'is-normal'"
            @click="tradeRecord.price=300"
            label="300积分"/>
          <b-button
            :type="tradeRecord.price==500?'is-warning':'is-normal'"
            @click="tradeRecord.price=500"
            label="500积分"/>
        </div>
      </b-field>
      <b-field message="自定义积分~">
        <b-input placeholder="自定义"
                 v-model="tradeRecord.price"
                 type="number"
                 min="1"
                 :max="userHasBalance">
        </b-input>
      </b-field>
      <b-field>
        <p>目前您拥有：{{userHasBalance}}积分</p>
      </b-field>
      <b-field>
        <p>充电将消耗：{{tradeRecord.price}}积分</p>
      </b-field>
    </section>
    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        label="充电"
        @click="makeCharge"
        :disabled="submitDisabled"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";
import {get,post} from "@/untils/request"

export default {
  name: "chargeAvatar",
  props: ["avatarId"],
  data(){
    return{
      userHasBalance:"0",
      tradeRecord:{
        payerId:"",
        payeeId:"",
        price:100
      },
    }
  },
  methods:{
    makeCharge(){
      post("/img2d/userBalance/chargeAuthor",this.tradeRecord)
      .then(({data})=>{
        if (data.flag){
          successTips("充电成功，期待作者的后续作品吧～")
        }
        else{
          failTips("充电失败：请检查积分是否足够(･_･;")
        }
        this.$emit("close")
      })
    }
  },
  mounted() {
    this.tradeRecord.payeeId=this.avatarId
    console.log(this.tradeRecord)
    if(!this.$store.state.logged){
      warningTips("请先登录，才可以给作者充电嗷")
      this.$emit("close")
    }
    else{
      get("/img2d/userBalance/getLatest").then(({data})=>{
        this.userHasBalance=data.data.balance
      })
    }
  },
  computed:{
    submitDisabled:function (){
      if (this.tradeRecord.price>=1&&this.tradeRecord.price<=this.userHasBalance){
        return false;
      }
      else{
        return true;
      }
    }
  }
}
</script>

<style scoped>

</style>
