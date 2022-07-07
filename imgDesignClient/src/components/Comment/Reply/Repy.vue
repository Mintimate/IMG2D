<template>
  <div ref="reply" class="reply-input-wrapper" style="display: none">
    <textarea
      v-model="commentContent"
      :placeholder="'回复 @' + username + '：'"
      auto-grow
      class="comment-textarea"
      dense
    />
    <div class="emoji-container">
      <span
        :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'"
        @click="chooseEmoji = !chooseEmoji"
      >
        <i style="font-size: 1.5rem" class="iconfont icon-EmojiAdd"/>
      </span>
      <div style="margin-left:auto">
        <button class="cancle-btn v-comment-btn" @click="cancleReply">
          取消
        </button>
        <button class="upload-btn v-comment-btn" @click="insertReply">
          提交
        </button>
      </div>
    </div>
    <!-- 表情框 -->
    <emoji :chooseEmoji="chooseEmoji" @addEmoji="addEmoji"></emoji>
  </div>
</template>

<script>
import Emoji from "../Emoji/Emoji";
import EmojiList from "@/assets/emoji/js/emoji";
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";
import {post} from "@/untils/request";

export default {
  components: {
    Emoji
  },
  props: ["postId"],
  data: function () {
    return {
      index: 0,
      chooseEmoji: false,
      username: "",
      replyUserId: null,
      parentId: null,
      commentContent: "",
      postType:""
    };
  },
  methods: {
    cancleReply() {
      this.$refs.reply.style.display = "none";
    },
    insertReply() {
      //判断登录
      if (!this.$store.state.logged) {
        warningTips("请先登录嗷～")
        return false;
      }
      if (this.commentContent.trim() == "") {
        warningTips("回复不能为空")
        return false;
      }
      //解析表情
      const reg = /\[.+?\]/g;
      this.commentContent = this.commentContent.replace(reg, function (str) {
        return (
          "<img src= '" +
          EmojiList[str] +
          "' width='22'height='20' style='padding: 0 1px'/>"
        );
      });
      let comment = {
        articleImgId: this.postId,
        replyUserId: this.replyUserId,
        parentId: this.parentId,
        commentContent: this.commentContent
      };
      this.commentContent = "";
      post("/img2d/comments/add/"+this.addHref, comment)
        .then(({data}) => {
          if (data.flag) {
            this.$emit("reloadReply", this.index);
            successTips("回复成功～～")
          } else {
            failTips("尴尬，回复失败；请重试或联系管理员")
          }
        }).catch(()=>{
        failTips("尴尬，回复失败；请重试或联系管理员")
    });
    },
    addEmoji(text) {
      this.commentContent += text;
    }
  },
  computed: {
    addHref(){
      if(this.postType==undefined||this.postType==""||this.postType=="article"){
        return "toArticle";
      }
      else{
        return "toIMG";
      }
    },
    getHref(){
      if(this.postType==undefined||this.postType==""||this.postType=="article"){
        return "byArticleId";
      }
      else{
        return "byIMG";
      }
    }
    // isLike() {
    //   return function (commentId) {
    //     var commentLikeSet = this.$store.state.commentLikeSet;
    //     return commentLikeSet.indexOf(commentId) != -1 ? "like-active" : "like";
    //   };
    // }
  },

};
</script>

<style scoped>
.reply-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}

.comment-textarea {
  font-size: 0.875rem;
  color: #555 !important;
  outline: none;
  padding: 10px 5px;
  min-height: 122px;
  resize: none;
  width: 100%;
  border-radius: 4px;
}

.upload-btn {
  color: #fff;
  margin-left: 1rem;
  background-color: #49b1f5;
}

.upload-btn:hover {
  background-color: #ff7242;
}

.cancle-btn {
  background-color: #90939910;
  border-color: #90939950;
  color: #4c4948;
}

.v-comment-btn {
  border: 1px solid #ededed;
  border-color: #fff;
  border-radius: 4px;
  text-align: center;
  padding: 6px 16px;
  font-size: 14px;
  transition: all 0.3s;
  outline: none;
}

.emoji-container {
  margin: 10px 0;
  display: flex;
  align-items: center;
}
</style>
