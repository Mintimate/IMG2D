<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改图片标签</p>
    </header>
    <section class="modal-card-body">
      <b-field label="修改或添加标签">
        <b-taginput
          v-model="tagNames"
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
  name: "updateImageTags",
  props: ['checkItem'],
  data(){
    return{
      newImageInfo:{
        id:"",
        tagNames:[]
      },
      imgId:"",
      tagNames:[]
    }
  },
  mounted() {
    if (this.checkItem.length==1){
      this.imgId=this.checkItem[0].id
      for (let tag in this.checkItem[0].tagDTOList){
        this.tagNames.push(this.checkItem[0].tagDTOList[tag].tagName)
      }
    }
  },
  methods:{
    updateImageTagsInfo() {
      this.newImageInfo.id=this.imgId
      this.newImageInfo.tagNames=this.tagNames
      post("/img2d/img/admin/updateImage/tagNames", this.newImageInfo).then(({data})=>{
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
