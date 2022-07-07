<template>
  <section>
    <b-table
      :data="commentList"
      ref="table"
      paginated
      per-page="10"
      aria-next-label="Next page"
      aria-previous-label="Previous page"
      aria-page-label="Page"
      aria-current-label="Current page"
      detailed
      detail-key="id"
      show-detail-icon
      checkable
      :checked-rows.sync="checkItems"
      checkbox-position="left">
      <b-table-column field="id" label="评论ID" numeric centered v-slot="props">
        <span :class="props.row.isDelete==1? 'isDelete':''">
          {{ props.row.id }}
        </span>
      </b-table-column>

      <b-table-column field="user.first_name" label="对应图片" sortable centered v-slot="props">
        <span :class="props.row.isDelete==1? 'isDelete':''">
          {{ props.row.articleImgId }}
        </span>
      </b-table-column>

      <b-table-column field="user.last_name" label="所属用户" sortable centered v-slot="props">
        <span :class="props.row.isDelete==1? 'isDelete':''">
          {{ props.row.username }}
        </span>
      </b-table-column>

      <b-table-column field="date" label="评论内容" sortable centered v-slot="props">
        <span :class="props.row.isDelete==1? 'isDelete':''">
          {{ props.row.commentContent }}
        </span>
      </b-table-column>

      <b-table-column field="id" label="评论时间"  numeric centered v-slot="props">
        <span :class="props.row.isDelete==1? 'isDelete':''">
          <span class="tag"> {{ new Date().toLocaleDateString(props.row.createTime) }}
          </span>
        </span>
      </b-table-column>

      <template #detail="props">
        <template v-if="props.row.replyDTOList==undefined||props.row.replyDTOList==null">
          <b-message class="m-0">
            空空如也嗷(*≧ω≦)～～～
          </b-message>
        </template>
        <template v-else>
          <b-table
            :data="props.row.replyDTOList"
            ref="table"
            paginated
            per-page="5"
            aria-next-label="Next page"
            aria-previous-label="Previous page"
            aria-page-label="Page"
            aria-current-label="Current page"
            checkable
            :checked-rows.sync="checkItems"
            checkbox-position="left">
            <b-table-column field="id" label="评论ID"  numeric centered v-slot="item">
              <span :class="item.row.isDelete==1? 'isDelete':''">{{ item.row.id }}</span>
            </b-table-column>
            <b-table-column field="id" label="评论者" numeric centered v-slot="item">
              <span :class="item.row.isDelete==1? 'isDelete':''">{{ item.row.username }}</span>
            </b-table-column>
            <b-table-column field="id" label="回复内容"  numeric centered v-slot="item">
              <span :class="item.row.isDelete==1? 'isDelete':''">{{ item.row.commentContent }}</span>
            </b-table-column>
            <b-table-column field="id" label="评论时间"  numeric centered v-slot="item">
             <span :class="item.row.isDelete==1? 'isDelete':''">
               <span class="tag"> {{ new Date().toLocaleDateString(item.row.createTime) }}
               </span>
             </span>
            </b-table-column>
          </b-table>
        </template>
      </template>

      <template #bottom-left>
        <div class="field is-grouped">
          <p class="control">
            <b-button
              label="取消删除"
              @click="deleteComments(0)"
              class="button is-link"/>
          </p>
          <p class="control">
            <b-button
              label="删除评论"
              @click="deleteComments(1)"
              class="button is-danger"/>
          </p>
        </div>
      </template>
    </b-table>
  </section>
</template>

<script>
import {get, post} from "@/untils/request";
import {successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "auditCommentImage",
  data() {
    return {
      commentList: [{
        id: "",
        articleImgId: "",
        userId: "",
        username: "",
        userAvatarPath: "",
        userSpace: "",
        commentContent: "",
        likeCount: "",
        createTime: "",
        replyCount: "",
        isDelete:"",
        replyDTOList: [
          {
            id: "",
            parentId: "",
            userId: "",
            username: "",
            userAvatarPath: "",
            userSpace: "",
            replyUserId: "",
            replyUsername: "",
            replyUserSpace: "",
            commentContent: "",
            likeCount: "",
            createTime: "",
            isDelete:""
          }
        ]
      }],
      checkItems:[],
    }
  },
  created() {
    this.getCommentData()
  },
  methods: {
    getCommentData() {
      get("/img2d/comments/admin/getAllForIMG").then(({data}) => {
        if (data.flag) {
          this.commentList = data.data
        }
      })
    },
    deleteComments(choose){
      post("/img2d/comments/admin/auditComments/Image/"+choose,this.checkItems).then(({data})=>{
        if(data.flag){
          successTips("操作成功")
          this.getCommentData()
        }
      })
    }
  }
}
</script>

<style scoped>
.isDelete{
  text-decoration:line-through
}
</style>
