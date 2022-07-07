<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改文章标题</p>
    </header>
    <section class="modal-card-body">
      <b-field label="修改文章标题">
        <b-input
          v-model="newImageInfo.articleTitle"/>
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
import {post} from "@/untils/request";

export default {
  name: "updatePostName",
  props: ['checkItem'],
  data() {
    return {
      newImageInfo: {
        id: "",
        articleTitle: "",
      },
      imgNameLast:"",
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
      this.newImageInfo.id = this.checkItem[0].id
      this.newImageInfo.articleTitle = this.checkItem[0].articleTitle
    }
  },
  methods: {
    updateImagesCateInfo() {
      post("/img2d/article/admin/updatePost/articleTitle", this.newImageInfo).then(({data}) => {
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
