<template>
  <section>
    <div class="columns">
      <div class="column">
        <b-modal
          destroy-on-hide
          :active="updateRecommendArticleButton"
          width="25rem"
        >
          <update-recommend-post
            :choseItem="changeItem"
            :chose-item-id="changeItem.id"
            @success='updateRecommendPostFinish(arguments)'
            @close="updateRecommendArticleButton=false">
          </update-recommend-post>
        </b-modal>
        <b-table
          :data="SystemTopPost"
          ref="table">

          <b-table-column field="topId"
                          label="推"
                          sortable
                          centered
                          v-slot="props">
            {{ props.row.topId }}
          </b-table-column>

          <b-table-column field="articleTitle"
                          label="文章标题"
                          sortable
                          centered
                          v-slot="props">
            {{ props.row.articleTitle }}
          </b-table-column>

          <b-table-column field="username"
                          label="作者"
                          sortable
                          centered
                          v-slot="props">
            {{ props.row.username }}
          </b-table-column>

          <b-table-column field="articleDesc"
                          label="悬浮描述"
                          centered
                          v-slot="props">
            {{ props.row.articleDesc }}
          </b-table-column>

          <b-table-column field="imgName"
                          label="操作"
                          sortable
                          centered
                          v-slot="props">
            <div class="buttons is-centered">
              <button class="button is-small is-success"
                      @click="updateRecommendPostStart(props.row)">修改文章
              </button>
              <button @click="openArticle(props.row.id)" class="button is-small is-primary">查看文章</button>
            </div>
          </b-table-column>

        </b-table>
      </div>
    </div>
    <hr/><hr/>
        <b-table
          scrollable
          :data="SystemBannerIndex"
          detailed
          show-detail-icon
          checkable
          :checked-rows.sync="SystemBannerIndexCheckItems"
          checkbox-position="left"
          ref="tableData">

          <b-table-column
            field="topId"
            label="推"
            sortable
            centered
            v-slot="props">
            {{ props.row.topId }}
          </b-table-column>

          <b-table-column
            field="imgUrl"
            label="图片地址"
            sortable
            centered
            v-slot="props">
            {{ props.row.imgUrl }}
          </b-table-column>

          <b-table-column
            field="title"
            label="主标题描述"
            sortable
            centered
            v-slot="props">
            {{ props.row.title }}
          </b-table-column>

          <b-table-column
            field="subtitle"
            label="副标题描述"
            centered
            v-slot="props">
            {{ props.row.subtitle }}
          </b-table-column>

          <b-table-column
            field="url"
            label="跳转链接"
            sortable
            centered
            v-slot="props">
            {{ props.row.url }}
          </b-table-column>

          <template #detail="props">
            <section class="hero is-medium">
              <img :src="props.row.imgUrl" style="object-fit: cover;max-height: 42em"/>
              <a :href="props.row.url" target="_blank">
                <div class="carousel-caption">
                  <h1 class=" is-size-5">
                    <strong style="color: #000000">{{ props.row.title }}</strong>
                  </h1>
                  <p class="">{{ props.row.subtitle }}</p>
                </div>
              </a>
            </section>
          </template>


          <template #bottom-left>
            <div class="field is-grouped">
              <b-modal
                destroy-on-hide
                :active="updateBannerIndexButton"
              >
                <update-banner-index
                  :checkItem="SystemBannerIndexCheckItems"
                  :source-list="SystemBannerIndex"
                  @success="updateBannerIndexFinish"
                  @close="updateBannerIndexButton=false"></update-banner-index>
              </b-modal>
              <p class="control">
                <b-button
                  rounded
                  :disabled="SystemBannerIndexCheckItems.length!=1"
                  @click="updateBannerIndexButton=true"
                  label="修改此项"
                  class="button is-link"/>
              </p>
              <p class="control">
                <b-tooltip
                  :active="SystemBannerIndex.length<=1"
                  label="不能再少啦，最少需要一项哦(･_･;"
                  type="is-danger is-light"
                  position="is-top">
                  <b-button
                    rounded
                    :disabled="SystemBannerIndexCheckItems.length==0||SystemBannerIndex.length<=1"
                    @click="confirmDelete"
                    label="删除选定"
                    class="button is-danger"/>
                </b-tooltip>
              </p>
              <b-modal
                destroy-on-hide
                :active="addBannerIndexButton"
              >
                <add-banner-index
                  :source-list="SystemBannerIndex"
                  @success="updateBannerIndexFinish"
                  @close="addBannerIndexButton=false"></add-banner-index>
              </b-modal>
              <p class="control">
                <b-button
                  rounded
                  @click="addBannerIndexButton=!addBannerIndexButton"
                  label="新增内容"
                  class="button is-info"/>
              </p>
            </div>
          </template>
        </b-table>
    <hr/><hr/>
  </section>
</template>

<script>
import {get, post} from "@/untils/request"
import {productSnowKey} from "@/untils/snowKey";
import updateRecommendPost from "@/components/adminAudit/post/setRecommendPost"
import updateBannerIndex from "@/components/adminAudit/index/updateBannerIndex"
import addBannerIndex from "@/components/adminAudit/index/addBannerIndex"
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";
import {sortBy,makeMinus} from "@/untils/jsonUntil";

export default {
  name: "auditInterface",
  components: {updateRecommendPost, updateBannerIndex,addBannerIndex},
  data() {
    return {
      SystemTopPost: [{
        topId: "",
        id: "",
        username: "",
        articleTittle: "",
        articleDesc: ""
      }],
      SystemBannerIndexCheckItems: [],
      SystemBannerIndex: [{
        /**
         * 优先级别
         */
        topId: "",
        /**
         * 图片地址
         */
        imgUrl: "",
        /**
         * 点击跳转地址
         */
        url: "",
        /**
         * 主标题
         */
        title: "",
        /**
         * 副标题
         */
        subtitle: "",
      }
      ],
      updateRecommendArticleButton: false,
      updateBannerIndexButton: false,
      addBannerIndexButton:false,
      changeItem: "",
    }
  },
  mounted() {
    this.getRecommendArticle()
    this.getBannerIndex()
  },
  methods: {
    getRecommendArticle() {
      get("/img2d/article/getRecommendArticle").then(({data}) => {
        if (data.flag) {
          data.data.sort(sortBy("topId", true, parseInt))
          this.SystemTopPost = data.data
        }
      })
    },
    getBannerIndex() {
      get("/img2d/systemctl/getBannerIndex").then(({data}) => {
        if (data.flag) {
          if (data.data.length == 1 && data.data[0].topId == -1) {
            this.SystemBannerIndex = [{
              topId: "-1",
              imgUrl: require("@/assets/loading/Loading_IMG.jpg"),
              url: "https://www.mintimate.cn",
              title: "Mintimate's Blog",
              subtitle: "Mintimate的博客，只为与你分享。不定期分享干货。"
            }]
          }
          else {
            this.SystemBannerIndex=data.data
          }
        }
      })
    },
    openArticle(articleId) {
      window.open("/postDetail/" + productSnowKey(articleId), '_blank');
    },
    updateRecommendPostStart(item) {
      this.changeItem = item
      this.updateRecommendArticleButton = true
    },
    updateRecommendPostFinish(args) {
      this.SystemTopPost.forEach((item, index) => {
        if (item.id == args[1]) {
          this.SystemTopPost[index] = args[0]
          return
        }
      })
      post("/img2d/article/admin/updateRecommendArticle", this.SystemTopPost).then(({data}) => {
        if (data.flag) {
          this.$nextTick(this.getRecommendArticle())
          successTips("更新成功，获取最新数据")
        } else {
          warningTips("尴尬，更新失败：数据异常")
        }
      })
      this.updateRecommendArticleButton = false
    },
    updateBannerIndexFinish() {
      this.$nextTick(this.getBannerIndex)
      this.updateBannerIndexButton=false
      this.addBannerIndexButton=false
      this.SystemBannerIndexCheckItems=[]
    },
    confirmDelete() {
      this.$buefy.dialog.confirm({
        title: '确定删除该项',
        message: '是否确认删除，注意<b>该操作不可复原！！！</b>',
        confirmText: '删除',
        cancelText:"手滑啦(･_･;",
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => {
          let afterBannerIndex=this.SystemBannerIndex
          afterBannerIndex=makeMinus(afterBannerIndex,this.SystemBannerIndexCheckItems)
          post("/img2d/systemctl/saveBannerIndex",afterBannerIndex).then(({data})=>{
            if(!data.flag){
              failTips("删除失败(･_･;")
            }
            else{
              successTips("删除成功:-)")
              this.getBannerIndex()
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>
.carousel-caption >>> {
  text-shadow: 0 0 0.2em #424242;
  right: 15%;
  bottom: 2rem;
  left: 15%;
  z-index: 10;
  padding-top: 20px;
  padding-bottom: 20px;
  color: #424242;
  text-align: center;
}
</style>
