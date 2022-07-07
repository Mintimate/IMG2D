<template>
  <section>
    <div v-if="userIdentApply.length==0">
      <b-field class="box p-0">
        <b-message type="is-black">
          真不错，空空如也；没有用户提交认证申请。
        </b-message>
      </b-field>
    </div>
    <div v-else>
      <b-table
        :data="userIdentApply"
        ref="table"
        paginated
        per-page="10"
        detailed
        show-detail-icon
        aria-next-label="Next page"
        aria-previous-label="Previous page"
        aria-page-label="Page"
        checkable
        :checked-rows.sync="checkItems"
        checkbox-position="left"
        aria-current-label="Current page">

        <b-table-column field="id" label="用户ID" sortable v-slot="props" centered>
          {{ props.row.userId }}
        </b-table-column>

        <b-table-column field="申请认证" label="申请认证" sortable v-slot="props" centered>
          {{ props.row.identWhat }}
        </b-table-column>

        <template #detail="props">
          <article class="media">
            <img :src="props.row.identURL">
          </article>
        </template>


        <template #bottom-left>
          <div class="field is-grouped">
            <p class="control">
              <button
                @click="judgeIdents"
                class="button is-info">
                审核通过
              </button>
            </p>
            <p class="control">
              <button
                @click="refuseIdents"
                class="button is-warning">
                拒绝通过
              </button>
            </p>
          </div>
        </template>
      </b-table>
    </div>
  </section>
</template>

<script>
import {get, post} from "@/untils/request"

export default {
  name: "auditUserIdent",
  data() {
    return {
      userIdentApply: [],
      checkItems: []
    }
  },
  created() {
    this.getUserIdents()
  },
  methods: {
    getUserIdents() {
      get("/img2d/userIdent/getIdents").then(({data}) => {
        if (data.flag) {
          this.userIdentApply = data.data
        }
      })
    },
    refuseIdents() {
      post("/img2d/userIdent/refuseIdent", this.checkItems).then(({data}) => {
        if (data.flag) {
          this.getUserIdents()
        }
      })
    },
    judgeIdents() {
      post("/img2d/userIdent/judgeIdent", this.checkItems).then(({data}) => {
        if (data.flag) {
          this.getUserIdents()
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
