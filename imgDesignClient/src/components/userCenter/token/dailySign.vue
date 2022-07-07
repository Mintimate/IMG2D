<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">每日签到</p>
    </header>
    <section class="modal-card-body">
      <b-field style="text-align: center">
        <b-datepicker
          class="is-centered"
          expanded
          inline
          v-model="date"
          :events="events"
          :min-date="new Date()"
          :max-date="new Date()"
          indicators="bars"
        >
        </b-datepicker>

      </b-field>
    </section>
    <footer class="modal-card-foot">
      <b-button
        :disabled="isDisabled"
        @click="makeTodaySign()"
        label="签到"
        type="is-primary"
        expanded
        rounded
        size="is-small"
      />
    </footer>
  </div>
</template>

<script>
import {get, post} from "@/untils/request"
import {successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "dailySign",
  data() {
    return {
      isDisabled: false,
      date: new Date(),
      events: [],
    }
  },
  created() {
    // const thisMonth = new Date().getMonth()
    this.getMonthSign()
    // console.log(thisMonth)
  },
  methods: {
    getMonthSign() {
      const thisMonth = new Date().getMonth()
      const thisYear = new Date().getFullYear()
      const today = new Date().getDate()
      get("/img2d/userBalance/getDailySign").then(({data}) => {
        if (data.flag) {
          let dateList = data.data.slice(0, today)
          for (let len = (dateList + "").length; len < today; len = dateList.length) {
            dateList = "0" + dateList;
          }
          for (let [index, value] of dateList.split("").entries()) {
            if (value == 1) {
              if ((index + 1) == today) {
                this.isDisabled = true
              }
              this.events.push({
                date: new Date(thisYear, thisMonth, index + 1),
                type: 'is-success'
              })
            }
          }
        }
      })
    },
    makeTodaySign() {
      post("/img2d/userBalance/makeDailySign").then(({data}) => {
        if (data.flag) {
          successTips(data.data)
          this.$emit("chargeFinish")
          this.$emit("close")
        }
      })
    },
  },
  computed: {
    submitDisabled: function () {
      return true;
    }
  }
}
</script>

<style scoped>

</style>
