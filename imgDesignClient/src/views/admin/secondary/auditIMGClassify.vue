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

      <b-table-column field="user.last_name" label="创建用户" sortable v-slot="props">
        {{ props.row.createBy }}
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
            <button  @click="addCate" class="button is-info">
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
  name: "auditIMGClassify",
  data(){
    return{
      classifyList:[
        {
          id:"",
          cateName:"",
          createBy:"",
          createTime:""
        }
      ],
      newCate:{
        cateName:null
      }
    }
  },
  methods:{
    getInfo(){
      get("/img2d/imgCate/getCateDetail")
      .then(({data})=>{
        this.classifyList=data.data
      })
    },
    addCate(){
      post("/img2d/imgCate/addImgCate",this.newCate)
      .then(this.getInfo)
    }
  },
  mounted() {
    this.getInfo()
  }
}
</script>

<style scoped>

</style>
