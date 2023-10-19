<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      这里是占利康二创的博客系统
    </div>
    <div>
				欢迎您，尊敬的管理员
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
