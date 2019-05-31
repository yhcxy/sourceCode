<template>
  <el-row class="filter-header" style="display:flex;align-items:center;" width="100%" v-if="col.filter">
    <el-dropdown
      trigger="click"
      :hide-on-click="true"
      style=""
      @command="handleCommand"
    >
      <span class="el-dropdown-link">{{symbols[queryData[col.prop + "Cond"]]}}</span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          v-for="con in conditions[col.filter.type]"
          :key="con"
          :command="composeValue(con,col)"
        >{{symbols[con] + shownames[con]}}</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-input
      v-if="col.filter.type == 'text'"
      v-model="queryData[col.prop]"
      :maxlength="col.filter.maxlength"
      :clearable="col.filter.clearable"
      :disabled="col.filter.disabled"
      size="mini"
      placeholder="输入关键字搜索"
      style="padding:0px;flex:1;"
      @keyup.enter.native="onQuery"
    />
    <el-input-number
      v-model="queryData[col.prop]"
      v-else-if="col.filter.type == 'number'"
      :min="col.filter.min"
      :max="col.filter.max"
      :controls="col.filter.controls"
      :step="col.filter.step"
      :precision="col.filter.precision"
      size="mini"
      :disabled="col.filter.disabled"
      placeholder="输入关键字搜索"
      controls-position="right"
      style="padding:0px;flex:1;"
      @keyup.enter.native="onQuery"
    />
   <el-select
      v-model="queryData[col.prop]"
      v-else-if="col.filter.type == 'select'"
      :clearable="col.filter.clearable"
      :filterable="col.filter.filterable"
      :disabled="col.filter.disabled"
      :collapse-tags="col.filter.collapsetags"
      size="mini"
      placeholder="请选择"
      style="padding:0px;flex:1;"
      @change="onQuery"
    >
     <template v-if="col.filter.hasOwnProperty('optionKey')">
      <el-option
        v-for="item in col.filter.options[col.filter.optionKey]"
        :key="item.value + '.' + Math.random()"
        :value="item.value"
        :label="col.filter.showValue ? item.value : item.label"
      ></el-option>
      </template>
      <template v-else-if="(col.filter.options instanceof Array)">
         <el-option
        v-for="item in col.filter.options"
        :key="item.value + '.' + Math.random()"
        :value="item.value"
        :label="col.filter.showValue ? item.value : item.label"
      ></el-option>
      </template>
      <template v-else>
         <el-option
        v-for="item in col.filter.options[col.prop]"
        :key="item.value + '.' + Math.random()"
        :value="item.value"
        :label="col.filter.showValue ? item.value : item.label"
      ></el-option>
      </template>
    </el-select>
    <el-select
      v-model="queryData[col.prop]"
      v-else-if="col.filter.type == 'multiselect'"
      multiple 
      :clearable="col.filter.clearable"
      :filterable="col.filter.filterable"
      :disabled="col.filter.disabled"
      :collapse-tags="col.filter.collapsetags"
      :multiple-limit="col.filter.multiplelimit"
      size="mini"
      placeholder="请选择"
      style="padding:0px;flex:1;"
      @change="onQuery"
    >
      <template v-if="col.filter.hasOwnProperty('optionKey')">
      <el-option
        v-for="item in col.filter.options[col.filter.optionKey]"
        :key="item.value + '.' + Math.random()"
        :value="item.value"
        :label="col.filter.showValue ? item.value : item.label"
      ></el-option>
      </template>
      <template v-else-if="(col.filter.options instanceof Array)">
         <el-option
        v-for="item in col.filter.options"
        :key="item.value + '.' + Math.random()"
        :value="item.value"
        :label="col.filter.showValue ? item.value : item.label"
      ></el-option>
      </template>
      <template v-else>
         <el-option
        v-for="item in col.filter.options[col.prop]"
        :key="item.value + '.' + Math.random()"
        :value="item.value"
        :label="col.filter.showValue ? item.value : item.label"
      ></el-option>
      </template>
    </el-select>
    <el-date-picker
      v-else-if="col.filter.type == 'date'"
      v-model="queryData[col.prop]"
      type="daterange"
      range-separator="~"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      size="mini"
      :disabled="col.filter.disabled"
      :editable="col.filter.editable"
      :clearable="col.filter.clearable"
      @change="onQuery"
    ></el-date-picker>
    <el-date-picker
      v-else-if="col.filter.type == 'datetime'"
      v-model="queryData[col.prop]"
      type="datetimerange"
      range-separator="~"
      start-placeholder="开始时间"
      end-placeholder="结束时间"
      size="mini"
      :disabled="col.filter.disabled"
      :editable="col.filter.editable"
      :clearable="col.filter.clearable"
      @change="onQuery"
    ></el-date-picker>
    <el-input v-else :disabled="true" v-model="queryData[col.prop]" size="mini"/>
  </el-row>
</template>
<script>
export default {
  name: 'FilterHeaderColumn',
  data: function () {
    return {
      conditions: {
        "date": ["bt"],
        "datetime": ["bt"],
        "select": ["eq", "ne"],
        "multiselect": ["me"],
        "text": ["cn", "eq", "ne", "bw", "ew"],
        "number": ["eq", "ne", "gt", "ge", "lt", "le"],
      },
      symbols: {
        "bt": "‐‐",
        "cn": "~",
        "eq": "==",
        "ne": "!=",
        "me": "||",
        "bw": "^",
        "ew": "|",
        "gt": ">",
        "ge": ">=",
        "lt": "<",
        "le": "<="
      },
      shownames: {
        "bt": this.$t('components.condition.bt'),
        "cn": this.$t('components.condition.cn'),
        "eq": this.$t('components.condition.eq'),
        "ne": this.$t('components.condition.ne'),
        "me": this.$t('components.condition.me'),
        "bw": this.$t('components.condition.bw'),
        "ew": this.$t('components.condition.ew'),
        "gt": this.$t('components.condition.gt'),
        "ge": this.$t('components.condition.ge'),
        "lt": this.$t('components.condition.lt'),
        "le": this.$t('components.condition.le')
      }
    }
  },
  beforeCreate() {
  },
  props: {
    queryData: { // 查询参数
      type: Object,
      default: function () {
        return {}
      }
    },
    col: {
      type: Object,
      default: function () {
        return {}
      }
    },
    onQuery: { // 触发查询
      type: Function,
      default: () => { }
    },
  },
  methods: {
    composeValue(symbol, col) {
      let o = { "symbol": symbol, "col": col }
      return o;
    }, handleCommand(command) {
      this.queryData[command.col.prop + "Cond"] = command.symbol
      if (this.queryData[command.col.prop] && (command.col.type == 'number' || this.queryData[command.col.prop].length > 0)) {
        this.onQuery()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
	.filter-header /deep/ {
    .el-input--suffix .el-input__inner {
		  padding:0 5px;
		}
  }
</style>