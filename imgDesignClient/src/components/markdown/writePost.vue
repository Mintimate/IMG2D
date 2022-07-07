<template>
  <v-md-editor :include-level="[1,2,3]"
               :disabled-menus="[]"
               v-model="markdownSource"
               @upload-image="handleUploadImage"
               height="35rem">
  </v-md-editor>
</template>

<script>
import {uploadFile} from "@/untils/tencentCOS"
export default {
  name: "writePost",
  props:['post'],
  data(){
    return {
      markdownSource:""
    }
  },
  methods:{
    wait(ms) {
      return new Promise(resolve =>setTimeout(()=>resolve(), ms));
    },
    async handleUploadImage(event, insertImage, files) {
      for(let i=0 ;i<files.length;i++){
        const file=files[i]
        const filenameWithTime=new Date().getTime()+file.name
        const url=uploadFile(file,filenameWithTime)
        this.$nextTick(
          await this.wait(3000),
          insertImage({
            url: encodeURI(url),
            desc: file.name,
            // width: 'auto',
            // height: 'auto',
          })
        )

      }
    },
  },
  watch:{
    markdownSource:function (newValue){
      if (newValue!=this.show){
        this.$emit("save",newValue)
      }
    },
    post: {
      handler(newVal) {
        this.markdownSource = newVal
      },
      deep:true,
      immediate:true
    },
  }
}
</script>

<style scoped>

</style>
