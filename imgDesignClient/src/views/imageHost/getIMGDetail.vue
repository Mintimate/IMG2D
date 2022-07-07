<template>
  <div class="my-content">
    <div class="box my-left-display m-1">
      <div @click="isChoose=!isChoose"
           :class="{'img-view':isChoose}">
        <div class="img-layer"></div>
        <div class="eFhoug img">
          <img @load="getIMGSize" ref="opus"
               class="jOmqKq"
               width="100%" height="auto" :src="IMG_Detail.imgHostPath">
        </div>
      </div>
      <Comment
        :commentList="commentList"
        :count="count"
        :user-id="IMG_Detail.userId"
        :post-id="IMG_Detail.id"
        post-type="IMG"
        @reloadComment="listComment"/>
    </div>
    <div class="my-right-display m-1">
      <div class="box">
        <div class="clearfix">
          <a :href="IMG_Detail.userSpace" target="_blank">
            <img class="the-avatar"
                 :src="IMG_Detail.userAvatarPath">
          </a>
          <a :href="'/userSpace/'+IMG_Detail.userId" target="_blank">
            <span class="is-size-5">{{ IMG_Detail.username }}</span>
            <span class="" style="margin: 0 2%">/</span>
            <span class="is-size-7">{{ IMG_Detail.userRole=="SuperAdmin"? '至尊VIP':IMG_Detail.userRole }}</span>
          </a><br/>
          <div class="the-avatar-buttons">
            <button @click="showChargeAvatar=true" class="the-avatar-button is-primary button is-small is-rounded">充电
            </button>
            <b-modal v-model="showChargeAvatar" width="25rem">
              <charge-avatar
                :avatar-id="IMG_Detail.userId"
                @close="showChargeAvatar=false"/>
            </b-modal>
            <button
              @click="clickFollow"
              :class="[userFav.hasFollow ?'is-warning':'is-light']"
              class="the-avatar-button button is-small is-rounded">
              {{ userFav.hasFollow ? '已关注' : '关注' }}
            </button>
          </div>
        </div>
        <hr/>
        <div class="lover-buttons is-center">
          <button
            :class="[userFav.hasLikeImg ?'is-info':'is-light']"
            class="button-lover button is-rounded" @click="clickLike">
            <i class="iconfont icon-dianzan_huaban"></i>
            <div class="is-size-7" style="line-height: 1">
              <span>{{ IMG_Detail.likeCount == undefined ? 0 : IMG_Detail.likeCount }}</span>
              <br/>
              <span>点赞</span>
            </div>
          </button>
          <button
            :class="[userFav.hasCollect ?'is-info':'is-light']"
            class="button-lover button is-rounded" @click="clickCollect">
            <i class="iconfont icon-shoucang"></i>
            <div class="is-size-7" style="line-height: 1">
              <span>{{ IMG_Detail.collectCount == undefined ? 0 : IMG_Detail.collectCount }}</span>
              <br/>
              <span>收藏</span>
            </div>
          </button>
          <button
            @click="shareButtonActive=!shareButtonActive"
            class="button-lover button is-warning is-rounded">
            <b-tooltip
              position="is-bottom"
              type="is-light"
              :always="shareButtonActive"
              :triggers="['click']"
              :auto-close="['outside', 'escape']">
              <template v-slot:content>
                <share :config="config"></share>
              </template>
              <span></span>
            </b-tooltip>
            <i class="iconfont icon-weibiaoti-"></i>
            分享
          </button>
        </div>
        <div style="margin:20px 0 10px;padding:15px 20px;background:#f6f5fa;">
          <span style="color:#636363">图片标签(AI识别)</span>
          <div class="mt-3">
            <ul style="font-size: 0.8rem;line-height: 1.5rem">
              <b-field>
                <b-taglist>
                  <b-tag
                    type="is-primary"
                    v-for="tag in IMG_Detail.tagDTOList" :key="tag.id">
                    <router-link
                      style="color: #ffffff"
                      :to="'/getIMGByTag?tagName='+tag.tagName">
                      <b-icon
                        icon="tag"
                        size="is-small">
                      </b-icon>
                      <span>{{ tag.tagName }}</span>
                    </router-link>
                  </b-tag>
                </b-taglist>
              </b-field>
            </ul>
          </div>
        </div>
        <hr/>
        <div style="margin:20px 0 10px;padding:15px 20px;background:#f6f5fa;">
          <a href="/zh/service/license/" style="color:#636363">图片许可证</a>
          <div style="margin-top:4px;">
            <ul style="font-size: 0.8rem;line-height: 1.5rem">
              <li>可以做商业用途</li>
              <li>不要求署名</li>
            </ul>
          </div>
        </div>
        <hr/>
        <b-collapse
          :open="false"
          animation="slide">
          <template #trigger="props">
            <div
              class="button is-success is-fullwidth is-rounded"
              role="button">
              下载
              <a class="card-header-icon">
                <b-icon
                  pack="fas"
                  :icon="props.open ? 'caret-down' : 'minus'">
                </b-icon>
              </a>
            </div>
          </template>
          <div class="card-content">
            <div class="content is-small">
              <ol type="A">
                <li>
                  提供高质量、小体积的图床图片；在画质质量基本没亏损情况下，大幅度缩小图片体积。
                </li>
                <li>
                  你也可以选择下载原图图片（<strong>需要使用积分按次下载哦</strong>）
                </li>
              </ol>
            </div>
          </div>
          <footer class="card-footer">
            <b-button @click="openImageHost" class="card-footer-item is-size-6">
              图床链接
            </b-button>
            <b-button
              @click="confirm"
              type="is-link"
              target="_blank" :href="imgSrc" class="card-footer-item is-size-6">原图链接
            </b-button>
          </footer>
        </b-collapse>
        <hr/>
        <div style="padding:15px 20px;background:#f6f5fa">
          <table>
            <tbody>
            <tr>
              <th style="white-space:nowrap;width: 50%">图片类型</th>
              <td>JPG</td>
            </tr>
            <tr>
              <th>清晰度</th>
              <td>{{ IMGSize.width }}×{{ IMGSize.height }}</td>
            </tr>
            <tr>
              <th>创建</th>
              <td>{{ new Date(IMG_Detail.createTime).toLocaleDateString() }}</td>
            </tr>
            <tr>
              <th>类别</th>
              <td><a target="_blank" :href="'/getIMGByClassify/'+IMG_Detail.cateId">{{ IMG_Detail.cateName }}</a></td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import chargeAvatar from "@/components/imgTool/chargeAvatar"
import Comment from "@/components/Comment/Comment";
import {backToSnowKey} from "../../untils/snowKey";
import {downloadByPost, get, post} from "@/untils/request";
import {warningTips} from "@/untils/tipsInfoUntil";

export default {
  name: "getIMGDetail",
  props: ["imgID"],
  components: {chargeAvatar, Comment},
  data() {
    return {
      shareButtonActive: false,
      showChargeAvatar: false,
      isChoose: false,
      IMG_ID: "",
      IMGSize: {
        width: "",
        height: ""
      },
      userFav: {
        hasCollect: false,
        hasFollow: false,
        hasLikeImg: false,
      },
      IMG_Detail: {
        id: "",
        imgName: "",
        imgHostPath: require("@/assets/loading/Loading_IMG.jpg"),
        cateId: "",
        cateName: "",
        userId: "",
        username: "",
        userRole:"",
        userAvatarPath: "",
        userSpace: "",
        likeCount: "",
        collectCount: "",
        createTime: "",
        tagDTOList: [
          {
            id: "",
            tagName: ""
          }
        ]
      },
      imgName: "loading",
      authorName: "Loading",
      imgCategory: "loading",
      imgSrc: "loading",
      avatarIMG: null,
      commentList: [],
      count: 0,
    }
  },
  computed: {
    config: function () {
      return {
        source: window.location.host+this.$route.fullPath, // 来源（QQ空间会用到）, 默认读取head标签：<meta name="site" content="http://overtrue" />
        title: this.IMG_Detail.imgName + "--IMG2D 专注图片", // 标题，默认读取 document.title 或者 <meta name="title" content="share.js" />
        description: 'Hey，快来看看这张图片，来自用户：' + this.IMG_Detail.username, // 描述, 默认读取head标签：<meta name="description" content="PHP弱类型的实现原理分析" />
        image: this.IMG_Detail.imgHostPath, // 图片, 默认取网页中第一个img标签
        sites: ['qzone', 'qq', 'weibo', 'wechat'], // 启用的站点
        wechatQrcodeTitle: '微信扫一扫：分享', // 微信二维码提示文字
        wechatQrcodeHelper: '<p>微信里点“发现”，扫一下</p><p>二维码便可将本文分享至朋友圈。</p>'
      }
    },
  },
  methods: {
    getIMGSize() {
      this.IMGSize.width = this.$refs.opus.naturalWidth;
      this.IMGSize.height = this.$refs.opus.naturalHeight;
    },
    checkLogin() {
      if (this.$store.state.logged == undefined || this.$store.state.logged == "") {
        warningTips("请先登录再操作嗷")
        return true
      }
      return false
    },
    listComment() {
      get("/img2d/comments/get/byIMG", {
        articleId: this.IMG_Detail.id
      })
        .then(({data}) => {
          this.commentList = data.data.recordList;
          this.count = data.data.count;
        });
    },
    clickLike() {
      if (this.checkLogin()) {
        return
      }
      post("/img2d/img/clickLike/" + this.IMG_Detail.id).then(({data}) => {
        if (data.flag) {
          this.IMG_Detail.likeCount = "OK~"
          this.userFav.hasLikeImg = !this.userFav.hasLikeImg
        }
      })
    },
    clickFollow() {
      if (this.checkLogin()) {
        return
      }
      post("/img2d/userCenter/loggedUser/follow/" + this.IMG_Detail.userId).then(({data}) => {
        if (data.flag) {
          this.userFav.hasFollow = !this.userFav.hasFollow
        }
      })
    },
    clickCollect() {
      if (this.checkLogin()) {
        return
      }
      post("/img2d/img/clickCollect/" + this.IMG_Detail.id).then(({data}) => {
        if (data.flag) {
          this.IMG_Detail.collectCount = "OK~"
          this.userFav.hasCollect = !this.userFav.hasCollect
        }
      })
    },
    downloadOriginIMG() {
      downloadByPost("/img2d/img/Original/" + this.IMG_ID).then(resp => {
        if (!resp) {
          return
        }
        const imageType = resp.headers['content-disposition'].split(".").pop()
        let url = window.URL.createObjectURL(new Blob([resp.data]))
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', "泪目～花了20积分下来了这张图片." + imageType)
        document.body.appendChild(link)
        link.click()
      })
    },
    openImageHost() {
      window.open(this.IMG_Detail.imgHostPath, '_blank')
    },
    confirm() {
      this.$buefy.dialog.confirm({
        title: "原图下载",
        message: '原图下载，单次20积分是否确定下载?',
        confirmText: "确定",
        cancelText: "取消",
        onConfirm: () => this.downloadOriginIMG()
      })
    }
  },
  mounted() {
    this.IMG_ID = backToSnowKey(this.imgID)
    if (this.IMG_ID != null) {
      get("/img2d/img/getSingleIMG/" + this.IMG_ID)
        .then(({data}) => {
          if (data.flag) {
            // console.log(data.data)
            this.IMG_Detail = data.data
            this.IMG_Detail.imgHostPath = "/img2d/" + data.data.imgHostPath
            this.IMG_Detail.userAvatarPath = "/img2d/" + data.data.userAvatarPath
            this.listComment()
            if (this.$store.state.logged) {
              get("/img2d/userCenter/loggedUser/getImgDetail/"
                + this.IMG_ID + "/"
                + this.IMG_Detail.userId).then(({data}) => {
                this.userFav = data.data
              })
            }
          }
        })
    }

  }
}
</script>

<style scoped>
@import '//at.alicdn.com/t/font_2867504_hdww7ni4efm.css';

.jOmqKq {
  display: block;
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 625px;
}

.fade-enter-active,
.fade-leave-active {
  transition: all .2s linear;
  transform: translate3D(0, 0, 0);
}

.fade-enter,
.fade-leave-active {
  transform: translate3D(100%, 0, 0);
}

/* bigimg */
.img-view {
  position: inherit;
  width: 100%;
}

/*遮罩层样式*/
.img-view .img-layer {
  position: fixed;
  z-index: 999;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.7);
  width: 100%;
  height: 100%;
  overflow: hidden;
}

/*不限制图片大小，实现居中*/
.img-view .img img {
  display: block;
  position: absolute;
  transform: scale(1.2);
  width: auto;
  left: 0;
  right: 0;
  margin: auto;
  z-index: 1000;
}

.eFhoug {
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  line-height: 0;
  font-size: 0px;
  min-width: 200px;
  min-height: 625px;
  cursor: zoom-in;
}


@media screen and (min-width: 1050px) {
  .my-content {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
  }
}

@media screen and (min-width: 1050px) {
  .my-left-display {
    -webkit-box-flex: 0;
    -ms-flex: none;
    flex: none;
    width: 75%;
  }
}

@media screen and (min-width: 1050px) {
  .my-right-display {
    -webkit-box-flex: 0;
    -ms-flex: none;
    flex: none;
    width: 25%;
  }
}

.the-avatar {
  float: left;
  width: 48px;
  height: 48px;
  border-radius: 48px;
  margin: 0 12px 0 0;
}

.the-avatar-button {
  font-size: 0.65rem;
}

.lover-buttons {
  border-left: 0;
  border-right: 0;
}

.button-lover {
  font-size: 0.75rem;
  margin: 2px;
  width: 28%;
  max-width: 100px;
}

.the-avatar-button {
  margin-right: 5px;
}

hr {
  margin-bottom: 1.5rem;
}

i {
  margin-right: 0.2rem;
}
</style>
