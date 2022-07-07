<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改图片分类</p>
    </header>
    <section class="modal-card-body">
      <b-field label="修改图片分类">
        <b-select
          v-model="newImageInfo.categoryId"
          placeholder="选择一个新的分类">
          <option
            v-for="item in cateList"
            :value="item.id"
            :key="item.id">
            {{ item.cateName }}
          </option>
        </b-select>
      </b-field>
    </section>

    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        @click="updateImagesCateInfo"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import {post, get} from "@/untils/request";

export default {
  name: "updatePostCate",
  props: ['checkItem'],
  data() {
    return {
      newImageInfo: {
        id: "",
        categoryId: ""
      },
      cateList: [
        // {
        //   id: "",
        //   cateName: ""
        // }
      ]
    }
  },
  mounted() {
    if (this.checkItem.length == 1) {
      console.log(this.checkItem[0])
      this.newImageInfo.id = this.checkItem[0].id
      this.newImageInfo.cateId = this.checkItem[0].cateId
      get("/img2d/articleCategory/getCategoryList").then(({data}) => {
        data.data.forEach(item=>{
          if (item.cateName==this.checkItem[0].cateName){
            this.newImageInfo.categoryId=item.id
          }
          this.cateList.push({
            id:item.id,
            cateName:item.cateName
          })
        })
      })
    }
  },
  methods: {
    updateImagesCateInfo() {
      post("/img2d/article/admin/updatePost/cate", this.newImageInfo).then(({data}) => {
        if (data.flag) {
          this.$emit("success", data.data)
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
