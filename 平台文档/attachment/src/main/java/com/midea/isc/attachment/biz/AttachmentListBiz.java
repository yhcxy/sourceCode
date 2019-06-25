package com.midea.isc.attachment.biz;

import com.midea.isc.attachment.mapper.AttachmentListMapper;
import com.midea.isc.attachment.model.AttachmentConfig;
import com.midea.isc.attachment.model.AttachmentList;
import com.midea.isc.attachment.param.AttachmentConfigParam;
import com.midea.isc.attachment.param.AttachmentListParam;
import com.midea.isc.attachment.sys.OSS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.model.FileInfo;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.BaseContextHandler;
import com.midea.isc.common.util.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentListBiz extends BaseBiz<AttachmentListMapper, AttachmentList, AttachmentListParam> {

	@Value("${spring.cloud.config.profile}")
	private String envTag;

	@Autowired
	AttachmentConfigBiz configBiz;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Environment env;
	
//	@Value("${atttachment.docx2pdfx.sign}")
//	private String signTag;
//	
//	@Value("${atttachment.docx2pdfx.sys}")
//	private String sysTag;
//	
//	@Value("${atttachment.docx2pdfx.downloadUrl}")
//	private String downloadUrl;

	public List<AttachmentList> uploadFiles(AttachmentListParam param, MultipartFile[] attachments)
			throws IOException, Exception {
		List<AttachmentList> result = new ArrayList<AttachmentList>();
		if (param.getProfile() == null) {
			Profile profile = new Profile();
			profile.set__userName("admin");
			param.setProfile(profile);
		}
		AttachmentConfigParam configParam = new AttachmentConfigParam();
		configParam.setApplication(param.getApplication());
		AttachmentConfig config = configBiz.selectOne(configParam);
		if (config != null) {
			for (MultipartFile attachment : attachments) {
				newUploadFile(param, attachment, config, param.getReferenceId());
			}

			AttachmentListParam queryParam = new AttachmentListParam();
			queryParam.setProfile(param.getProfile());
			queryParam.setReferenceTable(param.getReferenceTable());
			queryParam.setReferenceId(param.getReferenceId());
			queryParam.setApplication(param.getApplication());
			result = mapper.find(queryParam);
			
		} else {
			throw new IscException("ISC-926");
		}
		return result;
	}

	public int uploadFile(AttachmentListParam param, MultipartFile attachment, Integer referenceId) throws IscException {
		if (param.getProfile() == null) {
			Profile profile = new Profile();
			profile.set__userName(param.getApplication() + "-FTP-system");
			param.setProfile(profile);
		}
		AttachmentConfigParam configParam = new AttachmentConfigParam();
		configParam.setApplication(param.getApplication());
		//cache
		AttachmentConfig config = configBiz.selectOne(configParam);
		int i = 0;
		if (config != null) {
			String ret = this.newUploadFile(param, attachment, config, String.valueOf(referenceId));
			try{
				return Integer.parseInt(ret);
			}catch (NumberFormatException e){
				throw new IscException("ISC-924", ret);
			}
		}

		throw new IscException("ISC-924", "application null");
	}

	@SuppressWarnings("unused")
	public void downloadAttachment(HttpServletResponse response, int attachmentId) throws IOException, IscException {
		AttachmentListParam param = new AttachmentListParam();
		param.setAttachmentId(attachmentId);
		AttachmentList attachment = mapper.selectOne(param);
		if (param != null) {
			AttachmentConfigParam configParam = new AttachmentConfigParam();
			configParam.setApplication(attachment.getApplication());
			AttachmentConfig config = configBiz.selectOne(configParam);
			OSS.downloadFile(response, config, attachment);
		} else {
			throw new IscException("ISC-928");
		}
	}

	public FileInfo rpcDownloadAttachment(int attachmentId) throws IOException, IscException {
		AttachmentListParam param = new AttachmentListParam();
		param.setAttachmentId(attachmentId);
		AttachmentList attachment = mapper.selectOne(param);
		AttachmentConfigParam configParam = new AttachmentConfigParam();
		configParam.setApplication(attachment.getApplication());
		AttachmentConfig config = configBiz.selectOne(configParam);
		return OSS.rpcDownloadFile(config, attachment);
	}

    public List<FileInfo> rpcBatchDownloadAttachment (int[] attachmentIds) throws IOException, IscException {
        List<FileInfo> infos = new ArrayList<>();
		for (int attachmentId : attachmentIds) {
			infos.add(attachmentId,this.rpcDownloadAttachment(attachmentId));
		}
        return infos;
    }

	public int deleteAttachment(AttachmentListParam param) throws Exception {
		int result = 0;
		List<AttachmentList> attachmentList = mapper.selectList(param);
		HashMap<String, AttachmentConfig> configList = new HashMap<>();
		for (AttachmentList attachment : attachmentList) {
			AttachmentConfig config = null;
			if (!configList.containsKey(attachment.getApplication())) {
				AttachmentConfigParam configParam = new AttachmentConfigParam();
				configParam.setApplication(attachment.getApplication());
				config = configBiz.selectOne(configParam);
				configList.put(attachment.getApplication(), config);
			} else {
				config = configList.get(attachment.getApplication());
			}
			AttachmentListParam deleteParam = new AttachmentListParam();
			deleteParam.setAttachmentId(attachment.getAttachmentId());
			if (mapper.delete(deleteParam) > 0) {
				result += 1;
				OSS.deleteFile(config, attachment);
			}
		}

		return result;
	}

	private String getDirectory(boolean unix, AttachmentListParam param) {
		String rootDir = OSS.getAttachmentRootDir();
		// 按application，table分开存储
		rootDir = rootDir + (rootDir.endsWith(unix ? "/" : "\\") ? "" : (unix ? "/" : "\\"));
		return rootDir + param.getApplication() + (unix ? "/" : "\\") + param.getReferenceTable();
	}

	private String newUploadFile(AttachmentListParam param, MultipartFile attachment, AttachmentConfig config, String detailId){
		String ret = null;
		try{
			String fullName = attachment.getOriginalFilename();
			String displayName = fullName.indexOf('.') == -1 ? fullName : fullName.substring(0, fullName.indexOf('.'));
			String fileType = fullName.substring(fullName.indexOf('.') + 1);
			boolean unix = "/".equals(System.getProperties().getProperty("file.separator"));
			String dir = getDirectory(unix, param);
			// 生成唯一的文件名
			String fileName = UUID.randomUUID().toString() + fullName.substring(fullName.indexOf('.') == -1 ? 0 : fullName.indexOf('.') );
			// 文件完整路径（包含文件名）
			String path = dir + (unix ? "/" : "\\") + fileName;
			param.setAttachmentId(null);
			String bucket = param.getApplication() + "-" + param.getReferenceTable();
			param.setFileName(
					OSS.uploadFile(bucket, path, attachment.getSize(), attachment.getInputStream(), config));
			param.setDisplayName(displayName);
			param.setFileType(fileType);
			param.setFileSize(attachment.getSize());
			param.setFileDirectory(dir);
			param.setCreatedBy(param.getProfile().get__userName());
			param.setCreationDate(CommUtil.getTimeZoneDatetime(0));
			param.setLastUpdatedBy(param.getProfile().get__userName());
			param.setLastUpdateDate(CommUtil.getTimeZoneDatetime(0));
            param.setReferenceId(detailId);
			mapper.insert(param);

			ret = String.valueOf(param.getAttachmentId());
		}catch (IOException e){
			e.printStackTrace();
			ret = "File transport error";
		}catch (ParseException e){
			e.printStackTrace();
			ret = "Parsing time error";
		}catch (Exception e){
			e.printStackTrace();
			ret = "error";
		}finally {
			return ret;
		}
	}
	
	public AttachmentList docx2pdfx(HttpServletRequest request, HttpServletResponse response, AttachmentListParam param) throws Exception {
		if( null == param || null == param.getAttachmentId() ) {
			throw new IscException("ISC-928");
		}
		AttachmentList attachment = mapper.selectOne(param);
		if( null == attachment ) {
			throw new IscException("ISC-928");
		}
		String ssoToken = "";
		if(null != param.getProfile() && null != param.getProfile().get__token() && !"".equals(param.getProfile().get__token()) ) {
			ssoToken = param.getProfile().get__token();
		}else {
			ssoToken = BaseContextHandler.getToken();
		}
		System.out.println("ssoToken==="+ssoToken);
		OSS.docx2pdfx(request, response, attachment, ssoToken);
		return attachment;
	}
	
	public AttachmentList docx2pdfx(HttpServletRequest request, HttpServletResponse response, Integer attachmentId) throws IOException {
		if( null == attachmentId  ) {
			throw new IscException("ISC-928");
		}
		AttachmentListParam param = new AttachmentListParam();
		param.setAttachmentId(attachmentId);
		AttachmentList attachment = mapper.selectOne(param);
		if( null == attachment ) {
			throw new IscException("ISC-928");
		}
		String ssoToken = "";
		if(null != param.getProfile() && null != param.getProfile().get__token() && !"".equals(param.getProfile().get__token()) ) {
			ssoToken = param.getProfile().get__token();
		}else {
			ssoToken = BaseContextHandler.getToken();
		}
		System.out.println("ssoToken==="+ssoToken);
		OSS.docx2pdfx(request, response, attachment, ssoToken);
		return attachment;
	}
}
