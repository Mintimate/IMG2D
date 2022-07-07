<template>
  <b-field>
    <b-upload
        v-model="imgFile"
        drag-drop
        expanded
        accept=".png, .jepg, .jpg, .webp, .bmp"
    >
      <section class="section">
        <div class="content has-text-centered">
          <p>
            <b-icon icon="upload" size="is-large"></b-icon>
          </p>
          <p>将图片拖到到此处，或点击此处选择图片</p>
          <p>.jpg、.png、.webp、.bmp</p>
          <div class="tags" style="text-align: center;display: inherit">
            <p v-if="imgFile!=null" class="tag is-primary is-large">
              <i class="iconfont icon-photo  mr-2"></i>
              {{ imgFile.name }}
            </p>
          </div>
        </div>
      </section>
    </b-upload>
  </b-field>
</template>

<script>
export default {
  name: "choseIMG",
  props:['dropFiles'],
  data(){
    return{
      imgFile:null
    }
  },
  watch:{
    imgFile(After) {
      if (After != null) {
        this.$emit("update:dropFiles",After)
      }
    },
    dropFiles(After){
      if (After == null) {
        this.imgFile=null
      }
    }
  },
}
</script>

<style scoped>

</style>