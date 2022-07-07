<template>
  <section>
    <b-table
      :data="PostDetailList"
      ref="table"
      paginated
      per-page="10"
      detailed
      detail-key="id"
      show-detail-icon
      aria-next-label="Next page"
      aria-previous-label="Previous page"
      aria-page-label="Page"
      checkable
      :checked-rows.sync="checkItems"
      checkbox-position="left"
      aria-current-label="Current page">

      <b-table-column centered field="cateName" label="文章分类" sortable v-slot="props">
        {{ props.row.cateName }}
      </b-table-column>

      <b-table-column field="username" label="上传用户" sortable v-slot="props">
        {{ props.row.username }}
      </b-table-column>

      <b-table-column field="createTime" label="上传时间" sortable centered v-slot="props">
                  <span class="tag is-success">
                      {{ new Date(props.row.createTime).toLocaleDateString() }}
                  </span>
      </b-table-column>

      <b-table-column field="imgName" label="文章标题" sortable centered v-slot="props">
        {{ props.row.articleTitle }}
      </b-table-column>
      <b-table-column centered field="isAudit" label="状态" sortable v-slot="props">
        <span class="tag" :class="[props.row.isDelete != 0 ? 'is-danger' : '' +
         'is-success']">
                  {{ props.row.isDelete != 0 ? "未审核" : "通过" }}
        </span>
      </b-table-column>

      <template #detail="props">
        <b-field>
          <b-taglist>
            <b-tag v-for="tag in props.row.tagDTOList" :key="tag.id">
              <b-icon
                icon="tag"
                size="is-small">
              </b-icon>
              <span>{{ tag.tagName }}</span>
            </b-tag>
          </b-taglist>
        </b-field>
        <v-md-editor mode="preview" v-model="props.row.articleContent"></v-md-editor>
      </template>
      <template #bottom-left>
        <div class="field is-grouped">
          <p class="control">
            <b-button
              label="通过审核"
              @click="auditPostList(0)"
              class="button is-link"/>
          </p>
          <p class="control">
            <b-button
              label="吊销审核"
              @click="auditPostList(1)"
              class="button is-danger"/>
          </p>
          <b-modal
            destroy-on-hide
            :active="newInfoValue.changeTag"
            width="25rem"
          >
            <update-tags :checkItem="checkItems"
                         @success="updateSingleIMG"
                         @close="newInfoValue.changeTag=false"></update-tags>
          </b-modal>
          <p class="control">
            <b-button
              @click="newInfoValue.changeTag=!newInfoValue.changeTag"
              label="修改标签"
              :disabled="checkItems.length!=1"
              class="button is-info"/>

          </p>
          <b-modal
            destroy-on-hide
            :active="newInfoValue.changeCate"
            width="25rem"
          >
            <update-cate :checkItem="checkItems"
                         @success="updateSingleIMG"
                         @close="newInfoValue.changeCate=false"></update-cate>
          </b-modal>
          <p class="control">
            <b-button
              @click="newInfoValue.changeCate=!newInfoValue.changeCate"
              label="修改分类"
              :disabled="checkItems.length!=1"
              class="button is-info"/>
          </p>
          <b-modal
            destroy-on-hide
            :active="newInfoValue.changeName"
            width="25rem"
          >
            <update-post-name :checkItem="checkItems"
                         @success="updateSingleIMG"
                         @close="newInfoValue.changeName=false"></update-post-name>
          </b-modal>
          <p class="control">
            <b-button
              @click="newInfoValue.changeName=!newInfoValue.changeName"
              label="重命名"
              :disabled="checkItems.length!=1"
              class="button is-info"/>
          </p>
          <p class="control">
            <b-button
              label="修改重构"
              @click="openSeniorChange"
              :disabled="checkItems.length!=1"
              class="button is-warning"/>
          </p>
        </div>
      </template>
    </b-table>
  </section>
</template>

<script>
import {get, post} from "@/untils/request"
import {successTips} from "@/untils/tipsInfoUntil";
import updateTags from "@/components/adminAudit/post/updatePostTags"
import updateCate from "@/components/adminAudit/post/updatePostCate"
import updatePostName from "@/components/adminAudit/post/updatePostName"
export default {
  name: "auditPost",
  components:{updateTags,updateCate,updatePostName},
  data() {
    return {
      newInfoValue: {
        changeTag: false,
        changeCate: false,
        changeName:false
      },
      PostDetailList: [
        {
          id:"",
          userId:"",
          username:"",
          cateName:"",
          articleBanner:"",
          articleTitle:"",
          articleDesc:"",
          articleContent:"",
          type:"",
          originalUrl:"",
          createTime:"",
          updateTime:"",
          isDelete:"",
          tagDTOList: [
            {
              id: "",
              tagName: ""
            }
          ]
        },
      ],
      checkItems: [],
    }
  },
  mounted() {
    this.getLatestData()
  },
  methods:{
    getLatestData(){
      get("/img2d/article/admin/getAllInfo").then(({data}) => {
        this.PostDetailList = data.data
      })
    },
    openSeniorChange(){
      window.open("/discuss/fixPost/"+this.checkItems[0].id,"_blank")
    },
    auditPostList(state) {
      this.checkItems.forEach(item => {
        item.isDelete = state
      })
      post("/img2d/article/admin/auditPost/" + state, this.checkItems).then(({data}) => {
        if (data.flag){
          successTips("操作成功")
        }
      })
    },
    // 对图片内容更新后，重新获取最新数据
    updateSingleIMG(){
      successTips("数据更新成功，正在获取最新数据")
      this.getLatestData()
      this.checkItems=[]
      this.newInfoValue.changeTag=false
      this.newInfoValue.changeCate=false
      this.newInfoValue.changeName=false
    }
  }
}
</script>

<style scoped>

</style>
