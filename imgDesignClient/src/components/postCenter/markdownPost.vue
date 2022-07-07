<template>
  <section>
    <div class="banner"
         :style="{
         background: 'url(' + postDetail.articleBanner + ')' +
          'center center / cover no-repeat'
    }"
    >
      <!-- style="background: url('https://imagehost.mintimate.cn/Magisk_Update.png')center center / cover no-repeat;"-->
      <div class="article-info-container">
        <div class="article-title">
          <h1 class="is-size-2 has-text-weight-bold" style=" padding: 0.5rem 0.5rem">
            {{ postDetail.articleTitle }}
          </h1>
        </div>
        <div class="article-info">
          <div class="first-line">
            <span>
            <i class="iconfont iconrili"></i>
            发表于 {{ new Date(postDetail.createTime).toLocaleDateString() }}
            </span>
            <span
            class="separator">|
            </span>
            <span>
              <i class="iconfont icongengxinshijian"></i>
            更新于 {{ new Date(postDetail.updateTime).toLocaleDateString() }}
            </span>
            <span class="separator">|</span>
            <span class="article-category">
              <i class="iconfont iconfenlei1"></i>
              <a :href="'/findArticle/'+postDetail.cateName" class=""> {{
              postDetail.cateName
            }} </a>
            </span>
          </div>
          <div class="second-line"><span><i
            class="iconfont icon-icon-14-zishutongji"></i> 字数统计: {{ words }} </span>
            <span class="separator">|</span>
            <span><i class="iconfont icon-yuedushichang"></i> 阅读时长: {{ readTime }} </span>
          </div>
          <span><i
            class="iconfont icon-anpinglunshuliang"></i>评论数: {{ count }}</span>
          <template v-if="postDetail.type=='0'">
            <span class="separator">|</span>
            <span><i class="iconfont icon-wenzhang"></i> 原文地址:
              <a :href="postDetail.originalUrl" target="_blank">点击查看</a> </span>
          </template>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="left-content">
        <div class="content-box">
          <div style="padding: 14px">
            <hr/>
            <div ref="markdownContent">
              <v-md-editor mode="preview" v-model="postDetail.articleContent"></v-md-editor>
            </div>
            <div>
              <Comment
                :commentList="commentList"
                :count="count"
                :post-id="postDetail.id"
                @reloadComment="listComment"/>
            </div>
          </div>
        </div>
      </div>
      <div class="right-content">
        <div class="catalog content-box">
          <div class="catalog-detail">
            <div class="catalog-title"><p class="has-text-weight-bold	is-size-5">目录</p></div>
            <hr/>
            <div class="catalog-auto"></div>
            <!--            <ul class="catalog">-->
            <!--              <template v-for="(item, index) in catalog">-->
            <!--                <li class="mt-2" :key="index" :style="{ paddingLeft: item.level * 11-11 + 'px' }">-->
            <!--                  <a-->
            <!--                    :href="'#' + item.id"-->
            <!--                  >{{ item.title }}-->
            <!--                  </a-->
            <!--                  >-->
            <!--                </li>-->
            <!--              </template>-->
            <!--            </ul>-->
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import Comment from "@/components/Comment/Comment";
import {get} from "@/untils/request";
import tocbot from "tocbot";
// import {backToSnowKey} from "@/untils/snowKey";

export default {
  name: "markdownPost",
  props: ["postSource"],
  components: {Comment},
  data() {
    return {
      catalog: "",
      postDetail: {
        id: "",
        userId: "",
        cateName: "",
        articleBanner: "",
        articleTitle: "",
        articleDesc: "",
        articleContent: "",
        type: "",
        originalUrl: "",
        createTime: "",
        updateTime: "",
        tagNameList: [],
      },
      words: "",
      readTime: "",
      commentList: [],
      count: 0,
    }
  },
  destroyed() {
    tocbot.destroy();
  },
  methods: {
    deleteHTMLTag(content) {
      return content.replace(/<\/?[^>]*>/g, "").replace(/[|]*\n/, "").replace(/&npsp;/gi, "");
    },
    generatCatalog() {
      const article_content = this.$refs.markdownContent.children[0].children[1].children[1].children[1].children[0].children[0].children[0].children[0];
      const titleTag = ["H1", "H2", "H3"];
      article_content.children[0].childNodes.forEach((e, index) => {
        if (titleTag.includes(e.nodeName)) {
          const id = "header-" + index;
          e.setAttribute("id", id);
        }
      });
      tocbot.init({
        tocSelector: ".catalog-auto", //要把目录添加元素位置，支持选择器
        contentSelector: ".github-markdown-body", //获取html的元素
        headingSelector: "h1, h2, h3", //要显示的id的目录
        hasInnerContainers: true,
        onClick: function (e) {
          e.preventDefault();
        }
      });
    },
    // generatCatalog() {
    //   // 生成目录
    //   // 保证渲染成功
    //   this.$nextTick(() => {
    //     const article_content = this.$refs.markdownContent.children[0].children[1].children[1].children[1].children[0].children[0].children[0].children[0];
    //     const titleTag = ["H1", "H2", "H3"];
    //     let titles = [];
    //     article_content.children[0].childNodes.forEach((e, index) => {
    //       if (titleTag.includes(e.nodeName)) {
    //         const id = "header-" + index;
    //         e.setAttribute("id", id);
    //         titles.push({
    //           id: id,
    //           title: e.innerHTML,
    //           level: Number(e.nodeName.substring(1, 2)),
    //           nodeName: e.nodeName
    //         });
    //       }
    //     });
    //     this.catalog = titles;
    //   });
    // },
    listComment() {
      get("/img2d/comments/get/byArticleId", {
        articleId: this.postDetail.id
      })
        .then(({data}) => {
          this.commentList = data.data.recordList;
          this.count = data.data.count;
        });
    },
  },
  // computed: {
  //   postID: function () {
  //     return backToSnowKey(this.postId)
  //   }
  // },
  watch: {
    postSource: {
      handler(newVal) {
        this.postDetail = newVal
        // 字数统计
        this.words = this.deleteHTMLTag(this.postDetail.articleContent).length
        // 计算阅读时间
        this.readTime = Math.round(this.words / 400) + "分钟";
        this.listComment()
        this.$nextTick(this.generatCatalog)
      },
      deep: true,
      // immediate: true
    },
  },
}
</script>

<style>
.toc-list-item {
  font-size: 16px;
  line-height: 1.75;
  list-style: none;
}

.toc-link {
  display: block;
  border-left: 3px solid transparent;
  color: #666261 !important;
  transition: all .2s ease-in-out;
}

.is-active-link {
  border-left-color: #6238d5;
  background: #7957D5;
  color: #fff !important;
}

.node-name--H1 {
  padding-left: 10px;
}

.node-name--H2 {
  padding-left: 20px;
}

.node-name--H3 {
  padding-left: 30px;
}

.banner {
  color: #eee !important;
  animation: main 1s;
  position: absolute;
  background-color: #49b1f5 !important;
  top: -60px;
  left: 0;
  right: 0;
  height: 500px;
  animation: header-effect 1s;
}

.article-info-container {
  position: absolute;
  bottom: 6.25rem;
  padding: 0 8%;
  width: 100%;
  text-align: center;
}

.banner::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}


.content-box {
  display: block;
  max-width: 100%;
  outline: none;
  text-decoration: none;
  transition-property: box-shadow, opacity;
  overflow-wrap: break-word;
  position: relative;
  white-space: normal;
  transition: box-shadow 280ms cubic-bezier(0.4, 0, 0.2, 1);
  will-change: box-shadow;
  box-shadow: 0px 3px 1px -2px rgb(0 0 0 / 20%), 0px 2px 2px 0px rgb(0 0 0 / 14%), 0px 1px 5px 0px rgb(0 0 0 / 12%);
  border-radius: 12px
}

.row {
  display: flex;
  flex-wrap: wrap;
  flex: 1 1 auto;
  justify-content: center;

  animation: main 1s;
  max-width: 1200px;
  margin: 372.5px auto 40px auto !important;
  padding: 0 5px;
}

.left-content {
  max-width: 100%;
  width: 100%;
  padding: 14px;
}

@media screen and (min-width: 1024px) {
  .left-content {
    max-width: 75%;
    width: 100%;
    padding: 14px;
  }
}
.right-content {
  flex: 0 0 25%;
  max-width: 25%;
  width: 100%;
  padding: 14px;
}


@media screen and (max-width: 1024px) {
  .right-content {
    display: none !important;
  }
}

.right-content {
  width: 100%;
}

.catalog {
  width: 100%;
  position: sticky;
  top: 3rem;
  background-color: #FFFFFF;
  border-color: #FFFFFF;
  color: rgba(0, 0, 0, 0.87);
}

.catalog-title {
  display: flex;
  align-items: center;
  padding-left: 15px;
  line-height: 1.75;
}

.catalog-detail {
  padding: 10px 0px 24px;
}

.not-display-mobile {
  /*top: auto;*/
  /*position: fixed;*/
  position: sticky;
  /*left: 70%;*/
  margin-left: 30px;
}

hr {
  margin: 0.5rem 0;
}
</style>
