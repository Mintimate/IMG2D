<template>
  <div>
    <div class="columns">
      <div class="column">
        <indexHeader/>
      </div>
      </div>
    <div class="card mt-3">
      <imageWaterFallDisplay/>
    </div>

  </div>
</template>


<script>
import imageWaterFallDisplay from "../../components/displayIMG/imageWaterFallDisplay"
import indexHeader from "../../components/displayIMG/indexHeader";

export default {
  name: "myIndex",
  components: {indexHeader, imageWaterFallDisplay},
  data() {
    return {}
  },
  methods: {}
}
</script>

<style scoped>
</style>
