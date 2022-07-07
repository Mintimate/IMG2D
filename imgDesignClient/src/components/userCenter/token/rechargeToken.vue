<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">充值积分</p>
    </header>
    <section class="modal-card-body">
      <b-field>
        <div class="buttons pr-2">
             <b-button
               :type="orderInfo.total_amount==50?'is-warning':'is-normal'"
               @click="orderInfo.total_amount=50"
               label="50¥"/>
          <b-button
            :type="orderInfo.total_amount==100?'is-warning':'is-normal'"
            @click="orderInfo.total_amount=100"
            label="100¥"/>
          <b-button
            :type="orderInfo.total_amount==200?'is-warning':'is-normal'"
            @click="orderInfo.total_amount=200"
            label="200¥"/>

        </div>
        <b-field message="这里可以自定义金额~">
        <b-input placeholder="自定义"
                 v-model="orderInfo.total_amount"
                 type="number"
                 min="1"
                 max="10000">
        </b-input>
        </b-field>
      </b-field>
      <b-field>
        <p>充值将获得：{{orderInfo.total_amount*100}}积分</p>
      </b-field>
    </section>
    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        @click="submitOrderInfo"
        :disabled="submitDisabled"
        label="充值"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
export default {
  name: "rechargeToken",
  data(){
    return{
      orderInfo:{
        subject:"IMG2D代币",
        total_amount:50
      }
    }
  },
  methods:{
    submitOrderInfo(){
      this.$emit("submit",this.orderInfo)
    }
  },
  computed:{
    submitDisabled:function (){
      if (this.orderInfo.total_amount>=1&&this.orderInfo.total_amount<=10000){
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
