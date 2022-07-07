<template>
  <div>
    <markdown-post :post-source="postText"/>
  </div>
</template>

<script>
import markdownPost from "@/components/postCenter/markdownPost"
import {get} from "@/untils/request"
import {backToSnowKey} from "@/untils/snowKey";

export default {
  name: "postDetail",
  components: {markdownPost},
  props: ["postID"],
  data() {
    return {
      postText: {
        id:"",
        userId:"",
        categoryName:"",
        articleBanner:"",
        articleTitle:"",
        articleDesc:"",
        articleContent: "",
        type:"",
        originalUrl: "",
        createTime:"",
        updateTime:"",
        tagNameList:[],
      },
    }
  },
  created() {
    get("/img2d/article/getArticleDetail/" + backToSnowKey(this.postID))
      .then(({data}) => {
        if (data.flag) {
          this.postText=data.data
        }
      })
  },
}
</script>

<style scoped>

</style>
