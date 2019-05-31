<template>
  <el-container direction="vertical">
    <el-main style="flex-grow: 1;display: flex;flex-direction: column">
    <el-table
      stripe
      border
      :fit="fitTable"
      highlight-current-row
      size="mini"
      @current-change="currentChange"
      @selection-change="checkChange"
      :data="tableData"
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.3)"
      height="100%"
      ref="table"
      style="flex-grow: 1"
    >
      <el-table-column v-if="checkbox" type="selection"/>
      <el-table-column type="index" align="center" fixed></el-table-column>
      <template v-for="(col, key) in innerHeader">
        <el-table-column
          v-if="!col.hidden"
          :key="key"
          :prop="col.prop"
          :label="generateHeader(col.label)"
          :fixed="col.fixed ? col.fixed : false"
          :min-width="col.minWidth"
          :width="col.width"
          :align="col.align?col.align:'center'"
          :show-overflow-tooltip="true"
        >

        <template slot="header" slot-scope="scope">
            <template v-if="showFilterBar">
              <el-row style="display:block;">
                <span style="text-align:center">{{ generateHeader(col.label) }}</span>
                <header-sort
                  v-if="col.sortable"
                  :columnName="col.prop"
                  :sortData="col.sortData"
                  :sortField="sortField"
                  :sortType="sortType"
                  :changeSort="sortChange"
                ></header-sort>
              </el-row>
              <filter-column :col="col" :queryData="queryData" :onQuery="query"></filter-column>
            </template>
            <template v-else>
              <span>
                <span>{{ generateHeader(col.label) }}</span>
                <el-popover placement="bottom" width="200" trigger="click" v-show="col.filter">
                  <filter-column :col="col" :queryData="queryData" :onQuery="query"></filter-column>
                  <i slot="reference" class="el-icon-search"></i>
                </el-popover>
                <header-sort
                  v-if="col.sortable"
                  :columnName="col.prop"
                  :sortData="col.sortData"
                  :sortField="sortField"
                  :sortType="sortType"
                  :changeSort="sortChange"
                ></header-sort>
              </span>
            </template>
          </template>
          <template slot-scope="scope">
            <template v-if="scope.row['___editting'] && (col.editType == 'update' || (col.editType == 'add' && scope.row.___add))">
              <span @click="triggerSelect(scope.row.____index, col.prop)" style="padding-left:7px;z-index:9999;position:relative" v-if="col.type == 'select' && col.formattor" v-html="col.formattor(scope.row[col.prop])"></span>
              <el-select
                  v-model="scope.row[col.prop]"
                  v-if="col.type == 'select'"
                  :style="'left:' + scope.row[col.prop + 'width'] + 'px'"
                  @change="dropChange(scope.$index, scope.row, col)" size="mini"
                  placeholder=""
                  :id="col.prop + scope.row.____index"
                  >
                <template v-if="col.hasOwnProperty('formattor')">
                  <template v-if="col.hasOwnProperty('optionKey')">
                    <el-option v-for="o in col.options[col.optionKey]" :key="o.value" :value="o.value"><span v-html="col.formattor(o.value)"></span></el-option>
                  </template>
                  <template v-else>
                    <el-option v-for="o in col.options[col.prop]" :key="o.value" :value="o.value"><span v-html="col.formattor(o.value)"></span></el-option>
                  </template>
                </template>
                <template v-else>
                  <template v-if="col.hasOwnProperty('optionKey')">
                    <el-option v-for="o in col.options[col.optionKey]" :key="o.value" :value="o.value" :label="o.label">{{o.label}}</el-option>
                  </template>
                  <template v-else>
                     <el-option v-for="o in col.options[col.prop]" :key="o.value" :value="o.value" :label="o.label">{{o.label}}</el-option>
                 </template>
                </template>
              </el-select>
              <el-time-picker @change="scope.row['___editted'] = true;callback(col, scope.row)" v-model="scope.row[col.prop]" placeholder="" v-else-if="col.type == 'time'" :value-format="col.valueFormat" :format="col.format"></el-time-picker>
              <el-date-picker @change="scope.row['___editted'] = true;callback(col, scope.row)" v-model="scope.row[col.prop]" type="date" placeholder="" v-else-if="col.type == 'date'" :value-format="col.valueFormat" :format="col.format"></el-date-picker>
              <el-date-picker @change="scope.row['___editted'] = true;callback(col, scope.row)" v-model="scope.row[col.prop]" type="datetime" placeholder="" v-else-if="col.type == 'datetime'" :value-format="col.valueFormat" :format="col.format"></el-date-picker>
              <el-switch @change="scope.row['___editted'] = true;callback(col, scope.row)" v-model="scope.row[col.prop]" v-else-if="col.type == 'switch'" :inactive-value="col.switchValues.inactive" :active-value="col.switchValues.active"></el-switch>
              <input @change="scope.row['___editted'] = true;callback(col, scope.row)" style="width:100%" v-model="scope.row[col.prop]" :precision="col.precision" :step="col.step" :max="col.max" :min="col.min" v-else-if="col.type == 'number'" size="mini" type="number"/>
              <span v-else-if="col.type == 'dialog'">
                <a v-if="col.showType == 'href'" v-html="formatter(scope,col)" @click="callback(col, scope.row)"></a>
                 <el-button v-if="col.showType == 'button'" type="primary" v-html="formatter(scope,col)" @click="callback(col, scope.row)"></el-button>
                <span v-else v-html="formatter(scope,col)" @click="callback(col, scope.row)"></span>
              </span>
              <el-date-picker @change="scope.row['___editted'] = true;callback(col, scope.row)" v-model="scope.row[col.prop]" type="textarea" placeholder="" v-else-if="col.type == 'textarea'" :autosize="true"></el-date-picker>
              <el-input v-model="scope.row[col.prop]" @change="scope.row['___editted'] = true;callback(col, scope.row)" v-else style="width:98%"/>
            </template>
            <template v-else-if="col.showType == 'href'">
              <a v-html="formatter(scope,col)" @click="callback(col, scope.row)"></a>
            </template>
            <template v-else-if="col.showType == 'button'">
              <el-button type="primary" v-html="formatter(scope,col)" @click="callback(col, scope.row)"></el-button>
            </template>
             <span v-html="formatter(scope,col)" v-else></span>
          </template>
        </el-table-column>
      </template>
    </el-table>
   <!-- <el-dialog :visible.sync="showMultiDialog" style="text-align:left">
      <multi ref="multi" :options="multiOptions" :values="multiValues" :setValues="setValues"/>
    </el-dialog>-->
    <span style="position:absolute;top:10000000000px" ref="privateSpan"></span>
    </el-main>
  </el-container>

</template>
<script>
import { http } from '@/portal/utils/http'
import { formatDate } from '@/portal/utils/index'
import FilterColumn from './components/FilterHeaderColumn'
import { generateHeader } from '@/portal/utils/i18n'
export default {
  name: 'TableEdit',
  components:{multi:()=>import('@/portal/components/MultiCheckbox'),FilterColumn},
  data() {
    return {
      loading: false,
      queryData: {}, // 查询条件存储
      tableData: [],// 表格数据
      innerHeader:{},
      innerData:[],
      findTop:false,
      current:null,
      doNotReresh:false,
      showMultiDialog:false,
      // multiOptions:[],
      multiValues:[],
      currentProp:null,
      privateHtml:'',
      selected:[],
      deletes:[],
      headerMap:{}
    }
  },
  created() {
    if (this.autoQuery) {
      this.query();
    }
  },
  props: {
    autoQuery: {
      type: Boolean,
      default: function () {
        return true
      }
    },
    url: { // 查询数据url
      type: String,
      default: function () {
        return ""
      }
    },
    deleteUrl: {
      type: String,
      default: function () {
        return null
      }
    },
    insertUrl: {
      type: String,
      default: function () {
        return null
      }
    },
    updateUrl: {
      type: String,
      default: function () {
        return null
      }
    },
    saveUrl: {
      type: String,
      default: function () {
        return null
      }
    },
    fitTable: { // 列自适应
      type: Boolean,
      default: function () {
        return true
      }
    },
    preQueryData: { // 前置查询条件
      type: Object,
      default: function () {
        return {}
      }
    },
    postQueryData: { // 后置查询条件
      type: Object,
      default: function () {
        return {}
      }
    },
    rowDblclick: { // 行双击事件
      type: Function,
      default: (row, event, column) => { console.log('default: ' + row + '---' + event + '---' + column) }
    },
    tableHeader: { // 表头数据
      type: Array,
      default: function () {
        return []
      }
    },
    showFilterBar: { // 是否显示过滤行
      type: Boolean,
      default: false
    },
    checkbox: {
      type: Boolean,
      default: false
    },
    options:{
      type: Object,
      default: function () {
        return null
      }
    },
    multiMethod:{
      type: Function,
      default: function () {
        return null
      }
    },
    delKey:{
      type: String,
      default: function () {
        return null
      }
    },
    autoHeight: {
      type: Object,
      default: function () {
        return null
      }
    },
    multiCommit: {
      type: Boolean,
      default: function () {
        return false
      }
    },
    initData: {
      type: Function,
      default: null
    }
  },
  methods: {
    generateHeader,
    callback(col, row){
      if(col.callback){
        col.callback(row)
      }
    },
    triggerSelect(index, prop){
      document.getElementById(prop + index).click()
    },
    setValues(values){
      if(this.current[this.currentProp].sort().toString() === values.sort().toString())
        return

      this.multiValues = values
      this.current[this.currentProp] = values
      this.current.___editted = true
    },
    currentChange(row, old){
      if(null == row){
        this.$refs.table.setCurrentRow(old)
        if(!this.multiCommit)
          old.___editting = false
        this.current = null
        return
      }

      if(null != old){
        if(!this.multiCommit)
          old.___editting = false

        if(old.___editted && !this.multiCommit){
          this.$refs.table.setCurrentRow(old)
          return this.$message.error(this.$t('common.editBeforeSave'))
        }
      }

      row['___editting'] = true
      let innerData = []
      for(let d of this.innerData){
        innerData.push(d)
      }

      for(let h of this.tableHeader){
        if(h.type == 'select'){
          if(h.formattor){
            let a = this.$refs['privateSpan']
            a.innerHTML = h.formattor(h.options[0].value)
            let width = a.offsetWidth + 10
            row[h.prop + 'width'] = -width
          }else{
            row[h.prop + 'Label'] = h.key ? h.optionMap[row[h.key]] : h.optionMap[row[h.prop]]
          }
        }

      }
      innerData[row.____index] = row
      this.innerData = innerData

      for(let prop in row){
        if(row[prop + 'Label'] != undefined){
          row[prop + 'Label'] = this.headerMap[prop].optionMap[row[prop]].label
        }
      }

      this.current = row
    },
    delSelection(){
      //这个是物理删除的函数
      if(null == this.selected || 0 == this.selected.length){
        return this.$message.error(this.$t('common.cannotDelete'))
      }

      this.$confirm(this.$t('common.delRow'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() =>{

        let indexes = []
        let deletes = []

        for(let row of this.selected){
          if(!row.___add){
            deletes.push(row.____index)
          }

          indexes.push(this.tableData.indexOf(row))
        }

        let params = null
        if(null != this.delKey){
          params = {}
          params[this.delKey] = deletes
        }
        if(deletes.length > 0){
          http({
            url: this.deleteUrl,
            method: 'post',
            data: null == params ? deletes : params
          }).then(data => {
            this.find()
          }).catch(err => {
          })

          return
        }

          //先对indexes倒序排列
         indexes.sort(function(a, b){return -(a-b)})
         for(let index of indexes){
            this.tableData.splice(index, 1)
         }

      }).catch(()=>{

      })
   },
   deleteFromView(confirmType = 'NONE'){
      if(null == this.selected || 0 == this.selected.length){
        return this.$message.error(this.$t('common.cannotDelete'))
      }

      if('CONFIRM' == confirmType){
        this.$confirm(this.$t('common.deleteViews'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        }).then(() =>{
          this.func()
        }).catch(()=>{

        })
      }else{
        this.func()
      }
      
   },
  func(){
        let indexes = []//直接删除的数据(新增但未保存的数据可以直接删除)
        for(let row of this.selected){
          indexes.push(this.tableData.indexOf(row))
          if(!row.___add){
            this.deletes.push(row)
          }
        }
          //先对indexes倒序排列再直接删除
         indexes.sort(function(a, b){return -(a-b)})
         for(let index of indexes){
            this.tableData.splice(index, 1)
         }

         let s = this.deletes
      },
    save(willFind, checkFunction){
      if(null == this.current && !this.multiCommit){
        return
      }

      if(!this.multiCommit && (this.current.___editted || this.current.___add)){

        this.$confirm(this.$t('common.editted'), {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }).then(() => {
            this.loading = true
            let isPass = typeof(checkFunction) == 'function' ? checkFunction(this.current) : this.check()
            if(!isPass){
              return this.$message.error({
                  message: this.$t('common.checkEdit')
              })
            }
            let url = this.current.___add ? this.insertUrl : this.updateUrl
            this.doNotReresh = this.current.___add
            http({
                url: url,
                method: 'post',
                data: this.current
              }).then(data => {
                this.loading = false
                this.current.___editted = false
                if(willFind)
                  this.find()
              }).catch(err => {
                this.loading = false
              })
          }).catch(err=>{
          })
      }else if(this.multiCommit){
        let editted = false
        for(let d of this.tableData){
          if(d.___editted || this.deletes.length > 0){
            editted = true
            break
          }
        }

        if(!editted){
          return this.$message.error(this.$t('common.cannotSave'))
        }

         this.$confirm(this.$t('common.editted'), {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }).then(() => {
            this.loading = true
            let isPass = typeof(checkFunction) == 'function' ? checkFunction(this.innerData) : this.check()
            if(!isPass){
              this.loading = false
              return this.$message.error({
                  message: this.$t('common.checkEdit')
              })
            }

            http({
                url: this.saveUrl,
                method: 'post',
                data: this.getEditted()
              }).then(data => {
                this.loading = false
                this.tableData.forEach(o =>{
                  o.___editting = false
                  o.___add = false
                  o.___editted = false
                })
                if(willFind)
                  this.find()
              }).catch(err => {
                this.loading = false
              })
          }).catch(err=>{
          })
      }
    },
    query() {
      if(null != this.current){
          if((this.current.___editted || this.current.___add)){
            return this.$message.error(this.$t('common.editBeforeSave'))
          }

          this.save(true)
      }

      this.find()
    },
    find(data) {
      if(undefined == data){
        data = {}
      }

      for(let i in this.preQueryData){
        data[i] = this.preQueryData[i]
      }

      for(let i in this.queryData){
        data[i] = this.queryData[i]
      }

      this.loading = true

      http({
        url: this.url,
        method: 'post',
        data
      }).then(data => {
        this.loading = false
        if (null != data) {
          let index = 0
          for(let d of data){
            d['____index'] = index++
            d['___editting'] = false

            // for(let prop in d){
            //   if(this.headerMap[prop].options){
            //     d[prop + 'Label'] = ''
            //   }

            // }
          }

          if(this.initData){
            this.tableData = this.initData(data)
            this.innerData = this.initData(data)
          }else{
            this.tableData = data
            this.innerData = data
          }

          this.currentProp = null
          this.current = null
          this.selected = []
          this.deletes = []
        }
      }).catch(err => {
        this.loading = false
      })
    },
    formatter(scope, col) {
      if (col.hasOwnProperty('formattor')) {
        if(col.type=='datetime'){
         return col.formattor(scope.row[col.prop], col.format)
        }
        return col.formattor(scope.row[col.prop])
      } else if(col.type == 'select'){
        return this.label(col, scope.row)
      }else if(col.type == 'dialog'){
        let map = {}
        let key = col.prop
        if(col.hasOwnProperty('optionKey')){
          key = col.optionKey
        }

        for(let o of col.options[key]){
          map[o.value] = o.label
        }

        let ret = []
        if(scope.row[col.prop]){
          for(let data of scope.row[col.prop]){
            ret.push(map[data])
          }
        }
        return ret.join(',')
      }else{
        return scope.row[col.prop];
      }
    },
    dropChange(index, row, col){
      row['___editted'] = true
      if(col.key){
        row[col.key] = row[col.prop]
      }

      if(col.formattor)
        row[col.prop + 'Label'] = ''
      else{
        row[col.prop + 'Label'] = col.optionMap[row[col.prop]]
      }

      if(col.callback){
        col.callback(row)
      }
    },
    label(col, row){
      if(undefined == row || null == row){
        return ''
      }

      if(col.optionMap){
        row[col.prop + 'Label'] = col.optionMap[row[col.prop]]

        return row[col.prop + 'Label']
      }

      return ''
    },
    add(){
      if(null != this.current && !this.multiCommit){
        if((this.current.___editted || this.current.___add)){
          return this.$message.error(this.$t('common.editBeforeSave'))
        }
        this.current.___editting = false
      }
      let row = this.makeNewRow()
      this.tableData.splice(0, 0, row)
      this.$refs.table.setCurrentRow(this.tableData[0])
    },
    makeNewRow(){
      let tmp = {}
      this.tableHeader.forEach(o =>{
        let key = o.prop
        if(o.key){
          key = o.key
        }

        tmp[key] = undefined == o.default ?  '' : o.default
      })

      tmp['___add'] = true
      tmp['___editting'] = true

      return tmp
    },
    findTag(component, tag){
      if(component.$options._componentTag == tag){
        return component
      }

      let ret = null
      if(component.$children.length > 0){
        for(let count = 0; count < component.$children.length; count++){
            ret = this.findTag(component.$children[count], tag)

            if(null != ret){
              return ret
            }
        }
      }

      return null
    },
    multiDialog(row, prop){
      if(this.multiMethod){
        this.multiMethod(row, prop)
      }
    //   this.multiOptions = this.options[prop]
       this.multiValues = row[prop]

     //if(this.$refs.multi)
       // this.$refs.multi.setMap()
     // this.showMultiDialog = true
      this.currentProp = prop
    },
    check(){
      let ret = true
      if(this.multiCommit){
        this.innerData.forEach(o =>{
          if(o.___add || o.___editted){
            this.innerHeader.forEach(h => {
            if(h.notNull){
              if(h.type != 'multi' && '' == o[h.prop])
                ret = false
              else if(h.type == 'multi' && o[h.prop].length == 0){
                ret = false
              }
            }
            })
          }
        })

      }else{
          this.innerHeader.forEach(o => {
          if(o.notNull){
            if(o.type != 'multi' && '' == this.current[o.prop])
              ret = false
            else if(o.type == 'multi' && this.current[o.prop].length == 0){
              ret = false
            }
          }
        })
      }


      return ret
    },
    getLeft(prop){
      return 'left:' + this['left' + prop] + 'px'
    },
    checkChange(selection){
      this.selected = selection
    },
    getEditted(){
      let updates = []
      let adds = []
      let deletes = this.deletes
      this.tableData.forEach(o =>{
        if(o.___add){
          adds.push(o)
        }else if(o.___editted){
          updates.push(o)
        }
      })

      return {updates, adds, deletes}
    }
  },
  watch:{
    tableHeader:{
      immediate:true,
      handler:function(e){
        this.innerHeader = []
        this.$forceUpdate()
      }
    },
    tableData:{
      immediate:true,
      handler:function(e){
        this.innerHeader = []
      }
    },
    innerData:{
      immediate:true,
      handler(e){
        this.$forceUpdate()
      }
    }
  },
  updated(){
    if(this.findTop){
      let component = this.findTag(this, 'table-body')
      component.$el.parentElement.scrollTop = 0

      this.findTop = false
    }
  },
  beforeUpdate(){
    for(let h of this.tableHeader){
      h['editType'] = undefined == h.editType ? 'update' : h.editType
      if(h.type == 'date' || h.type == 'datetime' || h.type == 'time'){
        if(undefined == h.format){
          if(h.type == 'date')
            h['format'] = 'yyyy-MM-dd'
          else{
            h['format'] = 'yyyy-MM-dd HH:mm:ss'
          }

          if(undefined == h.format){
            if(h.type == 'date')
              h['valueFormat'] = 'yyyy-MM-dd'
            else{
              h['valueFormat'] = 'yyyy-MM-dd HH:mm:ss'
            }
          }
          if(undefined == h.format){
            if(h.type == 'time')
              h['valueFormat'] = 'HH:mm'

          }
        }
      }
    }
    this.innerHeader = this.tableHeader

    if(undefined == this.optionMap){
      this['headerMap'] = {}
      this.tableHeader.forEach(col =>{
        let key = col.prop
        if(col.hasOwnProperty('optionKey')){
          key = col.optionKey
        }

        if(col.options && undefined != col.options[key]){

            col['optionMap'] = {}
            col.options[key].forEach(option =>{
                col['optionMap'][option.value] = !col.showLabel && option.label ? option.label : option.value
            })
        }

        this['headerMap'][col.prop] = col
      })
    }
  }
}
</script>
<style scoped>
.el-table th {
  padding: 0px;
  /* background: blue; */
  text-align: center;
}
.el-table th div {
  line-height: 16px;
}
.el-table th > .cell {
  padding: 0px;
}
.el-table  td > .cell{
	white-space: pre;
}
.el-table--mini td{
	padding: 3px 0;
}

.el-input-number .el-input {
  padding: 0px;
}

.el-select > .el-input {
  padding: 0px;
}

.el-table th div {
  display: flex;
}

.___span{
  position: relative;
}
</style>
