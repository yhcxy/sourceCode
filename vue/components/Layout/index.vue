<template>
<span class="layout">
    <el-row>
            <el-table border :data="innerData" ref="table" height="300">
                <el-table-column :label="$t('components.headers.columnName')" width="130">
                    <template slot-scope="scope">
                        <span v-if="columnLabelName">{{$t(scope.row[columnLabelName])}}</span>
                        <span v-else-if="parentPath">{{$t(parentPath+'.' + scope.row.columnName)}}</span>
                         <span v-else>{{$t(parentPath+'.' + scope.row.columnName)}}</span>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.sort')" width="70" align="center">
                    <template slot-scope="scope">
                        <i class="el-icon-arrow-up up" @click="swap(scope.$index, -1)"></i>
                        <i class="el-icon-arrow-down down" @click="swap(scope.$index, 1)"></i>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.hidden')" width="70" align="center">
                    <template slot-scope="scope">
                        <input type="checkbox" v-model="scope.row.hidden"/>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.width')" width="150">
                    <template slot-scope="scope">
                        <el-input-number size="mini" v-model="scope.row.width"></el-input-number>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.freeze')" width="150">
                    <template slot-scope="scope">
                        <el-select v-model="scope.row.fixed" size="mini">
                            <el-option :label="$t('components.none')" value="">{{$t('components.none')}}</el-option>
                            <el-option :label="$t('components.left')" value="left">{{$t('components.left')}}</el-option>
                            <el-option :label="$t('components.right')" value="right">{{$t('components.right')}}</el-option>
                        </el-select>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
        <br>
        <el-row>
            <el-col :span="24">
                {{$t('components.pager.showing')}}
                <el-select v-model="innerPageSize" size="mini" style="width:70px">
                    <el-option v-for="size in options" :key="size" :label="size" :value="size"></el-option>
                 </el-select>{{$t('components.pager.entries')}}&nbsp;&nbsp;&nbsp;&nbsp;
                 {{$t('components.showFilter')}}
                <el-switch ref="dd" v-model="innerFilter" active-value="1" inactive-value="0" size="mini"></el-switch>
            </el-col>
        </el-row><br>
        <div slot="footer" class="dialog-footer" style="text-align:right">
            <el-button @click="save()">{{$t('common.save')}}</el-button>
            <el-button @click="saveApply()">{{$t('common.save_apply')}}</el-button>
            <el-button @click="apply()">{{$t('common.apply')}}</el-button>
            <el-button @click="cancel">{{$t('common.cancel')}}</el-button>
        </div>
</span>
</template>
<script>
import {http} from '@/portal/utils/http'
import app from '@/portal/appConfig'
import {setLayout} from '@/portal/utils/layout'
let _this
export default{
    name: "Layout",
    props:{
        tableData:{
            default:() => null,
            type:String
        },
        pageSize:{
            default:() => null,
            type:String
        },
        showFilter:{
            default:() => null,
            type:String
        },
        component:{
            default:() => null,
            type:Object
        },
        cancel:{
            default:function(){
                let parent = _this.$parent
                let count = 1

                while(count < 10 && undefined != parent){
                    parent['showLayoutDialog'] = false

                    parent = parent.$parent
                }
                return null
            },
            type:Function
        },
        gridId:{
            default:() => null,
            type:String
        },
        pageName:{
            default:() => null,
            type:String
        },
        parentPath:{
            default:() => null,
            type:String
        },
        // 列名显示取值字段属性
        columnLabelName: {
            default:() => null,
            type:String
        },
    },
    data(){
        return{
            showDialog:false,
            tableHeader:[{
                prop: "columnName",
                label: this.$t('components.headers.columnName')
            },{
                prop: "seq",
                label: this.$t('components.headers.sort')
            },{
                prop: "hidden",
                label: this.$t('components.headers.hidden')
            },{
                prop: "width",
                label: this.$t('components.headers.width')
            }],
            innerData:[],
            options:[
                5, 10, 15, 20, 30, 50
            ],
            innerPageSize:this.component[this.pageSize],
            innerFilter:this.filter+''//避免switch失效
        }
    },
    methods:{
        setLayout,
        swap(index, val){
            let innerData = []
            if((index == 0 && val == -1) || (this.innerData.length == index + 1 && val == 1)){
                return
            }

            for(let i in this.innerData){
                if(i == index + val){
                    innerData.push(this.innerData[index])
                    continue
                }

                if(i == index){
                    innerData.push(this.innerData[index + val])
                    continue
                }

                innerData.push(this.innerData[i])
            }
            this.innerData = innerData
        },
        save(){
            let data = {}
            let details = []
            let seq = 1

            this.innerData.forEach(o => {
                let tmp = {}

                Object.assign(tmp, o);
                tmp['seq'] = seq++

                if('number' != typeof(tmp.hidden)){
                    tmp.hidden = tmp.hidden ? 1 : 0
                }

                if(!tmp.hasOwnProperty('fixed')){
                    tmp['fixed'] = ''
                }

                tmp['columnWidth'] = tmp.width
                
                details.push(tmp)
            })

            data['details'] = details
            data.pageSize = this.innerPageSize
            data.showFilter = this.innerFilter
            
            Object.assign(data, {application:app.APP_NAME, pageName:this.pageName, gridId:this.gridId});

            http({
                url: '/admin/layout/save',
                method: 'post',
                data
            }).then(resp =>{
                this.$message.success({
                    message: this.$t('common.success')
                })
            })
            this.cancel()
        },
        apply(){
            this.setLayout(this.innerData, this.innerPageSize, this.innerFilter * 1, this.gridId, this.component)
            this.cancel()
        },
        saveApply(){
            this.save()
            this.apply()
        },
        setData(data){
           this.innerData = data
        }
    },
    beforeCreate(){
    },
    created(){
        for(let data of this.component[this.tableData]){
            let d = {}
            for(let k in data){
                d[k] = data[k]
            }
            this.innerData.push(d)
        }
        this.innerPageSize = this.component[this.pageSize] * 1
        this.innerFilter = this.component[this.showFilter] + ''
        this.innerData.forEach(o =>{
            o['columnName'] = o.prop
        })
        _this = this
    }
}
</script>
<style>
    .up:hover{
        cursor: pointer;
    }

    .down:hover{
        cursor: pointer;
    }
    .layout .el-table td > .cell{
        white-space:inherit !important
    }
</style>
