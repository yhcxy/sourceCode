<template>
  <div class="the_quick_search" >
    <el-row style="padding: 0;">
      <el-col :span="24" style="position: relative;padding: 0;">
        <el-select class="the_quick_select" :disabled="disabled"
          v-model="inputName" clearable filterable remote reserve-keyword placeholder :remote-method="remoteMethod"
                   @change="getSelectData" @clear="clearOptions"
                   @visible-change="closeSelect">
          <el-option style="border-bottom: 1px solid #ddd;font-weight: bold;" value="121" disabled v-if="selectGridData.length>0">
            <span style="width: 40%;border-right: 1px solid #ddd;display: inline-block;">{{ labelObj[0] }}</span>
            <span style="width: 60%;display: inline-block;" :title="labelObj[1]">{{ labelObj[1] }}</span>
          </el-option>
          <el-option style="border-bottom: 1px solid #ddd;padding: 0 5px;height: 30px; line-height: 30px;"
                                 v-for="item in selectGridData"
                                 :key="item[resetObj[0]]"
                                 :label="item[resetObj[0]]"
                                 :value="item[resetObj[0]]">
          <span style="width: 40%;border-right: 1px solid #ddd;display: inline-block;">{{ item[resetObj[0]] }}</span>
          <span style="width: 60%;display: inline-block;" :title="item[resetObj[1]]">{{ item[resetObj[1]] }}</span>
        </el-option>
        </el-select>
        <el-button :disabled="disabled" icon="el-icon-search" @click="openDialog" style="position: absolute;top: 2px;right: 1px;padding: 5px;"></el-button>
      </el-col>
    </el-row>

    <el-dialog class="the_quick_search_dialog" title="快速查询" :visible.sync="dialogTableVisible" width="800px" :close-on-press-escape=false :close-on-click-modal=false append-to-body>

      <el-row style="margin-bottom: 5px;">
        <el-col :span="20"><span>查询结果</span></el-col>
        <el-col :span="4" >
          <el-button type="primary" size="mini" @click="closeDialog">{{$t('common.confirm')}}</el-button>
        </el-col>
      </el-row>
      <div class="table_wrapper" style="height: 360px;">
      <el-table :data="gridData" border style="width: 100%" max-height="355" highlight-current-row
                @row-dblclick="getLineData"
                @current-change="handleCurrentChange"
                @selection-change="checkChange" >

        <el-table-column v-if="multiSelect" width="30" type="selection" />
        <el-table-column type="index" width="40" />
        <template v-for="(col, key) in tableHeader">
          <el-table-column
            v-if="!col.hidden"
            :key="key"
            :prop="col.name"
            :label="col.label"
            :width="col.width"
            align="center"
            :show-overflow-tooltip="true">
            <template slot="header" slot-scope="scope">

              <div style="display: block">{{col.label}}</div>
              <div style="padding-bottom: 5px;">
                <el-input v-model="form[col.name]" v-if="col.name.toLowerCase() in trnasformSqlObj"
                          placeholder="输入关键字搜索"
                          @keyup.native.enter="query('enter')"></el-input>
              </div>
            </template>
          </el-table-column>
        </template>
      </el-table>
      </div>
      <el-footer class="page-bar">
        <pager-bar
          :dataCount="dataCount"
          :queryTotal="queryTotal"
          :pageIndex="viewIndex"
          :pageSize="viewSize"
          :pageCount="pageCount"
          :pageQuery="pageQuery"
          ref="pager"
        ></pager-bar>
      </el-footer>
    </el-dialog>

  </div>
</template>

<script>
  import { http } from '@/portal/utils/http'
  import PagerBar from '@/portal/components/Pager';
  import { getProfile } from "@/portal/utils/auth";
  import { isNull, trim } from "@/portal/utils";

    export default {
      name: "QuickSearch",
      components:{ PagerBar },
      inject: {
        elForm: {
          default: ''
        },

        elFormItem: {
          default: ''
        }
      },
      data(){
          return{
            tableHeader: [],     // 表头数据
            filterQueryArr:[],    //查询条件过滤
            isSizeChanged: false,
            dataCount: 0,
            queryTotal: -1,   // 共几条 -1就是问号
            viewIndex: 1,
            viewSize: 10,
            queryParam: {
              __page: 1,
              __pagesize: 10,
            },
            labelObj: [],
            resetObj: [],
            currentRow:'',
            form:{},
            queryForm:{},
            formLabelWidth: '120px',
            dialogTableVisible:false,
            gridData: [],
            //select下拉data区域
            inputName:'',
            selectVisible: false,
            showSelect:false,
            selectGridData:[],
            multipleSelection:[],    //多选表格的行数据
            paramStr:'',
            trnasformSqlObj: {},     //用来转换前端查询字段转换----
            initState:false,        // 组件初始化未打开的状态

          }
      },

      methods:{
        checkChange(val) {
          this.multipleSelection = val;
          this.dispatchChange();
        },
        dispatchChange(){
          if (!!this.elFormItem) {
            this.elFormItem.$emit('el.form.change');
          }
        },
        remoteMethod(queryStr) {
          if (queryStr) {
            this.showSelect = true;
            this.inputName=queryStr;
            this.paramStr=queryStr;
            //延时0.5秒查询
            setTimeout(() => {
              this.getConfigration();
            }, 500);
          } else {
            this.selectGridData = [];
          }
        },
        getSelectData(val){   //获取选中的值并且赋值
          let resObj = this.selectGridData.find(item=>item[this.resetObj[0]]===val);
          this.inputName = (this.showKey && resObj) ? resObj[this.showKey] : this.inputName;
          this.setValue(resObj);
          this.$forceUpdate();
          this.$emit("close-quicksearch", resObj, this.scopeData);
          this.dispatchChange();
        },
        getLineData(row){     //双击选中行数据
          this.inputName = (this.showKey && row) ? row[this.showKey] : this.inputName;
          this.setValue(row);
          this.$forceUpdate();
          this.$emit("close-quicksearch", row, this.scopeData);
          this.dispatchChange();
          this.dialogTableVisible = false;
          this.selectVisible=false;
        },
        clearOptions(){   //清空下拉选的值--------
          this.selectGridData =[];
          this.dispatchChange();
        },
        closeSelect(isClose){   //清空下拉选的值--------
          if(!isClose){
            if(this.inputName == this.paramStr){
              this.inputName='';      //没有选择下拉则清空input框的值
              this.setValue();
              this.selectGridData =[];
            }
          }
        },
        openDialog(){
          //添加前置事件---
          this.$emit("before-open", true);
          //第一次打开弹框页面flag----->清除过滤查询条件
          this.form={};
          this.showSelect=false;
          this.selectVisible=false;
          this.getConfigration();
          this.dialogTableVisible = true;
        },
        closeDialog(){
          if(this.multiSelect){
            this.$emit("close-quicksearch",this.multipleSelection);
          }else {
            if(!this.currentRow){
              this.$mideaMessage({
                type:'info',
                message: `请点击选中行`
              });
              return;
            }
            this.inputName = (this.showKey && this.currentRow) ? this.currentRow[this.showKey] : this.inputName;
            this.setValue(this.currentRow);
            this.$forceUpdate();
            this.$emit("close-quicksearch", this.currentRow);
          }
          this.dialogTableVisible = false;
          this.dispatchChange();
        },
        handleCurrentChange(val) {
          this.currentRow = val;
        },
        pageQuery(opr, size) {    // 查询数据的方法
          let allCount = Math.ceil(this.dataCount/this.viewSize);   //总页数---作为判断依据
          if(opr=="prev" && this.queryParam.__page == 1) return;
          if(opr=="next" && this.queryParam.__page == allCount) return;
          switch (opr){
            case "":
              this.viewSize = size;
              this.viewIndex = 1;
              this.queryParam.__pagesize = size;
              this.queryParam.__page = 1;
              break;
            case "prev":
              this.queryParam.__page -= 1;
              break;
            case "next":
              this.queryParam.__page += 1;
              break;
          }
          this.isSizeChanged =true;

          this.query(opr);
        },
        pageCount(){    // 查询共几条的方法

        },
        setCache(key,data){
          sessionStorage.setItem(getProfile().__language +"_QS_"+key, JSON.stringify(data));
        },
        getCache(key){
          var data= sessionStorage.getItem(getProfile().__language +"_QS_" +key);
          return data ? JSON.parse(data) :null;
        },
        getConfigration(){
          if( this.getCache(this.name) ) {
            let data = this.getCache(this.name);
            this.tableHeader= data.colModel.slice(0);
            this.filterQueryArr = Object.values(data.queryData.defCols);
            this.trnasformSqlObj ={};
            for(let i in data.queryData.defCols){
              if(i!='_id_column') {
                let lowerCase = i.replace(/t./g,'').replace(/_/g,'').toLowerCase();
                this.trnasformSqlObj[lowerCase] = i;
              }
            }
            this.resetObj = data.colModel.map(v=>v.name);
            this.labelObj = data.colModel.map(v=>v.label);
            this.query();
            return;
          }
          http({      //获取配置项---展示在页面上的字段
            url: "/isales-main/mstQuicksearchConfig/getConfig",
            method: 'post',
            loading: true,
            data: {name: this.name}
          }).then(data => {
            this.tableHeader= data.colModel.slice(0);
            this.filterQueryArr = Object.values(data.queryData.defCols);
            this.trnasformSqlObj ={};
            for(let i in data.queryData.defCols){
              if(i!='_id_column') {
                let lowerCase = i.replace(/t./g,'').replace(/_/g,'').toLowerCase();
                this.trnasformSqlObj[lowerCase] = i;
              }
            }
            this.resetObj = data.colModel.map(v=>v.name);
            this.labelObj = data.colModel.map(v=>v.label);
            //设置缓存数据----去除缓存数据
            this.setCache(this.name,{
              colModel: data.colModel,
              queryData: data.queryData,
            });
            this.query();
          }).catch(err => {
            console.log(err);
          });
        },
        query(opr) {
          this.initState = true;
          this.queryForm = {};
          if (opr == 'enter') {   //来自于enter事件，先清空form绑定的值，再重新赋值
            for (let i in this.form) {
              if (!(i.includes('_') || i.includes('.')) && this.form[i]) {
                let upperCase = this.trnasformSqlObj[i.toLowerCase()];
                if(upperCase) this.queryForm[upperCase] = this.form[i];
              }
            }
          }else {
            //下拉查询条件过滤条件----//展开下拉选择
            if (this.showSelect && this.filterQueryArr && this.filterQueryArr[0]) {
              this.queryForm[this.filterQueryArr[0].name] = this.inputName;
            }
          }
          if(JSON.stringify(this.preQueryData) !=="{}"){    //添加前置查询条件
            this.queryForm = Object.assign({}, this.preQueryData, this.queryForm);
          }
          let formData = JSON.stringify({query: JSON.stringify(this.queryForm), extendQuery:JSON.stringify({_quickKey:this.name,createdBy:getProfile().__userName}) });
          //
          let selectURL = "/isales-main/mstQuicksearchConfig/queryByFormCondition";
          let paramData = {};

          //是否是下拉查询
          if(this.showSelect){
            selectURL = "/isales-main/mstQuicksearchConfig/quickSearchQuery";
            formData = JSON.stringify({key:this.name,value:this.inputName,isSetValue:JSON.stringify(this.isSetValue), extendQuery:JSON.stringify({_quickKey:this.name,createdBy:getProfile().__userName}) });
          }
          paramData = {params: formData};
          Object.keys(this.queryParam).forEach(v=>{
            paramData[v] = this.queryParam[v];
          });

          http({      //获取数据---展示在table页面上的数据
            url: selectURL,
            method: 'post',
            loading: true,
            data: paramData
          }).then(data => {
            if(this.showSelect){
              this.selectGridData = data.slice(0);
              if(data.length===0){
                this.inputName='';
                this.setValue();
              }
              return;
            }else{
              this.gridData = data.data.slice(0);
            }
            //设置显示总条数
            this.queryTotal=data.pagecount;
            this.dataCount = data.pagecount;
            if(opr){     //改变相应的页码展示数
              switch (opr){
                case "":
                  this.viewIndex = 1;
                  break;
                case "prev":
                  this.viewIndex -= 1;
                  break;
                case "next":
                  this.viewIndex += 1;
                  break;
              }
            }
          }).catch(err => {
            console.log(err);
          });
        },
        setNullValue(value, defaultValue = '') {
          return isNull(value) ? defaultValue:value;
        },
        // 设置属性值
        setValue(row = {}) {
          if (this.scopeData) {
            [this.displayKey, this.valueKey].forEach((item, index) => {
              if ( Object.prototype.toString.call(item) == '[object Array]' ) {
                let [key, mapKey] = item;
                this.scopeData[mapKey] = this.setNullValue(row[key]);
              } else if(item) {
                this.scopeData[item] = this.setNullValue(row[item]);
              }
            });

            // map 值
            if ( Object.prototype.toString.call(this.mapValue) == '[object Array]' ) {
              for(let item of this.mapValue) {
                let [key, mapKey] = trim(item).split(',');
                if (key && mapKey) {
                  this.scopeData[mapKey] = this.setNullValue(row[key]);
                } else if (key) {
                  this.scopeData[key] = this.setNullValue(row[key]);
                }
              }
            }
          }
        }
      },

      props:{
        name: { // 父页面传值--->配置项
          type: String,
          default: function () {
            return ""
          }
        },
        showInput: { // 父页面传值input
          type: String,
          default: function () {
            return ""
          }
        },
        showKey: { //想要展示的字段属性key, 如选择code展示name 属性
          type: String,
          default: function () {
            return ""
          }
        },
        disabled: {
          type: Boolean,
          default: function () {
            return false
          }
        },
        isSetValue: {     //下拉查询是否全匹配-------默认false
          type: Boolean,
          default: function () {
            return false
          }
        },
        //
        // 展示的字段属性key, 如 {name, code} 中的 name 属性
        displayKey: {
          type: [String, Array],
          default: function () {
            return null
          }
        },
        // 存储值的字段属性key, 如 {name, code} 中的 code 属性
        valueKey: {
          type: [String, Array],
          default: function () {
            return null
          }
        },
        // 取值映射
        mapValue: {
          type: Array,
          default: function () {
            return null
          }
        },
        // 当前行上绑定的 data 对象
        scopeData: {
          type: Object,
          default: function () {
            return null
          }
        },
        //是否支持多选
        multiSelect:{
          type: Boolean,
          default: function () {
            return false
          }
        },
        preQueryData: { // 前置查询条件
          type: Object,
          default: function () {
            return {}
          }
        },
      },
      watch:{
        showInput:{
          immediate:true,
          handler:function(e){
            //区分未打开quicksearch页面---打开quicksearch页面input框的展示
            if(!this.initState){        //未打开quicksearch页面
              this.inputName= e;
            }else{
              this.inputName=(this.showKey) ? this.inputName : e;
            }
          }
        }
      },

    }
</script>

<style scoped lang="scss">
.the_quick_search /deep/ {
  position: relative;
  .el-input-group__append {
    padding-right: 8px;
  }
  .the_quick_select{
    display: block;padding: 0;
  }
  .the_quick_select .el-input__suffix{
    right: 25px;
  }
  .the_quick_select .el-input__suffix .el-input__icon{
    width: 16px;
  }
  .el-button{
    border: none;
  }
  .el-form-item{
    margin-bottom: 0;
  }
  .el-dialog__body{
    padding-top: 10px;
    height: 460px;
  }

}
.the_quick_search_dialog .el-table th div{
  line-height: 16px;
  padding-bottom: 0 !important;
}

</style>
