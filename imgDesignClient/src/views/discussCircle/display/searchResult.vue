<template>
  <section>
    <div class="box">
      <div class="columns">
        <div class="column">
          <b-field label-position="labelPosition">
            <b-input v-model="searchKeyWorld" expanded placeholder="再抖抖？看看其他结果:-)" type="search"></b-input>
            <b-field>
              <b-tooltip
                always
                :active="searchKeyWorld.length<4&&searchKeyWorld.length!=0 ? true:false" position="is-left" label="关键字最少需要四个字">
              <b-button
                :disabled="searchKeyWorld.length<4&&searchKeyWorld.length!=0 ? true:false"
                @click="makeSearch" class="button is-primary">
                {{searchKeyWorld.length==0?'小圈首页':'进行搜索'}}
              </b-button>
              </b-tooltip>
            </b-field>
          </b-field>

        </div>
      </div>
      <template v-if="articleList.length!=0">
      <router-link v-for="item in articleList" :key="item.id" :to="`/postDetail/${(productSnowKey(item.id))}`">
        <div class="columns">
          <div class="box column">
            <div class="columns">
              <div class="column is-5 is-mobile img-boder">
                <img :src="item.articleBanner">
              </div>
              <div class="column is-mobile">
                <strong><h1 class="has-text-weight-bold is-size-4">{{ item.articleTitle }}</h1></strong>
                <p>{{ item.articleDesc }}</p>
                <b-field>
                  <b-taglist>
                    <b-tag v-for="tag in item.tagDTOList" :key="tag.id">
                      <b-icon
                        icon="tag"
                        size="is-small">
                      </b-icon>
                      <span>{{ tag.tagName }}</span>
                    </b-tag>
                  </b-taglist>
                </b-field>
                <b-field>
                  <b-taglist>
                    <b-tag
                      v-if="item.isTop!=0"
                      type="is-primary">
                      <b-icon
                        icon="format-vertical-align-top"
                        size="is-small">
                      </b-icon>
                      <span>置顶</span>
                    </b-tag>
                    <b-tag
                      type="is-primary">
                      <b-icon
                        icon="border-all"
                        size="is-small">
                      </b-icon>
                      <span>{{ item.cateName }}</span>
                    </b-tag>
                    <b-tag
                      type="is-primary">
                      <b-icon
                        icon="calendar-range"
                        size="is-small">
                      </b-icon>
                      <span>{{ new Date(item.createTime).toLocaleDateString() }}</span>
                    </b-tag>
                  </b-taglist>
                </b-field>
              </div>
            </div>
          </div>
        </div>
      </router-link>
      </template>
      <template v-else>
        <div class="columns">
          <div class="box column">
            <div class="columns">
              <div class="column is-5 is-mobile img-boder">
                <img :src="loadingResource.loadingIMG">
              </div>
              <div v-once class="column is-mobile">
                <strong><h1 class="has-text-weight-bold is-size-4">等待用户进行搜索操作ing</h1></strong>
                <p>哈希级别的搜索效率，闪电般的搜索速度～只要服务器没被打，我就能给你所要的～</p><br/><br/>
                <p v-if="searchKeyWorld.length!=0">尴尬，全局搜索后发现，
                  <u>{{searchKeyWorld}}</u>
                  关键字没有匹配项</p>
              </div>
            </div>
          </div>
        </div>

      </template>
    </div>
  </section>
</template>

<script>
import {get} from "@/untils/request"
import {productSnowKey} from "@/untils/snowKey";

export default {
  name: "searchResult.vue",
  data() {
    return {
      searchKeyWorld: "",
      articleList: [],
      loadingResource:{
        loadingIMG:require("@/assets/loading/Loading_IMG.jpg")
      }
    }
  },
  created() {
    this.searchKeyWorld = this.$route.query.searchKeyWorld
    this.getArticleHomeByKeyWord()
  },
  methods: {
    getArticleHomeByKeyWord() {
      get("/img2d/article/getHomeArticleBySearch/" + this.searchKeyWorld).then(({data}) => {
        if (data.flag) {
          this.articleList = data.data
        }
      })
    },
    productSnowKey,
    makeSearch() {
      console.log(this.searchKeyWorld)
      if (this.searchKeyWorld == undefined || this.searchKeyWorld.length == 0) {
        window.open("/discussIndex", "_self")
        return
      }
      window.open("/searchArticle/?searchKeyWorld=" + this.searchKeyWorld, "_self")
    }
  }
}
</script>

<style scoped>

</style>
