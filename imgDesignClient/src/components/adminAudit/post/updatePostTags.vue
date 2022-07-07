<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改文章标签</p>
    </header>
    <section class="modal-card-body">
      <b-field label="修改或添加标签">
        <b-taginput
          v-model="tagNameList"
          ellipsis
          type="is-warning"
          icon="label"
          maxtags="6"
          placeholder="删除或添加"
          aria-close-label="Delete this tag">
        </b-taginput>
      </b-field>
    </section>

    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        @click="updateImageTagsInfo"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import {post} from "@/untils/request";

export default {
  name: "updatePostTags",
  props: ['checkItem'],
  data(){
    return{
      newPostInfo:{
        id:"",
        tagNameList:[]
      },
      imgId:"",
      tagNameList:[]
    }
  },
  mounted() {
    if (this.checkItem.length==1){
      this.imgId=this.checkItem[0].id
      for (let tag in this.checkItem[0].tagDTOList){
        this.tagNameList.push(this.checkItem[0].tagDTOList[tag].tagName)
      }
    }
  },
  methods:{
    updateImageTagsInfo() {
      this.newPostInfo.id=this.imgId
      this.newPostInfo.tagNameList=this.tagNameList
      post("/img2d/article/admin/updatePost/tagNames", this.newPostInfo).then(({data})=>{
        if (data.flag){
          this.$emit("success",data.data)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
