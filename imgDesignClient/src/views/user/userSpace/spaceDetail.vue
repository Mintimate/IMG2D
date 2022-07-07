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
                  {{userInfo.userRole}}
                </a>
              </div>
              <div class="h-basic-spacing">
              <span title="我们也不知道，这样做冷门教程并用爱发电能持续多久……好在，总算帮到一些人:)
QQ：198330181
接受付费辅助和商务合作^_^ "
                    style="color: hsla(0,0%,100%,.8)"
                    class="h-sign is-size-7">
                {{userInfo.userSignature}}
              </span>
              </div><!---->
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="myBox mt-5">
      <space-image-detail-card label="收藏图片" :type="$route.params.type"/>
    </div>
  </div>

</template>

<script>
import spaceImageDetailCard from "@/components/userCenter/userSpace/spaceImageDetailCard"
import {get} from "@/untils/request"
export default {
  name: "spaceDetail",
  components:{spaceImageDetailCard},
  data(){
    return{
      backgroundImageSrc: require("@/assets/userSpace/banner.webp"),
      avatarPath: require("@/assets/userSpace/test.webp"),
      userInfo:{
        username:"",
        userAvatarPath:"",
        userSignature:"",
        userRole:""
      },
    }
  },
  created() {
    this.getMime()
  },
  methods:{
    getMime(){
      get("/img2d/userCenter/getSpaceInfo/"+this.$route.params.userId).then(({data})=>{
        if (data.flag){
          this.userInfo=data.data
          this.userInfo.userAvatarPath="/img2d"+this.userInfo.userAvatarPath
        }
      })
    },
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
