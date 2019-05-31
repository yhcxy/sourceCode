<template>
  <el-upload
    drag
    :multiple="multiple"
    :disabled="disabled"
    :name="uploadName"
    :accept="accept"
    ref="upload"
    :limit="limit"
    :action="uploadUrl"
    :data="uploadData"
    :headers="uploadHeader"
    :on-preview="handlePreview"
    :before-remove="beforeRemove"
    :on-remove="handleRemove"
    :file-list="fileList"
    :beforeUpload="beforeUpload"
    :on-exceed="onExceed"
    :onError="uploadError"
    :onSuccess="uploadSuccess"
    :auto-upload="true"
  >
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">
      {{ $t('components.fileupload.dragfile') }}
      <em>{{ $t('components.fileupload.clickupload') }}</em>
    </div>
    <div class="el-upload__tip" slot="tip" v-if="tip.visible">{{tip.msg}}</div>
  </el-upload>
</template>
<script>
import { http } from "@/portal/utils/http"
import appConfig from '@/portal/appConfig'
import { getToken } from '@/portal/utils/auth'
import store from '@/portal/store'
import axios from 'axios'

export default {
  name: 'FileUpload',
  data() {
    let headerData = {}
    if (!process.env.ENABLE_SSO && store.getters.token) {
      headerData["isc-token"] = getToken()
    }
    headerData["isc-app"] = appConfig.APP_NAME
    return {
      uploadUrl: process.env.BASE_API + '/attachment/uploadFile',
      uploadHeader: headerData,
      uploadName: 'file',
      fileList: []
    }
  },
  props: {
    accept: {
      type: String,
      default: function () {
        return ""
      }
    },
    multiple: {
      type: Boolean,
      default: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    tip: {
      type: Object,
      default: function () {
        return {
          visible: false,
          msg: ''
        }
      }
    },
    limit: { // 限制文件数量
      type: Number,
      default: function () {
        return 1
      }
    },
    uploadData: {
      type: Object,
      default: function () {
        return {
          referenceTable: '',
          referenceId: ''
        }
      }
    },
    handlePreview: {
      type: Function,
      default: function (file) {
        if (file.status === 'success') {
          let url = process.env.BASE_API + '/attachment/downloadFile'
          return axios({
            method: 'get',
            url: url,
            params: { attachmentId: file.attachmentId },
            headers: this.$parent.headers, //设置header信息
            responseType: 'blob'
          }).then(res => {
            const blob = res.data
            const reader = new FileReader()
            reader.readAsDataURL(blob)
            reader.onload = (e) => {
              const a = document.createElement('a')
              a.download = file.displayName + '.' + file.fileType
              a.href = e.target.result
              document.body.appendChild(a)
              a.click()
              document.body.removeChild(a)
            }
          });
        }
      }
    },
    beforeUpload: {
      type: Function,
      default: (file) => {
        // 可做类型校验
        // if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
        //   this.$message.error('只支持jpg、png、gif格式的图片！')
        //   return false
        // }
      }
    },
    beforeRemove: {
      type: Function,
      default: function (file, fileList) {
        if (file.status === 'success') {
          return this.$confirm(this.$t('common.delRow'));
        }
      }
    },
    handleRemove: {
      type: Function,
      default: function (file, fileList) {
        let _this = this
        debugger
        http({
          url: '/attachment/deleteFile',
          method: 'post',
          data: { attachmentId: file.attachmentId },
          loading: true
        }).then(data => {
        }).catch(err => {
          _this.$parent.refreshData()
        })
      }
    },
    onExceed: {
      type: Function,
      default: function (file, fileList) {
        this.$mideaMessage({
          type: 'info',
          message: this.$t('components.fileupload.fileslimit') + ":" + this.limit
        })
      }
    },
    uploadSuccess: {
      type: Function,
      default: function (response, file, fileList) {
        switch (response.resultCode) {
          case 'ISC-000':
            // let data = response.data
            // for (var i = 0; i < data.length; i++) {
            //   data[i]['name'] = data[i].displayName + "." + data[i].fileType
            // }
            // this.$parent.fileList = data
            this.$parent.refreshData()
            break;
          case 'ISC-999': // 程序报错，显示more信息       
          default:
            this.$mideaMessage({
              msgCode: response.resultCode,
              message: response.resultMsg
            })
        }
      }
    },
    uploadError: {
      type: Function,
      default: function (response, file, fileList) {
        this.$parent.$mideaMessage({
          type: 'error',
          message: this.$t('components.fileupload.uploadFailed')
        })
      }
    }
  },
  methods: {
    // 当设置了取消自动上传的时候，调用此方法开始上传
    // submitUpload () {
    //   this.$refs.upload.submit()
    // },
    refreshData() {
      debugger
      let _this = this;
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles()      }
      http({
        url: '/attachment/listFile',
        method: 'post',
        data: this.uploadData,
        loading: true
      }).then(data => {
        for (var i = 0; i < data.length; i++) {
          data[i]['name'] = data[i].displayName + "." + data[i].fileType
        }
        _this.fileList = data
      }).catch(err => {
        console.log(err)
      })
    }
  },
  created() {
    this.refreshData()
  }
}
</script>
