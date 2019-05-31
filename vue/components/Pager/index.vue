<template>
  <el-row style="padding-top:5px;color:#909399;font-size:12px;">
    <el-col style="width:33.33%">{{$t('components.pager.showing')}}
      <el-select v-model="ps" size="mini" style="width:70px" @change="change">
        <el-option v-for="size in options" :key="size" :label="size" :value="size"></el-option>
      </el-select>{{$t('components.pager.entries')}}
    </el-col>
    <el-col class="application-col clear" style="width:33.33%;text-align:center;line-height:28px">
      {{$t('components.pager.showing')}}&nbsp;{{begin}}&nbsp;{{$t('components.pager.to')}}&nbsp;{{end}}&nbsp;
      {{$t('components.pager.of')}}&nbsp;<a class="clear" href="javascript:void(0)" @click="pageCount">{{queryTotal | getAll}}</a>&nbsp;{{$t('components.pager.entries')}}
    </el-col>
    <el-col class="right application-col clear" style="width:33.33%;line-height:28px">
      <i class="el-icon-caret-left" :class="[pageIndex == 1 ? 'forbid' : 'can']" @click="page('prev')"></i>
      <i class="el-icon-caret-right" :class="[dataCount < pageSize ? 'forbid' : 'can']" @click="page('next')"></i>
    </el-col>
  </el-row>
</template>
<script>
export default {
  props: {
    dataCount: { // 当前页查询出来几条数据
      type: Number,
      default: 0
    },
    queryTotal: { // 共几条 -1就是问号
      type: Number,
      default: -1
    },
    pageIndex: { // 当前第几页
      type: Number,
      default: 0
    },
    pageSize: { // 分页大小
      type: Number
    },
    pageCount: { // 查询共几条的方法
      type: Function,
      default: undefined
    },
    pageQuery: { // 查询数据的方法
      type: Function
    }
  },
  name: 'isc-pager',
  componentName: 'isc-pager',
  data(){
    return {
      options:[
        5, 10, 15, 20, 30, 50
      ],
      ps: this.pageSize,
      ps2: this.pageSize
    }
  },
  computed:{
    begin:{
      get(){
        return 0 >= this.pageIndex ? 0 : ((this.pageIndex - 1) * this.pageSize + 1)
      }
    },
    end:{
      get(){
        return 0 >= this.pageIndex ? 0 : (this.pageIndex* this.pageSize)
      }
    }
  },
  filters:{
    getAll(all){
      return all < 0 ? '?' : all;
    }
  },
  methods:{
    page(opr){
      if('prev' == opr && this.pageIndex <= 1){
          return
      }

      if('next' == opr && this.dataCount < this.pageSize){
          return
      }
      this.pageQuery(opr)
    },
    change(val){
     if(this.ps2 != val){
        this.pageQuery('', val)
        this.ps2 = val
        this.ps = val
      }
    },
    changeLang(val){
      
    }
  },
  beforeCreate(){    
  }
} 
</script>
<style>
.page{
  width: 120px;
}

.right{
  text-align: right;
}

.forbid{
  color: #909399
}

.can{
  color: #409EFF
}

.can:hover{
  cursor: pointer
}

.clear{
  margin-right: 0 !important
}

a.clear{
  color: #409EFF
}

a.clear:hover{
  margin-right: 0 !important
}
</style>

