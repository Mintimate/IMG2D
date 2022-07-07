<template>
  <div class="column" style="min-height: 60vh">
    <template v-if="tips.length!=0">
      <div class="box" :style="HasRead(item.hasRead)" v-for="item in tips" :key="item.id">
        <div class="content">
          <h3 :style="HasRead(item.hasRead)" v-if="item.type=='UserAccount'">用户提醒
            <span style="position: absolute;right: 10%" class="tag is-success is-small">{{ new Date(item.createTime).toLocaleDateString() }}</span>
          </h3>
          <h3 :style="HasRead(item.hasRead)" v-else-if="item.type=='IMGInFo'">图片提醒
            <span style="position: absolute;right: 10%" class="tag is-success is-small">{{ new Date(item.createTime).toLocaleDateString() }}</span>
          </h3>
          <h3 :style="HasRead(item.hasRead)" v-else>文章提醒
            <span style="position: absolute;right: 10%" class="tag is-success is-small">{{ new Dateitem.createTime().toLocaleDateString() }}</span>
          </h3>
          <p>
            {{ item.message }}
          </p>
        </div>
        <b-collapse
          :open="false"
          position="is-bottom"
          aria-id="contentIdForA11y4">
          <template #trigger="props">
            <a
              aria-controls="contentIdForA11y4"
              :aria-expanded="props.open">
              <b-icon :icon="!props.open ? 'menu-down' : 'menu-up'"></b-icon>
              {{ !props.open ? '展开操作' : '收起操作' }}
            </a>
          </template>
          <b-field class="mb-3">
            <div class="buttons">
              <button
                @click="makeDeleteInfo(item.id)"
                class="button is-rounded is-small is-light is-danger">删除
              </button>
              <button
                :disabled="item.hasRead==1"
                @click="makeReadInfo(item.id)"
                class="button is-rounded is-small is-info is-light">标为已读
              </button>
            </div>
          </b-field>
        </b-collapse>
      </div>
    </template>
    <template v-else>
      <b-field class="mt-3">
        <b-message type="is-success">
          Nice~~没有任何信息，消息箱里空空如也嗷(･_･;
        </b-message>
      </b-field>
    </template>


  </div>
</template>

<script>
import {get, post} from "@/untils/request"
import {sortBy} from "@/untils/jsonUntil";

export default {
  name: "alertInfo",
  data() {
    return {
      tips: []
    }
  },
  mounted() {
    this.getMessage()
  },
  methods: {
    HasRead(Value) {
      if (Value == 1) {
        return {'color': '#b3b3b3',}
      }
    },
    getMessage() {
      get("/img2d/userAlertInfo/getPersonAlertInfo").then(({data}) => {
        this.tips = data.data.sort(sortBy("createTime",true,parseInt))
        console.log(this.tips)
      })
    },
    makeDeleteInfo(id) {
      post("/img2d/userAlertInfo/deleteInfo", id).then(({data}) => {
        if (data.flag) {
          this.getMessage()
        }
      })
    },
    makeReadInfo(id) {
      post("/img2d/userAlertInfo/haveRead", id).then(({data}) => {
        if (data.flag) {
          this.getMessage()
        }
      })
    }
  }
}
</script>

<style scoped>
.HasRead {
  color: #b3b3b3
}
</style>
