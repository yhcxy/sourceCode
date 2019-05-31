<!--
 * @Author: tim
 * @LastEditors: tim
 * @Description: main header
 * @Date: 2019-03-19 18:01:32
 * @LastEditTime: 2019-04-29 16:52:55
 -->

<template>
  <div class="main-header">
    <el-row type="flex">
      <el-col :span="lSpan" class="header-left"><slot name="left"></slot></el-col>
      <el-col :span="rSpan" class="header-right button-group">
        <slot name="right"></slot>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'MainHeader',
  props: {
    lSpan: {
      type: Number,
      default: 12
    },
    rSpan: {
      type: Number,
      default: 12
    }
  },
  data() {    
    return {
      
    }
  }
};
</script>

<style lang="scss" scoped>
.main-header /deep/ {
  height: 36px;
  padding: 5px;
  background: #f8f8f8;
  border: 1px solid #efefef;
  // overflow: hidden;
  > .el-row {
    height: 100%;
    align-items: center;
  }
  .header-left {
    font-size: 14px;
  }
  .header-right {
    text-align: right;    
  }
}
</style>
