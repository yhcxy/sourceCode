<template>
  <span class="caret-wrapper" @click="changeSort(columnName)">
    <i :class="{'sort-caret ascending':true,'activeascending':activeType == 'asc'}"></i>
    <i :class="{'sort-caret descending':true,'activedescending':activeType == 'desc'}"></i>
  </span>
</template>
<script>
export default {
  name: 'HeaderSort',
  props: {
    columnName: {
      type: String,
      default: function () {
        return ''
      }
    },
    sortField: {
      type: String,
      default: function () {
        return ''
      }
    },
    sortType: {
      type: String,
      default: function () {
        return ''
      }
    },
    changeSort: {
      type: Function,
      default: () => {
      }
    }
  },
  computed: {
    activeType: function () {
      if (this.sortField == this.columnName) {
        return this.sortType
      } else {
        return ''
      }
    }
  },
  methods: {
    // 父组件改变props，子组件如果直接使用props，会触发子组件更新
    // 父组件改变props，子组件如果将props放进data中再使用，不会触发子组件更新
    // 父组件改变props，子组件如果将props放进computed中再使用，会触发子组件更新
    // 子组件改变props，父组件会更新，但其他子组件不会更新，故共享的属性需要放到父组件去改变
  }
}
</script>
<style>
.el-table .caret-wrapper {
  height: 18px;
}
.el-table .sort-caret.ascending {
  top: 0px;
}
.el-table .sort-caret.descending {
  bottom: 0px;
}
.el-table .sort-caret {
  border: 4px solid transparent;
}
.activeascending {
  border-bottom-color: #409eff !important;
}
.activedescending {
  border-top-color: #409eff !important;
}
</style>
