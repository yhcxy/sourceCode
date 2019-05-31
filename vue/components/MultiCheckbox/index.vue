<template>
<span>
  <el-row>
        <el-checkbox @change="all" :indeterminate="indeterminate" v-model="checkAll" :label="$t('common.all')"></el-checkbox>
        <el-checkbox-group v-model="checked" :min="min" :max="max">
            <el-checkbox @change="checkOne" v-for="o in options" :label="o.value" :key="o.value" :value="o.value">{{o.label}}</el-checkbox>
        </el-checkbox-group>
  </el-row>
  <div slot="footer" class="dialog-footer" style="text-align:right">
        <el-button @click="confirm">{{$t('common.confirm')}}</el-button>
        <el-button @click="close">{{$t('common.cancel')}}</el-button>
    </div>
</span>
</template>
<script>
let _this
export default {
  name: 'MultiCheckbox',
  data() {
    return {
        indeterminate:false,
        optionMap:{},
        checkAll:false,
        checked:[]
    }
  },
  props:{
    setValues:{
        type:Function,
        default:()=>null
    },
    options:{
        type:Array,
        default:()=>[]
    },
    values:{
        type:Array,
        default:()=>[]
    },
    min:{
        type:Number,
        default:()=>0
    },
    max:{
        type:Number,
        default:()=>999999999
    },
    close:{
        default:function(){
            let parent = _this.$parent
            let count = 1

            while(count < 10 && undefined != parent){
                parent['showMultiDialog'] = false

                parent = parent.$parent
            }
            return null
        },
        type:Function
    }
  },
  methods:{
    confirm(){
        this.setValues(this.checked)
        this.close()
    },
    setMap(){
        this.optionMap = {}
        for(let o of this.options){
           this.optionMap[o.value] = o.label
        }
        this.checked = this.values

        if(this.checked.length == this.options.length){
            this.checkAll = true
        }
    },
    all(value){
        if(!value && this.min > 0){
            debugger
            this.checkAll = true
            return this.$message.error(this.$t('common.setMin'));
        }

        this.checked = []
        if(value){
            for(let o of this.options){
                this.checked.push(o.value)
            }
        }
        this.indeterminate = false
    },
    checkOne(value){
        if(value && this.checked.length == this.options){
            this.checkAll = true
            this.indeterminate = false
        }else if(this.checked.length > 0){
            this.checkAll = false
            this.indeterminate = true
        }else{
            this.checkAll = false
            this.indeterminate = false
        }
    }
  },
  mounted(){
    this.setMap()
    _this = this
  }
}
</script>
<style>

</style>