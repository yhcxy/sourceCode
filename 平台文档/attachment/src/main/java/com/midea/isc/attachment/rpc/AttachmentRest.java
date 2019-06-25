package com.midea.isc.attachment.rpc;

import com.midea.isc.attachment.biz.AttachmentListBiz;
import com.midea.isc.attachment.model.AttachmentList;
import com.midea.isc.attachment.param.AttachmentListParam;
import com.midea.isc.auth.client.annotation.IgnoreClientToken;
import com.midea.isc.common.model.FileInfo;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.CommUtil;
import com.midea.isc.common.web.rpc.BasicRest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AttachmentRest extends BasicRest {

    @Autowired
    private AttachmentListBiz attachmentListBiz;

    /**
     * 返回attchmentId
     * @param application
     * @return
     * @throws Exception
     */
    @IgnoreClientToken
    @RequestMapping(value = "rpc/uploadFile", method = RequestMethod.POST)
    public int uploadFile(@RequestParam("file") MultipartFile attachment,
                                           @RequestParam("referenceTable") String referenceTable,
                                           @RequestParam("referenceId") Integer referenceId,
                                           @RequestParam("application") String application)
            throws Exception {
        if (attachment == null) {
            throw new IscException("ISC-925", "attachment null");
        }
        if (CommUtil.isEmpty(referenceTable)) {
            throw new IscException("ISC-924", "ReferenceTable null");
        }
        if (null == referenceId) {
            throw new IscException("ISC-924", "ReferenceId null");
        }

        if (CommUtil.isEmpty(application)) {
            throw new IscException("ISC-924", "application null");
        }
        AttachmentListParam param = new AttachmentListParam();
        Profile profile = new Profile();
        profile.set__app(application);
        param.setProfile(null);
        param.setApplication(application);
        param.setReferenceTable(referenceTable);

        return attachmentListBiz.uploadFile(param, attachment, referenceId);
    }

    /**
     * 获取附件列表
     *
     * @param param
     *            必须指定referenceId 必须指定referenceTable
     * @return 附件清单
     */
    @RequestMapping(value = "rpc/listFile", method = RequestMethod.POST)
    public List<AttachmentList> listFile(@RequestBody AttachmentListParam param) {
        if (StringUtils.isEmpty(param.getReferenceTable())) {
            throw new IscException("ISC-924", "ReferenceTable null");
        }
        if (StringUtils.isEmpty(param.getReferenceId())) {
            throw new IscException("ISC-924", "ReferenceId null");
        }
        return attachmentListBiz.selectList(param);
    }

    /**
     * 附件下载
     *
     * @param attachmentId
     *            附件ID
     */
    @RequestMapping(value = "rpc/downloadFile")
    public FileInfo downloadFile(@RequestParam("attachmentId") int attachmentId) throws Exception {
        return attachmentListBiz.rpcDownloadAttachment(attachmentId);
    }

    /**
     * 多附件下载
     *
     * @param attachmentIds
     *            附件ID
     */
    @RequestMapping(value = "rpc/batchDownloadFile")
    public List<FileInfo> batchDownloadFile(@RequestParam("attachmentIds") int[] attachmentIds) throws Exception {
        return attachmentListBiz.rpcBatchDownloadAttachment(attachmentIds);
    }
}
