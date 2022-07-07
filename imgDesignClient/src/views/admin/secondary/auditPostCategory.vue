<template>
  <section>
    <b-table
      :data="classifyList"
      ref="table"
      paginated
      per-page="10"
      show-detail-icon
      aria-next-label="Next page"
      aria-previous-label="Previous page"
      aria-page-label="Page"
      aria-current-label="Current page">

      <b-table-column field="id" label="ID" width="40" numeric v-slot="props">
        {{ props.row.id }}
      </b-table-column>

      <b-table-column field="user.first_name" label="图片分类" sortable v-slot="props">
        {{ props.row.cateName }}
      </b-table-column>

      <b-table-column field="date" label="创建时间" sortable centered v-slot="props">
                  <span class="tag is-success">
                      {{ new Date(props.row.createTime).toLocaleDateString() }}
                  </span>
      </b-table-column>

      <template #bottom-left>
        <div class="field is-grouped">
          <p class="control is-expanded">
            <input class="input" type="text" v-model="newCate.cateName" placeholder="输入需要添加的分类">
          </p>
          <p class="control">
            <button @click="addCate" class="button is-info">
              添加分类
            </button>
          </p>
          <p class="control">
            <button @click="getInfo" class="button is-warning">
              获取最新数据
            </button>
          </p>
        </div>
      </template>
    </b-table>
  </section>
</template>

<script>
import {get, post} from "@/untils/request"

export default {
  name: "auditPostCategory",
  data() {
    return {
      classifyList: [],
      newCate: {
        cateName: null
      }
    }
  },
  methods: {
    getInfo() {
      get("/img2d/articleCategory/getCategoryList")
        .then(({data}) => {
          if (data.flag) {
            this.classifyList = data.data
          }
        })
      .catch(e=>{
        console.log(e.data)
      })
    },
    addCate() {
      console.log(this.newCate)
      post("/img2d/articleCategory/addCategory", this.newCate)
        .then(({data}) => {
          console.log(data)
          this.getInfo()
        })
    }
  },
  mounted() {
    this.getInfo()
  }
}
</script>

<style scoped>

</style>
