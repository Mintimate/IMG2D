<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改图片昵称</p>
    </header>
    <section class="modal-card-body">
      <b-field label="修改图片昵称（无法更改图片格式）">
        <b-input
          v-model="newImageInfo.imgName"/>
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
  name: "updateImageName",
  props: ['checkItem'],
  data() {
    return {
      newImageInfo: {
        id: "",
        imgName: "",
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
      this.newImageInfo.imgName = this.checkItem[0].imgName.substring(0, this.checkItem[0].imgName.lastIndexOf("."))
      this.imgNameLast="."+this.checkItem[0].imgName.split('.').pop().toLowerCase()
    }
  },
  methods: {
    updateImagesCateInfo() {
      this.newImageInfo.imgName=this.newImageInfo.imgName+this.imgNameLast
      post("/img2d/img/admin/updateImage/imgName", this.newImageInfo).then(({data}) => {
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
