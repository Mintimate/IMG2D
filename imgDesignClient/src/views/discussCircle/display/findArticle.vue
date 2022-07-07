<template>
  <div class="mt-2">
    <div class="columns">
      <div class="column is-9-desktop is-12 is-full-mobile">
        <div class="columns">
          <div class="column">
            <b-field label-position="labelPosition">
              <b-input v-model="searchKeyWorld" expanded placeholder="小手一抖，结果就有(*☻-☻*)" type="search"></b-input>
              <b-field>
                <b-tooltip
                  always
                  :active="searchKeyWorld.length<4&&searchKeyWorld.length!=0 ? true:false" position="is-left"
                  label="关键字最少需要四个字">
                  <b-button
                    :disabled="searchKeyWorld.length<4&&searchKeyWorld.length!=0 ? true:false"
                    @click="makeSearch" class="button is-primary">
                    {{ searchKeyWorld.length == 0 ? '搜索看看' : '进行搜索' }}
                  </b-button>
                </b-tooltip>
              </b-field>
            </b-field>
          </div>
          <div class="column has-text-centered is-align-self-center">
            <router-link
              to="/discussIndex"
              class="button is-small is-ghost is-light is-rounded">全部主题
            </router-link>
            <button
              @click="changeType('摄影资讯')"
              class="button is-small is-warning is-light is-rounded ml-3">摄影资讯
            </button>
            <button
              @click="changeType('摄影经验')"
              class="button is-small is-light is-light is-rounded ml-3">摄影经验
            </button>
            <button
              @click="changeType('摄影分享')"
              class="button is-small is-dark is-light is-rounded ml-3">摄影分享
            </button>
            <button
              @click="changeType('咨询求助')"
              class="button is-small is-success is-light is-rounded ml-3">咨询求助
            </button>
          </div>
        </div>
        <div class="box">
          <router-link v-for="item in articleList" :key="item.id" :to="`/postDetail/${(productSnowKey(item.id))}`">
            <div class="card columns">
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
          </router-link>
        </div>
      </div>
      <div class="column is-3-desktop is-hidden-mobile">
        <div class="columns">
          <div class="column m-1 has-text-centered is-align-self-center">
            <router-link class="button is-danger is-rounded is-small is-fullwidth" to="/discuss/newPost">发布主题
            </router-link>
          </div>
        </div>
        <div class="box">
          <div class="columns">
            <div class="column">
              <h3 class="has-text-weight-bold">小编推荐</h3>
              <ul class="pt-2">
                <li v-for="item in recommendArticleOne" :key="item.id">
                  <b-tooltip :label="item.articleDesc"
                             type="is-info is-light"
                             position="is-left">
                    <a target="_blank"
                       :href="'/postDetail/' + productSnowKey(item.id)">
                      {{ item.articleTitle }}
                    </a>
                  </b-tooltip>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="box">
          <div class="columns">
            <div class="column">
              <h3 class="has-text-weight-bold	">阅读发现</h3>
              <ul class="pt-2">
                <li v-for="item in recommendArticleTwo" :key="item.id">
                  <b-tooltip :label="item.articleDesc"
                             type="is-primary is-light"
                             position="is-left">
                    <a target="_blank" :href="'/postDetail/' + productSnowKey(item.id)">
                      {{ item.articleTitle }}
                    </a>
                  </b-tooltip>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <hr/>
    <div class="columns">
      <div class="column">
        <b-pagination
          :total="pagination.total"
          v-model="pagination.current"
          :current="Number(pagination.current)"
          :per-page="pagination.perPage"
          @change="paginationChange"
          rounded
          order="is-centered"/>
      </div>
    </div>
  </div>
</template>

<script>
import {get} from "@/untils/request";
import {productSnowKey} from "@/untils/snowKey";
import {backToTop} from "@/untils/backToTop";
import {failTips} from "@/untils/tipsInfoUntil";

export default {
  name: "findArticle",
  props:["articleType"],
  data() {
    return {
      onePageArticle: 4,
      searchKeyWorld: "",
      articleTypeName:"",
      articleList: [
        {
          id: "",
          userId: "",
          username: "",
          categoryId: "",
          cateName: "",
          articleBanner: require("@/assets/loading/Loading_IMG.jpg"),
          articleTitle: "Loading……",
          articleDesc: "Loading……",
          createTime: "",
          type: "",
          isTop: "",
          tagDTOList: [{
            id: "",
            tagName: ""
          }]
        }
      ],
      recommendArticleOne: [
        {
          topId: "",
          id: "",
          username: "",
          articleTittle: "",
          articleDesc: ""
        }
      ],
      recommendArticleTwo: [
        {
          topId: "",
          id: "",
          username: "",
          articleTittle: "",
          articleDesc: ""
        }
      ],
      pagination: {
        total: "",
        perPage: 4,
        current: 1
      }
    }
  },
  created() {
    this.articleTypeName=this.articleType
    if (!/^[0-9]+.?[0-9]*/.test(this.$route.query.page)) {
      // 如果不是纯数字
      this.pagination.current = 1
    } else {
      this.pagination.current = Number(this.$route.query.page)
    }
    this.getPostList()
    this.getRecommendArticle()
  },
  methods: {
    changeType(type){
      this.articleTypeName=type
      this.pagination.current=1
      this.getPostList()
    },
    getPostList() {
      get("/img2d/article/getHomeArticleByType/" + this.articleTypeName + "/" + this.pagination.current).then(({data}) => {
          if (!data.flag) {
            failTips("数据获取失败，请过会再尝试")
            return;
          }
          this.articleList = data.data.recordList
          this.pagination.total = data.data.count
        }
      )
    },
    paginationChange() {
      // 修改导航栏
      const query = JSON.parse(JSON.stringify(this.$route.query))
      query.page = this.pagination.current
      this.$router.push({path: this.$route.path, query})
      this.getPostList()
      backToTop()
    },
    productSnowKey,
    getRecommendArticle() {
      get("/img2d/article/getRecommendArticle").then(({data}) => {
        if (data.flag) {
          this.recommendArticleOne = data.data
        }
      })
      get("/img2d/article/getRecommendArticle").then(({data}) => {
        if (data.flag) {
          this.recommendArticleTwo = data.data
        }
      })
    },
    makeSearch() {
      window.open("/searchArticle/?searchKeyWorld=" + this.searchKeyWorld, "_self")
    }
  }
}
</script>

<style scoped>
@media screen and (max-width: 1024px) {
  .is-hidden-mobile {
    display: none;
  }
}

ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

li:before {
  position: absolute;
  left: 0;
  top: 11px;
  content: "";
  width: 0;
  height: 0;
  line-height: 0;
  border: 4px solid transparent;
  border-left: 5px solid #ccc;
}

li {
  position: relative;
  padding: 5px 0 10px 12px;
  line-height: 1.46;
}

.img-boder img {
  display: block;
  width: 100%;
  height: 14rem;
  object-fit: cover;
  box-shadow: 0 5px 11px 0 hsla(0, 0%, 0%, 0.18), 0 4px 15px 0 hsla(0, 0%, 0%, 0.15);
  border-radius: 0.25rem;
}

.b-tooltip >>> .tooltip-content{
  width: 300px!important;
  white-space: break-spaces;
  font-size: x-small;
}
</style>
