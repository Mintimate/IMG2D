<template>
  <div class="mt-3">
    <!--  头部信息-->
    <div class="columns">
      <div class="column is-2-desktop">
        <div class="columns  ">
          <div class="column">
            <b-dropdown expanded>
              <template #trigger>
                <b-button expanded type="is-primary" icon-right="menu-down" label="选择操作"/>
              </template>
              <b-dropdown-item>
                <div class="media" @click="SidebarShow=!SidebarShow">
                  <b-icon class="media-left" icon="check"></b-icon>
                  <div class="media-content">
                    <h3>提交文章</h3>
                    <small>重新提交文章至审核</small>
                  </div>
                </div>
              </b-dropdown-item>
              <b-dropdown-item>
                <div class="media">
                  <b-icon class="media-left" pack="fas" icon="sync-alt"></b-icon>
                  <div class="media-content">
                    <h3>清空控制台</h3>
                    <small>清空工作台</small>
                  </div>
                </div>
              </b-dropdown-item>
            </b-dropdown>
          </div>
        </div>
      </div>
      <div class="column">
        <b-input v-model="markdownPost.articleTitle" placeholder="请输入标题(･_･;" rounded></b-input>
      </div>
    </div>
    <!--  Markdown编译器-->
    <div>
      <writePost :post="markdownPost.articleContent" @save="updatePost"/>
      <writePostSideBar
        @close="closeSidebarShow"
        @commit="submitPost"
        :show="SidebarShow"
        :father-side-bar="markdownPost"
      />
    </div>
  </div>
</template>

<script>
import writePost from "@/components/markdown/writePost"
import writePostSideBar from "@/components/markdown/writePostSideBar"
import {isEmpty} from "@/untils/valueUntil";
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";
import {uploadFile} from "@/untils/tencentCOS";
import {get, post} from "@/untils/request";

export default {
  name: "reWritePost",
  components: {writePost, writePostSideBar},
  data() {
    return {
      markdownPost: {
        // 文章头图
        articleBanner: "",
        // 文章标题
        articleTitle: "",
        // 文章梗概
        articleDesc: "",
        // 文章内容
        articleContent: "",
        // 文章分类
        cateName: null,
        // 数据库内标签
        tagDTOList: [],
        // 文章标签
        tagNameList: [],
        // 文章类型
        type: "1",
        // 文章来源
        originalUrl: "",
        // 是否置顶
        isTop: 0,
      },
      SidebarShow: false,
    }
  },
  created() {
    get("/img2d/article/getArticleDetail/" + this.$route.params.articleId).then(({data}) => {
      if (data.flag) {
        if (this.$store.state.loggedUserId!=data.data.userId&&this.$store.state.role!="SuperAdmin"){
          failTips("文章不是你写的嗷，跳转首页中……")
        }
        else {
          this.markdownPost = data.data
        }
      }
    })
  },
  methods: {
    closeSidebarShow(newValue) {
      this.markdownPost.articleDesc = newValue.postDesc
      this.markdownPost.cateName = newValue.postCategory
      this.markdownPost.type = newValue.postType
      this.markdownPost.tagNameList = newValue.postTags
      this.markdownPost.articleBanner = newValue.postBanner
      this.markdownPost.originalUrl=newValue.originalUrl
      this.SidebarShow = false
    },
    updatePost(post) {
      this.markdownPost.articleContent = post
    },
    submitPost(SideBarValue) {
      // 原创/转载
      this.markdownPost.type = SideBarValue.postType
      // 文章描述
      if (isEmpty(SideBarValue.postDesc)) {
        warningTips("文章描述不能为空嗷")
        return;
      } else {
        this.markdownPost.articleDesc = SideBarValue.postDesc
      }
      // 文章分类
      this.markdownPost.cateName = SideBarValue.postCategory
      // 文章标签
      this.markdownPost.tagNameList = SideBarValue.postTags
      if(isEmpty(this.markdownPost.tagNameList)||this.markdownPost.tagNameList.length<2){
        warningTips("标签太少啦")
        return;
      }
      // 原文地址
      if (this.markdownPost.type=='0'&&isEmpty(SideBarValue.originalUrl)){
        warningTips("原文地址不能为空哦")
        return;
      }
      else {
        this.markdownPost.originalUrl=SideBarValue.originalUrl
      }
      // 文章头图
      if (isEmpty(SideBarValue.postBanner)) {
        warningTips("文章头图怎么能少呢？")
        return;
      } else {
        const file = SideBarValue.postBanner
        const filenameWithTime = new Date().getTime() + file.name
        const url = uploadFile(file, filenameWithTime)
        console.log(url)
        this.markdownPost.articleBanner = url
      }
      console.log(this.markdownPost)
      post("/img2d/article/saveArticle", this.markdownPost).then(({data}) => {
        if (data.flag) {
          successTips("上传成功，请等待审核")
        }
      })
    }
  },
}
</script>

<style scoped>

</style>
