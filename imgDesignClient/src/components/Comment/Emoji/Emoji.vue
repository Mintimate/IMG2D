<template>
  <div v-show="chooseEmoji" class="emoji-wrapper">
    <span
      v-for="(value, key, index) of emojiList"
      :key="index"
      class="emoji-item"
      @click="addEmoji(key)"
    >
      <img
        :src="value"
        :title="key"
        class="emoji"
        height="24"
        width="24"
      />
    </span>
  </div>
</template>

<script>
import EmojiList from "@/assets/emoji/js/emoji";

export default {
  props: {
    chooseEmoji: {
      type: Boolean
    }
  },
  data: function () {
    return {
      emojiList: EmojiList
    };
  },
  methods: {
    addEmoji(key) {
      this.$emit("addEmoji", key);
    }
  },
};
</script>

<style scoped>
.emoji {
  user-select: none;
  margin: 0.25rem;
  display: inline-block;
  vertical-align: middle;
}

.emoji-item {
  cursor: pointer;
  display: inline-block;
}

.emoji-item:hover {
  transition: all 0.2s;
  border-radius: 0.25rem;
  background: #dddddd;
}

.emoji-wrapper {
  max-height: 150px;
  overflow-y: auto;
}
</style>
