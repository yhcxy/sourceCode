package com.midea.isc.attachment.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.midea.isc.attachment.biz.AttachmentListBiz;
import com.midea.isc.attachment.model.AttachmentList;
import com.midea.isc.attachment.param.AttachmentListParam;
import com.midea.isc.auth.client.annotation.IgnoreUserToken;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.BaseContextHandler;
import com.midea.isc.common.util.CommUtil;
import com.midea.isc.common.web.contoller.BasicController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "")
public class AttachmentListController extends BasicController<AttachmentListBiz, AttachmentList, AttachmentListParam> {

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public List<AttachmentList> uploadFile(@RequestParam("file") MultipartFile[] attachments,
			@RequestParam("referenceTable") String referenceTable, @RequestParam("referenceId") String referenceId)
					throws IOException, Exception {
		if (attachments == null || attachments.length == 0) {
			throw new IscException("ISC-925", "attachments null");
		}
		if (CommUtil.isEmpty(referenceTable)) {
			throw new IscException("ISC-924", "ReferenceTable null");
		}
		if (CommUtil.isEmpty(referenceId)) {
			throw new IscException("ISC-924", "ReferenceId null");
		}
		AttachmentListParam param = new AttachmentListParam();
		param.setProfile(BaseContextHandler.getProfile());
		param.setApplication(BaseContextHandler.getApp());
		param.setReferenceId(referenceId);
		param.setReferenceTable(referenceTable);

		return baseBiz.uploadFiles(param, attachments);
	}

	/**
	 * 获取附件列表
	 * 
	 * @param param
	 *            必须指定referenceId 必须指定referenceTable
	 * @return 附件清单
	 * @throws IscException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/listFile", method = RequestMethod.POST)
	public @ResponseBody List<AttachmentList> listFile(HttpServletRequest request,
			@RequestBody AttachmentListParam param) throws IscException, UnsupportedEncodingException {
		if (param.getReferenceTable() == null) {
			throw new IscException("ISC-924", "ReferenceTable null");
		}
		if (param.getReferenceId() == null) {
			throw new IscException("ISC-924", "ReferenceId null");
		}

		return baseBiz.selectList(param);
	}

	/**
	 * 附件下载
	 * 
	 * @param attachmentId
	 *            附件ID
	 */
	@IgnoreUserToken
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @RequestParam("attachmentId") int attachmentId)
			throws Exception {
		baseBiz.downloadAttachment(response, attachmentId);
	}

	/**
	 * 删除附件，以applicaiton/json方式调用
	 * 
	 * @param param
	 * @return 成功与否
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public @ResponseBody int deleteFile(@RequestBody AttachmentListParam param) throws Exception {
		return baseBiz.deleteAttachment(param);
	}
	
//	@RequestMapping(value = "/docx2pdfx", method = RequestMethod.POST)
//	public @ResponseBody AttachmentList docx2pdfx(HttpServletRequest request, HttpServletResponse response , @RequestBody AttachmentListParam param) throws Exception {
//		return baseBiz.docx2pdfx(request, response, param);
//	}
	
	@RequestMapping(value="/docx2pdfx")
	public @ResponseBody AttachmentList docx2pdfx(HttpServletRequest request,HttpServletResponse response , @RequestParam Integer attachmentId) throws IOException {
		return baseBiz.docx2pdfx(request, response,attachmentId);
	}
}