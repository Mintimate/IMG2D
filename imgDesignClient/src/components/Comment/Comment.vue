<template>
  <div>
    <div class="comment-title"><i class="iconfont icon-pinglun"/>评论</div>
    <!-- 评论框 -->
    <div class="comment-input-wrapper">
      <div style="display:flex">
        <figure class="image is-128x128">
          <img
            class="is-rounded"
            v-if="this.$store.state.loggedUserAvatarPath"
            :src="this.$store.state.loggedUserAvatarPath"
          />
          <img
            class="image is-128x128"
            v-else
            :src="defaultAvatar"
          />
        </figure>
        <div class="ml-3" style="width:100%">
          <div class="comment-input">
            <textarea
              v-model="commentContent"
              auto-grow
              class="comment-textarea"
              dense
              placeholder="留下点什么吧..."
            />
          </div>
          <!-- 操作按钮 -->
          <div class="emoji-container">
            <span
              :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'"
              @click="chooseEmoji = !chooseEmoji"
            >
              <i style="font-size: 1.5rem" class="iconfont icon-EmojiAdd"/>
            </span>
            <button
              class="upload-btn v-comment-btn"
              style="margin-left:auto"
              @click="insertComment"
            >
              提交
            </button>
          </div>
          <!-- 表情框 -->
          <emoji :chooseEmoji="chooseEmoji" @addEmoji="addEmoji"/>
        </div>
      </div>
    </div>
    <!-- 评论详情 -->
    <div v-if="count > 0 && reFresh">
      <!-- 评论数量 -->
      <div class="count">{{ count }} 评论</div>
      <!-- 评论列表 -->
      <div
        v-for="(item, index) of commentList"
        :key="item.id"
        class="pt-5"
        style="display:flex"
      >
        <!-- 头像 -->
        <figure class="image is-48x48 comment-avatar">
          <img class="is-rounded" :src="'/img2d'+item.userAvatarPath"/>
        </figure>
        <div class="comment-meta">
          <!-- 用户名 -->
          <div class="comment-user">
            <span v-if="!item.userSpace">{{ item.username }}</span>
            <a v-else :href="item.userSpace" target="_blank">
             {{ item.username }}
            </a>
            <span v-if="item.userId == userId" class="blogger-tag">博主</span>
          </div>
          <!-- 信息 -->
          <div class="comment-info">
            <!-- 楼层 -->
            <span style="margin-right:10px">{{ count - index }}楼</span>
            <!-- 发表时间 -->
            <span style="margin-right:10px">{{ new Date(item.createTime).toLocaleDateString() }}</span>
            <!-- 点赞 -->
            <!--            <span-->
            <!--              :class="isLike(item.id) + ' iconfont icondianzan'"-->
            <!--              @click="like(item)"-->
            <!--            />-->
            <!--            <span v-show="item.likeCount > 0"> {{ item.likeCount }}</span>-->
            <!-- 回复 -->
            <span class="reply-btn" @click="replyComment(index, item)">
              回复
            </span>
          </div>
          <!-- 评论内容 -->
          <p class="comment-content" v-html="item.commentContent"></p>
          <!-- 回复人 -->
          <div
            v-for="reply of item.replyDTOList"
            :key="reply.id"
            style="display:flex"
          >
            <!-- 头像 -->
            <figure class="image is-32x32 comment-avatar">
              <img class="is-rounded" :src="'/img2d'+reply.userAvatarPath"/>
            </figure>
            <div class="reply-meta">
              <!-- 用户名 -->
              <div class="comment-user">
                <span v-if="!reply.userSpace">{{ reply.username }} </span>
                <a v-else :href="reply.userSpace" target="_blank">
                  {{ reply.username }}
                </a>
                <span v-if="item.userId == userId" class="blogger-tag">博主</span>
              </div>
              <!-- 信息 -->
              <div class="comment-info">
                <!-- 发表时间 -->
                <span style="margin-right:10px">
                  {{ new Date(reply.createTime).toLocaleDateString() }}
                </span>
                <!-- 点赞 -->
                <!--                <span-->
                <!--                  :class="isLike(reply.id) + ' iconfont icondianzan'"-->
                <!--                  @click="like(reply)"-->
                <!--                />-->
                <!--                <span v-show="reply.likeCount > 0"> {{ reply.likeCount }}</span>-->
                <!-- 回复 -->
                <span class="reply-btn" @click="replyComment(index, reply)">
                  回复
                </span>
              </div>
              <!-- 回复内容 -->
              <p class="comment-content">
                <!-- 回复用户名 -->
                <template v-if="reply.replyUserId != item.userId">
                  <span v-if="!reply.userSpace" class="ml-1">
                    @{{ reply.replyUsername }}
                  </span>
                  <a
                    v-else
                    :href="reply.replyUserSpace"
                    class="comment-nickname ml-1"
                    target="_blank"
                  >
                    @{{ reply.replyUsername }}
                  </a>
                  ，
                </template>
                <span v-html="reply.commentContent"/>
              </p>
            </div>
          </div>
          <!-- 回复数量 -->
          <div
            v-show="item.replyCount > 3"
            ref="check"
            class="mb-3"
            style="font-size:0.75rem;color:#6d757a"
          >
            共
            <b>{{ item.replyCount }}</b>
            条回复，
            <span
              style="color:#00a1d6;cursor:pointer"
              @click="checkReplies(index, item)"
            >
              点击查看
            </span>
          </div>
          <!-- 回复分页 -->
          <div
            ref="paging"
            class="mb-3"
            style="font-size:0.75rem;color:#222;display:none"
          >
            <span style="padding-right:10px">
              共{{ Math.ceil(item.replyCount / 5) }}页
            </span>
            <paging
              ref="page"
              :commentId="item.id"
              :index="index"
              :totalPage="Math.ceil(item.replyCount / 5)"
              @changeReplyCurrent="changeReplyCurrent"
            />
          </div>
          <!-- 回复框 -->
          <Reply ref="reply" @reloadReply="reloadReply"/>
        </div>
      </div>
      <!-- 加载按钮 -->
      <div class="load-wrapper">
        <b-button expanded rounded class="is-info is-light" v-if="count > commentList.length"  @click="listComments">
          加载更多...
        </b-button>
      </div>
    </div>
    <!-- 没有评论提示 -->
    <div v-else style="padding:1.25rem;text-align:center">
      来发评论吧~
    </div>
  </div>
</template>

<script>
import Reply from "./Reply/Repy";
import Paging from "./Paging/Paging";
import Emoji from "./Emoji/Emoji";
import EmojiList from "@/assets/emoji/js/emoji";
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";
import {post, get} from "@/untils/request"

export default {
  components: {
    Reply,
    Emoji,
    Paging
  },
  props: {
    postId: {
      type: String
    },
    postType: {
      type: String
    },
    commentList: {
      type: Array
    },
    count: {
      type: Number
    },
    userId:{
      type:String
    }
  },
  data: function () {
    return {
      defaultAvatar: require("@/assets/loading/default_avatar.jpg"),
      reFresh: true,
      imgCommentHref: "",
      articleCommentHref: "",
      commentContent: "",
      chooseEmoji: false,
      current: 1
    };
  },
  methods: {
    replyComment(index, item) {
      this.$refs.reply.forEach(item => {
        item.$el.style.display = "none";
      });
      //传值给回复框
      this.$refs.reply[index].commentContent = "";
      this.$refs.reply[index].username = item.username;
      this.$refs.reply[index].replyUserId = item.userId;
      this.$refs.reply[index].parentId = this.commentList[index].id;
      this.$refs.reply[index].chooseEmoji = false;
      this.$refs.reply[index].index = index;
      this.$refs.reply[index].postType = this.postType
      this.$refs.reply[index].$el.style.display = "block";
    },
    addEmoji(key) {
      this.commentContent += key;
    },
    checkReplies(index, item) {
      get("/img2d/comments/get/" + this.getHref + "/" + item.id + "/1" + "/replies",)
        .then(({data}) => {
          this.$refs.check[index].style.display = "none";
          item.replyDTOList = data.data;
          //超过1页才显示分页
          if (Math.ceil(item.replyCount / 5) > 1) {
            this.$refs.paging[index].style.display = "flex";
          }
        });
    },
    changeReplyCurrent(current, index, commentId) {
      //查看下一页回复
      get("/img2d/comments/get/" + this.getHref + "/" + commentId + "/" + current + "/replies",)
        .then(({data}) => {
          this.commentList[index].replyDTOList = data.data;
        });
    },
    listComments() {
      //查看下一页评论
      this.current++;
      const path = this.$route.path;
      const arr = path.split("/");
      this.axios
        .get("/api/comments", {
          params: {current: this.current, articleId: arr[2]}
        })
        .then(({data}) => {
          this.commentList.push(...data.data.recordList);
        });
    },
    insertComment() {
      //判断登录
      if (!this.$store.state.logged) {
        this.$store.state.loginFlag = true;
        return false;
      }
      //判空
      if (this.commentContent.trim() == "") {
        warningTips("评论不能为空嗷(･_･;")
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
      //发送请求
      let comment = {
        articleImgId: this.postId,
        commentContent: this.commentContent
      };
      console.log(comment)
      this.commentContent = "";
      post("/img2d/comments/add/" + this.addHref, comment).then(({data}) => {
        if (data.flag) {
          //查询最新评论
          this.$emit("reloadComment");
          successTips("评论成功～～")
        } else {
          failTips("评论失败，请咨询管理员")
        }
      });
    },
    // like(comment) {
    //   // 判断登录
    //   if (!this.$store.state.userId) {
    //     this.$store.state.loginFlag = true;
    //     return false;
    //   }
    //   // 发送请求
    //   this.axios
    //     .post("/api/comments/" + comment.id + "/like")
    //     .then(({data}) => {
    //       if (data.flag) {
    //         // 判断是否点赞
    //         if (this.$store.state.commentLikeSet.indexOf(comment.id) != -1) {
    //           this.$set(comment, "likeCount", comment.likeCount - 1);
    //         } else {
    //           this.$set(comment, "likeCount", comment.likeCount + 1);
    //         }
    //         this.$store.commit("commentLike", comment.id);
    //       }
    //     });
    // },
    reloadReply(index) {
      get("/img2d/comments/get/" + this.getHref + "/" + this.commentList[index].id + "/" + this.$refs.page[index].current + "/replies")
        .then(({data}) => {
          this.commentList[index].replyCount++;
          //回复大于5条展示分页
          if (this.commentList[index].replyCount > 5) {
            this.$refs.paging[index].style.display = "flex";
          }
          this.$refs.check[index].style.display = "none";
          this.$refs.reply[index].$el.style.display = "none";
          this.commentList[index].replyDTOList = data.data;
        })
    }
  },
  computed: {
    addHref() {
      if (this.postType == undefined || this.postType == "" || this.postType == "article") {
        return "toArticle";
      } else {
        return "toIMG";
      }
    },
    getHref() {
      if (this.postType == undefined || this.postType == "" || this.postType == "article") {
        return "byArticleId";
      } else {
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
  watch: {
    commentList() {
      this.reFresh = false;
      this.$nextTick(() => {
        this.reFresh = true;
      });
    }
  }
};
</script>

<style scoped>
@import "../../assets/icon/iconfront.css";

p {
  margin-bottom: 1.25rem !important;
}

.blogger-tag {
  background: #ffa51e;
  font-size: 12px;
  display: inline-block;
  border-radius: 2px;
  color: #fff;
  padding: 0 5px;
  margin-left: 6px;
}

.emoji-container {
  margin: 10px 0;
  display: flex;
  align-items: center;
}

.comment-input {
  position: relative;
}

.commmen-img {
  position: absolute;
  right: 0;
  bottom: 0;
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

.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: bold;
  line-height: 40px;
  margin-bottom: 10px;
}

.comment-title i {
  font-size: 1.5rem;
  margin-right: 5px;
}

.comment-input-wrapper {
  border: 1px solid rgba(144, 147, 153, 0.29);
  border-radius: 12px;
  padding: 10px;
  margin: 0 0 10px;
}

.count {
  padding: 5px;
  line-height: 1.75;
  font-size: 1.25rem;
  font-weight: bold;
}

.comment-meta {
  margin-left: 0.8rem;
  width: 100%;
  border-bottom: 1px dashed #f5f5f5;
}

.reply-meta {
  margin-left: 0.8rem;
  width: 100%;
}

.comment-user {
  font-size: 14px;
  line-height: 1.75;
}

.comment-user a {
  color: #1abc9c !important;
  font-weight: 500;
  transition: 0.3s all;
}

.comment-nickname {
  text-decoration: none;
  color: #1abc9c !important;
  font-weight: 500;
}

.comment-info {
  line-height: 1.75;
  font-size: 0.75rem;
  color: #b3b3b3;
}

.reply-btn {
  cursor: pointer;
  float: right;
  color: #ef2f11;
}

.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
}

.comment-avatar {
  transition: all 0.5s;
}

.comment-avatar:hover {
  transform: rotate(360deg);
}

.like {
  cursor: pointer;
  font-size: 0.875rem;
}

.like:hover {
  color: #eb5055;
}

.like-active {
  cursor: pointer;
  font-size: 0.875rem;
  color: #eb5055;
}

.load-wrapper {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}

.upload-btn {
  color: #fff;
  margin-left: 1rem;
  background-color: #49b1f5;
}

.upload-btn:hover {
  background-color: #ff7242;
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
</style>
