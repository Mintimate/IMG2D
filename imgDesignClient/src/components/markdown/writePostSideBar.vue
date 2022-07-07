<template>
  <section>
    <b-sidebar
      type="is-light"
      fullheight
      right
      :on-cancel="closeSideBar"
      v-model="SidebarShow"
    >
      <div class="p-1">
        <b-menu>
          <b-menu-list label="发布确认">
            <b-menu-item icon="cog" expanded>
              <template #label="props">
                文章分类
                <b-icon class="is-pulled-right" :icon="props.expanded ? 'menu-down' : 'menu-up'"></b-icon>
              </template>
              <b-field v-for="item in categoryList" :key="item.id">
                <b-radio name="postCategory"
                         v-model="sideBar.postCategory"
                         :native-value="item.cateName">
                  {{ item.cateName }}
                </b-radio>
              </b-field>
            </b-menu-item>
            <b-menu-item icon="account" expanded label="文章来源">
              <b-field>
                <b-radio v-model="sideBar.postType"
                         name="type"
                         native-value=0
                         type="is-success">
                  转载
                </b-radio>
              </b-field>
              <b-field>
                <b-input v-model="sideBar.originalUrl"/>
              </b-field>
              <b-field>
                <b-radio v-model="sideBar.postType"
                         name="type"
                         native-value=1
                         type="is-success">
                  原创
                </b-radio>
              </b-field>
            </b-menu-item>
          </b-menu-list>
          <b-menu-list>
            <b-menu-item label="文章描述" expanded icon="message-bulleted">
              <b-field>
                <b-input v-model="sideBar.postDesc" maxlength="100" type="textarea"></b-input>
              </b-field>
            </b-menu-item>
          </b-menu-list>
          <b-menu-list>
            <b-menu-item label="自定义标签" expanded icon="tag">
              <b-field>
                <b-taginput
                  v-model="tagNames"
                  ellipsis
                  size="is-small"
                  icon="label"
                  placeholder="输入标签"
                  aria-close-label="Delete this tag">
                </b-taginput>
              </b-field>
            </b-menu-item>
            <b-menu-item label="选择文章封面" expanded icon="image">
              <chooseIMG v-bind:dropFiles.sync="sideBar.postBanner"/>
            </b-menu-item>
          </b-menu-list>
          <b-menu-list label="操作">
            <b-field>
              <b-button @click="commitArticle" type="is-danger" expanded>确定发布</b-button>
            </b-field>
            <b-field>
              <b-button @click="SidebarShow=false" type="is-light" expanded>收起菜单</b-button>
            </b-field>
          </b-menu-list>
        </b-menu>
      </div>
    </b-sidebar>
  </section>
</template>

<script>
import chooseIMG from "@/components/imgTool/choseIMG"
import {get} from "@/untils/request"
// import {isEmpty} from "@/untils/valueUntil";

export default {
  name: "writePostSideBar",
  components: {chooseIMG},
  props: {
    show: {
      type: Boolean,
      default: false
    },
    fatherSideBar: {
      type: Object,
      default: () => {
      }
    }
  },
  created() {
    this.getListArticleCategory()
  },
  data() {
    return {
      showSourceInput:false,
      categoryList: [{
        cateName: "",
        id: ""
      }],
    }
  },
  computed: {
    SidebarShow: {
      get() {
        return this.show;
      },
      set() {
        this.$emit('close', this.sideBar)
      }
    },
    sideBar: {
      get() {
        let fatherSideBar = this.fatherSideBar ? this.fatherSideBar : {}
        let sideBar = {
          postDesc: this.fatherSideBar.articleDesc,
          postCategory: this.fatherSideBar.cateName,
          postType: this.fatherSideBar.type,
          originalUrl:this.fatherSideBar.originalUrl,
          postTags: this.tagNames,
          postBanner: this.fatherSideBar.articleBanner
        }
        return Object.assign({}, sideBar, fatherSideBar)
      },
      set(newValue){
        newValue.postType=='1'?this.showSourceInput=true:this.showSourceInput=false
      }
    },
    tagNames: {
      get() {
        let fatherSideBar = this.fatherSideBar ? this.fatherSideBar : {}
        let tags = []
        if (fatherSideBar.tagDTOList != undefined && fatherSideBar.tagDTOList.length != 0) {
          fatherSideBar.tagDTOList.forEach(item => {
            tags.push(item.tagName)
          })
        }
        return tags
      },
      set(value) {
        this.sideBar.postTags = value
      }
    }
  },
  methods: {
    commitArticle() {
      this.$emit("commit",this.sideBar)
    },
    closeSideBar() {
      this.$emit('close', this.sideBar)
    },
    getListArticleCategory() {
      get("/img2d/articleCategory/getCategoryList").then(({data}) => {
        this.categoryList = data.data
        this.sideBar.postCategory = data.data[0].cateName
      })
    },
  }
}
</script>

<style>
.b-sidebar .sidebar-content {
  width: 30%;
}

@media screen and (max-width: 1024px) {
  .b-sidebar .sidebar-content {
    width: 75%;
  }
}

</style>
