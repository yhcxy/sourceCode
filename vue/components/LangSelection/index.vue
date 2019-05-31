<template>
<el-dropdown @command="change" size="mini" trigger="click" v-if="langs.length>0">
  <span class="el-dropdown-link">
    {{language}}<i class="el-icon-arrow-down el-icon--right"></i>
  </span>
  <el-dropdown-menu slot="dropdown">
    <el-dropdown-item v-for="lang in langs" v-bind:key="lang.isoCode" :command="lang">{{lang.localName}}</el-dropdown-item>
  </el-dropdown-menu>
</el-dropdown>
</template>
<script>
import http from '@/portal/utils/http'
import {getProfile, setProfile} from '@/portal/utils/auth'
import storage from '@/portal/utils/storage'
import app from '@/portal/appConfig'
export default {
    name:'langSelection',
    data(){
        return {
            langs:[],
            language:''
        }
    },
    beforeCreate(){
        http({
            url: '/isc-admin/language/list?application=' + app.APP_NAME,
            method: 'get',
            data:{}
          }).then(data => {
            this.langs = data
            // 数据存储到 store
            this.$store.commit('SET_LOCALE_LANGUAGE', data)

            let profile = getProfile()
            data.forEach(o => {
                if(profile.__language == o.isoCode){
                    this.language = o.localName
                }
            })
        })
    },
    methods:{
        change(command){
             http({
                url: '/auth/user/getSetProfile',
                method: 'post',
                data: {__language:command.isoCode}
            }).then(data => {
                setProfile(data)
                storage.setLanguage(command.isoCode)
                this.$router.push({path:'/home'})
                history.go(0)
            })
        }
    }
}
</script>
<style>
.el-dropdown-link{
    cursor: pointer;
  }
</style>

