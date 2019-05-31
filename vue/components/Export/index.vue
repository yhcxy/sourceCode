<template>
<span class="exportTable">
    <el-row>
            <el-table border :data="innerData" ref="table" height="300">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column :label="$t('components.headers.columnName')">
                    <template slot-scope="scope" v-if="parentPath">
                        {{$t(parentPath+'.' + scope.row.columnName)}}
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.sort')" align="center">
                    <template slot-scope="scope">
                        <i class="el-icon-arrow-up up" @click="swap(scope.$index, -1)"></i>
                        <i class="el-icon-arrow-down down" @click="swap(scope.$index, 1)"></i>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.width')" align="center">
                    <template slot-scope="scope">
                        <el-input-number controls-position="right" style="width:100px" v-model="scope.row.width"/>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('components.headers.hidden')" align="center">
                    <template slot-scope="scope">
                        <input type="checkbox" v-model="scope.row.hidden"/>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
        <br>
        <el-row>
            <el-col :span="3" style="line-height:28px;vertical-align:middle">
                {{$t('common.fileName')}}:
            </el-col>
            <el-col :span="6">
                <el-input v-model="fileName" size="mini"/>
            </el-col>
            <el-col :span="8" style="line-height:28px;vertical-align:middle">
                {{ext}}
            </el-col>
            <el-col :span="7" style="line-height:28px;vertical-align:middle;text-align:right">
                <el-button type="primary" @click="exportFile">{{$t('common.export')}}</el-button>
                <el-button @click="cancel">{{$t('common.cancel')}}</el-button>
            </el-col>
        </el-row>
</span>
</template>
<script>
import {http} from '@/portal/utils/http'
import app from '@/portal/appConfig'
let _this
export default{
    name: "Export",
    props:{
        tableData:{
            default:() => [],
            type:Array
        },
        cancel:{
            default:function(){
                let parent = _this.$parent
                let count = 1

                while(count < 10 && undefined != parent){
                    parent['showExportDialog'] = false

                    parent = parent.$parent
                }
                return null
            },
            type:Function
        },
        parentPath:{
            default:() => null,
            type:String
        },
        name:{
            default:()=>null,
            type:String
        },
        url:{
            default:()=>null,
            type:String
        },
        getParam:{
            default:()=>null,
            type:Function
        }
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
            }],
            innerData:[],
            fileName:this.name,
            ext:'.xls'
        }
    },
    methods:{
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
        setData(data){
           this.innerData = data
        },
        exportFile(){
            let cols = []
            for(let data of this.innerData){
                if(!data.hidden){
                    data['title'] = this.$t(this.parentPath + '.' + data.prop)
                    cols.push(data)
                }
            }

            let params = {}
            if(this.getParam){
                let tmp = this.getParam()
                if(tmp.__pagesize)
                    delete tmp['__pagesize']
                if(tmp.__page)
                    delete tmp['__page']
                params['params'] = tmp
            }
            
            params['columns'] = cols
            params['fileName'] = this.fileName + this.ext

           http({
                url: this.url,
                method: 'post',
                data: params
            }).then(()=>{
                
            })
        }
    },
    beforeCreate(){
    },
    created(){
        this.innerData = Object.assign([], this.tableData)
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
    .exportTable .el-table td > .cell{
        white-space:inherit !important
    }
</style>
