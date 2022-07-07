<template>
  <div>
    <div>
      <div class="h-inner"
           :style="{backgroundImage:'url('+backgroundImageSrc+')'}">
        <div class="h-gradient"></div>
        <div class="h-user">
          <div class="h-info">
            <div>
              <div class="image is-64x64">
                <img class="is-rounded"
                     :src="userInfo.userAvatarPath">
              </div>
            </div>
            <div class="h-basic">
              <div>
                <span id="h-name">{{ userInfo.username }}</span>
                <a style="color: rgb(255, 255, 255); background-color: rgb(251, 114, 153);font-size: 0.2rem"
                   href="/userCenter/thirdLogin" target="_blank" class="h-level m-level">
                  {{ userInfo.userRole }}
                </a>
              </div>
              <div class="h-basic-spacing">
              <span
                    style="color: hsla(0,0%,100%,.8)"
                    class="h-sign is-size-7">
                {{ userInfo.userSignature }}
              </span>
              </div><!---->
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="myBox mt-5">
      <div v-show="userFollowed.length!=0" class="card">
        <header class="card-header">
          <p class="card-header-title is-size-5">
            关注的用户
            <b-button
              style="position: absolute;right: 0.5rem;"
              rounded
              size="is-small"
              type="is-light"
              label="更多"/>
          </p>
        </header>
        <div class="card-content">
          <div class="content">
            <div class="columns">
              <div v-for="item in userFollowed.slice(0,4)" :key="item.id" class="column">
                <a :href="'/userSpace/'+item.id" target="_blank">
                <div class=" media">
                  <div class="media-left">
                    <div class="image is-48x48">
                      <img class="is-rounded"
                           :src="'/img2d'+item.userAvatarPath">
                    </div>
                  </div>
                  <div class="media-content">
                    <p class="title is-5">{{ item.username }}</p>
                    <p style="width:120px;white-space:nowrap;overflow:hidden; text-overflow:ellipsis;" class="subtitle is-7">{{ item.userSignature }}</p>
                  </div>
                </div>
                </a>
              </div>
            </div>
            <div class="columns">
              <div v-for="item in userFollowed.slice(4,8)" :key="item.id" class="column">
                <div class=" media">
                  <div class="media-left">
                    <div class="image is-48x48">
                      <img class="is-rounded"
                           :src="$store.state.apiHost+item.userAvatarPath"
                           alt="Placeholder image">
                    </div>
                  </div>
                  <div class="media-content">
                    <p class="title is-5">{{ item.username }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <space-image-card label="最新的作品" type="LATEST"/>
      <div v-show="$route.params.userId==$store.state.loggedUserId">
        <space-image-card label="赞过的图片" type="LIKE"/>
        <space-image-card label="收藏的图片" type="COLLECTION"/>
      </div>
    </div>
  </div>
</template>

<script>
import spaceImageCard from "@/components/userCenter/userSpace/spaceImageCard"
import {get} from "@/untils/request"

export default {
  name: "space.vue",
  components: {spaceImageCard},
  data() {
    return {
      backgroundImageSrc: require("@/assets/userSpace/banner.webp"),
      avatarPath: require("@/assets/userSpace/test.webp"),
      userInfo: {
        username: "",
        userAvatarPath: "",
        userSignature: "",
        userRole: ""
      },
      userFollowed: []
    }
  },
  created() {
    this.getMime()
  },
  methods: {
    getMime() {
      get("/img2d/userCenter/getSpaceInfo/" + this.$route.params.userId).then(({data}) => {
        if (data.flag) {
          this.userInfo = data.data
          this.userInfo.userAvatarPath = "/img2d" + this.userInfo.userAvatarPath
          if (this.$route.params.userId == this.$store.state.loggedUserId) {
            this.getBaseSpaceInfo()
          }
        }
      })
    },
    getBaseSpaceInfo() {
      get("/img2d/userCenter/loggedUser/getFollowList").then(({data}) => {
        if (data.flag) {
          if (data.data){
            this.userFollowed = data.data
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.h-inner {
  background-position: 50%;
  background-size: cover;
  transition: background-image .2s ease, background-size 1s ease;
  padding-top: 116px;
  position: relative;
}

.h-gradient {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 84px;
  background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABQAAAABdCAMAAADNEjtLAAABEVBMVEUDAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAAADAACnjvHuAAAAW3RSTlMBAgMEBQYHCAkKCwwNDxAREhQVFhgZGxweHyEiJCUnKSosLS8xMjQ2ODk7PT5AQkNFR0hKTE5PUVNUVldZW1xeX2FiZGVnaGprbG5vcHFzdHV2d3h5ent8fX5/XoZ4lgAAAPVJREFUeAHt1AERADAQAiDdrX/mDyKEIAAAjGkDsOk3AAIEECCAAAEECCBAAAECCBBAgAACBBAggAABBAggQAABAggQQIAAAgQQIIAAAQQIIEAAAQIIEECAAAIEECCAAAEECAgQQIAAAgQQIIAAAQQIIEAAAQIIEECAAAIEECCAAAEECCBAAAECCBBAgAACBBAggAABBAggQAABAggQQIAAAgQQIIAAAQECCBBAgAACBBAggAABBAggQAABAggQQIAAAgQQIIAAAQQIIEAAAQIIEECAAAIEECCAAAEECCBAAAECCBBAgAACBAQIIEAAAQIseAEYdZ9sAcJDX4ICAAAAAElFTkSuQmCC);
  background-repeat: repeat-x;
}

.h-info {
  margin-left: 20px;
  padding-bottom: 16px;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-align: center;
  align-items: center;
}

.h-user {
  position: relative;
  z-index: 1;
}

.h-basic {
  color: #fff;
  font-size: 0;
  margin-left: 22px;
}

#h-name {
  display: inline-block;
  margin-right: 5px;
  font-weight: 700;
  line-height: 18px;
  font-size: 1.75rem;
  vertical-align: middle;
}


.m-level {
  height: 16px;
  line-height: 16px;
  border-radius: 4px;
  font-size: 12px;
  color: #fff;
  padding: 0 5px;
  background-color: #f45a8d;
  vertical-align: middle;
  margin-right: 5px;
  cursor: pointer;
}

.h-sign {
  background: transparent;
  border-radius: 4px;
  border: none;
  box-shadow: none;
  color: hsla(0, 0%, 100%, .8);
  line-height: 40px;
  height: 26px;
  margin-left: -5px;
  padding: 0 5px;
  position: relative;
  top: -1px;
  font-weight: 400;
  width: 20px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: clip;
  -o-text-overflow: clip;
}

.myBox {
  background-color: white;
  border-radius: 6px;
  -webkit-box-shadow: 0 0.5em 1em -0.125em rgb(10 10 10 / 10%), 0 0px 0 1px rgb(10 10 10 / 2%);
  box-shadow: 0 0.5em 1em -0.125em rgb(10 10 10 / 10%), 0 0px 0 1px rgb(10 10 10 / 2%);
  color: #4a4a4a;
  display: block;
}

</style>
