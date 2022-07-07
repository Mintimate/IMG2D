<template>
  <div class="card mt-3">
    <header class="card-header">
      <p class="card-header-title is-size-5">
        {{ label }}
        <template v-if="type=='LIKE'">
          <b-tooltip label="不支持查看更多点赞信息"
                     class="more-tag"
                     position="is-left"
                     append-to-body
                     :animated="false">
            <b-button
              disabled
              rounded
              size="is-small"
              type="is-light"
              label="更多"/>
          </b-tooltip>
        </template>
        <template v-else-if="type=='LATEST'">
          <b-button
            rounded
            class="more-tag-second"
            size="is-small"
            type="is-primary"
            @click="openSearch"
            label="查看文章"/>
          <b-tooltip label="不支持查看更多点赞信息"
                     class="more-tag"
                     position="is-left"
                     append-to-body
                     :animated="false">
            <b-button
              disabled
              rounded
              size="is-small"
              type="is-light"
              label="更多"/>
          </b-tooltip>
        </template>
        <template v-else>
          <b-tooltip label="只支持查看自己的更多收藏"
                     :active="whoSpace!=$store.state.loggedUserId"
                     class="more-tag"
                     position="is-left"
                     append-to-body
                     :animated="false">
            <b-button
              :disabled="whoSpace!=$store.state.loggedUserId"
              @click="MoreCollectDetail"
              rounded
              size="is-small"
              type="is-light"
              label="更多"/>
          </b-tooltip>
        </template>
      </p>
    </header>
    <div class="card-content">
      <template v-show="listIMG.length!=0">
                <waterfallSlim :images_info="listIMG"/>
      </template>
    </div>
  </div>
</template>

<script>
import waterfallSlim from "@/components/displayIMG/waterfallSlim"
import {productSnowKey} from "@/untils/snowKey";
import {get} from "@/untils/request"

export default {
  name: "spaceImageCard",
  props: ["label", "type"],
  components: {waterfallSlim},
  data() {
    return {
      listIMG: [
        // {
        //   id: "",
        //   src: "",
        //   title: "",
        //   author: "",
        //   authorAvatarPath: ""
        // }
      ],
      whoSpace: "",
    }
  },
  created() {
    this.whoSpace = this.$route.path.split("/")[2];
    this.$nextTick(this.Make())
  },
  methods: {
    Make() {
      if (this.type == "LIKE") {
        this.MoreLike()
      } else if (this.type == "LATEST") {
        this.UserRecentImages()
      } else {
        this.MoreCollection()
      }
    },
    UserRecentImages() {
      get("/img2d/img/userRecentImages/" + this.$route.params.userId)
        .then(({data}) => {
          const tempList = []
          const list = data.data
          if (data.data!=null){
            list.forEach(item => {
              tempList.push(
                {
                  id: productSnowKey(item.id),
                  src: this.$store.state.apiHost + item.imgThumbnailPath,
                  title: item.imgName,
                  author: item.username,
                  authorAvatarPath: item.userAvatarPath
                }
              )
            })
          }
          this.listIMG = tempList
        })
    },
    MoreLike() {
      if (this.$route.params.userId == this.$store.state.loggedUserId) {
        get("/img2d/img/userSpace/LikeIMG/")
          .then(({data}) => {
            const tempList = []
            if (data.data.count != 0) {
              const list = data.data.recordList
              if (list != undefined) {
                list.forEach(item => {
                  tempList.push(
                    {
                      id: productSnowKey(item.id),
                      src: this.$store.state.apiHost + item.imgThumbnailPath,
                      title: item.imgName,
                      author: item.username,
                      authorAvatarPath: item.userAvatarPath
                    }
                  )
                })
              }
            }
            this.listIMG = tempList
          })
      }

    },
    MoreCollection() {
      if (this.$route.params.userId == this.$store.state.loggedUserId) {
        get("/img2d/img/userSpace/CollectionIMG/")
          .then(({data}) => {
            const tempList = []
            if (data.data.count != 0) {
              const list = data.data.recordList
              if (list != undefined) {
                list.forEach(item => {
                  tempList.push(
                    {
                      id: productSnowKey(item.id),
                      src: this.$store.state.apiHost + item.imgThumbnailPath,
                      title: item.imgName,
                      author: item.username,
                      authorAvatarPath: item.userAvatarPath
                    }
                  )
                })
              }
            }
            this.listIMG = tempList
          })
      }

    },
    MoreCollectDetail() {
      window.open("/userSpace/" + this.$route.params.userId + "/Collect", "_self")
    },
    openSearch(){
      if(this.listIMG.length!=0){
        window.open("/searchArticle/?searchKeyWorld=" + this.listIMG[0].author, "_blank")
      }
    }
  }
}
</script>

<style scoped>
.more-tag {
  position: absolute;
  right: 0.5rem;
}

.more-tag-second {
  position: absolute;
  right: 4.5rem;
}
</style>
