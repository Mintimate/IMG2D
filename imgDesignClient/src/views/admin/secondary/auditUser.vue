<template>
  <section>
    <b-table
      :data="userList"
      ref="table"
      checkable
      :checked-rows.sync="checkItems"
      paginated
      per-page="10"
      detailed
      detail-key="id"
      show-detail-icon
      aria-next-label="Next page"
      aria-previous-label="Previous page"
      aria-page-label="Page"
      aria-current-label="Current page">

      <b-table-column centered field="cateName" label="用户ID" sortable v-slot="props">
        {{ props.row.id }}
      </b-table-column>

      <b-table-column field="username" label="用户昵称" sortable v-slot="props">
        {{ props.row.username }}
      </b-table-column>

      <b-table-column field="createTime" label="用户邮箱" sortable centered v-slot="props">
        {{ props.row.userEmail }}
      </b-table-column>

      <b-table-column field="imgName" label="用户认证" sortable centered v-slot="props">
        {{ props.row.userRole }}
      </b-table-column>
      <b-table-column centered field="isAudit" label="状态" sortable v-slot="props">
        <span class="tag" :class="props.row.isBan==1 ? 'is-danger':'is-success'">
          {{ props.row.isBan == 1 ? '封禁' : '正常' }}
        </span>
      </b-table-column>

      <template #detail="props">
        <article class="media">
          <figure class="media-left">
            <p class="image is-128x128">
              <img class="is-rounded" :src="props.row.userAvatarPath">
            </p>
          </figure>
          <div class="media-content">
            <div class="content">
              <ul class="block">
                <dl>
                  用户电话：{{ props.row.userTele }}
                </dl>
                <dl>
                  用户个性签名：{{ props.row.userSignature }}
                </dl>
                <dl>
                  用户个人空间：{{ props.row.userSpace }}
                </dl>
              </ul>
            </div>
          </div>
        </article>
      </template>
      <template #bottom-left>
        <div class="field is-grouped">
          <p class="control">
            <b-button
              :disabled="checkItems.length==0"
              @click="auditUser(0)"
              label="解禁账号"
              class="button is-link"/>
          </p>
          <p class="control">
            <b-button
              :disabled="checkItems.length==0"
              @click="auditUser(1)"
              label="封禁账号"
              class="button is-danger"/>
          </p>
          <p class="control">
            <b-modal
              @close="activeItem.updateBaseInfo=false"
              :active="activeItem.updateBaseInfo">
              <update-user-base
                @close="activeItem.updateBaseInfo=false"
                @finish="getLatestData()"
                :new-user-info.sync="checkItems"/>
            </b-modal>
            <b-button
              :disabled="checkItems.length!=1"
              @click="activeItem.updateBaseInfo=!activeItem.updateBaseInfo"
              label="修改基本信息"
              class="button is-info"/>
          </p>
          <p class="control">
            <b-modal
              @close="activeItem.updateSenior=false"
              width="28rem"
              :active="activeItem.updateSenior">
              <update-user-senior
                @close="activeItem.updateSenior=false"
                @finish="getLatestData()"
                :userinfo="checkItems[0]"/>
            </b-modal>
            <b-button
              :disabled="checkItems.length!=1"
              @click="activeItem.updateSenior=!activeItem.updateSenior"
              label="修改高级信息"
              class="button is-info"/>
          </p>
        </div>
      </template>
    </b-table>
  </section>
</template>

<script>
import {get, post} from "@/untils/request"
import {successTips} from "@/untils/tipsInfoUntil";
import updateUserBase from "@/components/adminAudit/user/updateUserBase"
import updateUserSenior from "@/components/adminAudit/user/updateUserSenior"

export default {
  name: "auditUser",
  components: {
    updateUserBase,
    updateUserSenior
  },
  data() {
    return {
      activeItem: {
        updateBaseInfo: false,
        updateSenior: false
      },
      userList: [{
        id: "",
        username: "",
        userEmail: "",
        userRole: "",
        isBan: "",
        userAvatarPath: "",
        userTele: "",
        userSignature: "",
        userSpace: ""
      }],
      checkItems: []
    }
  },
  mounted() {
    this.getLatestData()
  },
  methods: {
    auditUser(type) {
      post("/img2d/userCenter/admin/auditUser/" + type, this.checkItems).then(({data}) => {
        if (data.flag) {
          this.getLatestData()
          successTips("修改成功")
        }
      })
    },
    getLatestData() {
      get("/img2d/userCenter/admin/getUserInfo").then(({data}) => {
        if (data.flag) {
          this.userList = data.data
          this.userList.forEach(item => {
            item.userAvatarPath = "/img2d" + item.userAvatarPath
          })
          this.activeItem.updateBaseInfo = false
          this.activeItem.updateSenior = false
        }
      })
      this.checkItems = []
    },
  }
}
</script>

<style scoped>

</style>
