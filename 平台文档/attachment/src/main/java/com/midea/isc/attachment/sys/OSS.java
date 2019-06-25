package com.midea.isc.attachment.sys;

import com.fasterxml.jackson.core.type.TypeReference;
import com.midea.isc.attachment.model.AttachmentConfig;
import com.midea.isc.attachment.model.AttachmentList;
import com.midea.isc.attachment.model.MideaOSSReturn;
import com.midea.isc.common.model.FileInfo;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.CommUtil;
import com.midea.isc.common.util.JacksonUtils;
import com.midea.isc.common.util.TimeZoneConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class OSS {

	@Autowired
	private Environment env;

	private static MideaOSSCertification mideaOssCertService;

	@Autowired
	public void setCertification(MideaOSSCertification mideaOssCert) {
		OSS.mideaOssCertService = mideaOssCert;
	}

	/**
	 * 单机环境时的附件存储目录，集群环境不使用此属性
	 */
	private static String attachmentRootDir;
	private static String mideaOSSAuthServiceAddr;
	public static String mideaOSSApplServiceAddr;
	// private static int maxTempFileSizeMB;

	private static String atttachmentDocx2pdfxDownloadUrl;
	private static String atttachmentDocx2pdfxSign;
	private static String atttachmentDocx2pdfxSys;
	
	@PostConstruct
	public void readConfig() {
		attachmentRootDir = env.getProperty("atttachment.rootdir");
		mideaOSSAuthServiceAddr = env.getProperty("atttachment.mideaOSSAuthServiceAddr");
		mideaOSSApplServiceAddr = env.getProperty("atttachment.mideaOSSApplServiceAddr");
		
		atttachmentDocx2pdfxDownloadUrl = env.getProperty("atttachment.docx2pdfx.downloadUrl");
		atttachmentDocx2pdfxSign = env.getProperty("atttachment.docx2pdfx.sign");
		atttachmentDocx2pdfxSys = env.getProperty("atttachment.docx2pdfx.sys");
	}

	public static String getAttachmentRootDir() {
		return CommUtil.isEmpty(attachmentRootDir) ? "/apps/tmp" : attachmentRootDir;
	}
	
	public static String getAtttachmentDocx2pdfxDownloadUrl() {
		return atttachmentDocx2pdfxDownloadUrl;
	}

	public static String getAtttachmentDocx2pdfxSign() {
		return atttachmentDocx2pdfxSign;
	}

	public static String getAtttachmentDocx2pdfxSys() {
		return atttachmentDocx2pdfxSys;
	}

	/**
	 * 上传永久文件
	 * 
	 * @param bucket
	 *            OSS 卷名，用于文件的分组管理，类似文件夹，仅用于OSS存储服务
	 * @param file
	 *            文件保存路径（包括文件名）
	 * @param length
	 *            文件大小（字节）
	 * @param is
	 *            文件输入流
	 * @return 保存后的文件名或者标识
	 * @throws IOException
	 *             非分布式文件存储环境返回读写异常
	 */
	public static String uploadFile(String bucket, String file, long length, InputStream is, AttachmentConfig config)
			throws Exception {
		if (config != null) {
			return mideaOSSPut(bucket, file, length, is, config);
		} else
			return writeLocalFile(file, is);
	}

	public static void downloadFile(HttpServletResponse response, AttachmentConfig config, AttachmentList attachment)
			throws IOException, IscException {
		String type = attachment.getFileType();
//		if ("jpg".equalsIgnoreCase(type) || "png".equalsIgnoreCase(type)) {
//			response.setHeader("Content-Type", "image/" + ("jpg".equalsIgnoreCase(type) ? "jpeg" : type));
//		} else if ("pdf".equalsIgnoreCase(type)) {
//			response.setHeader("Content-Type", "application/pdf");
//		} else if ("html".equalsIgnoreCase(type)) {
//			response.setHeader("Content-Type", "text/html");
//		} else {
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("application/x-download");
//			String displayName = attachment.getDisplayName() + "." + type;
//			String outName = CommUtil.isEmpty(displayName) ? attachment.getFileName() : displayName.replace(" ", "");
//			outName = URLEncoder.encode(outName, "UTF-8");
//			response.addHeader("Content-Disposition", "attachment;filename=" + outName);
//		}
		
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		String displayName = attachment.getDisplayName() + "." + type;
		String outName = CommUtil.isEmpty(displayName) ? attachment.getFileName() : displayName.replace(" ", "");
		outName = URLEncoder.encode(outName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + outName);


		if (config != null) { // 有config从美云盘下载
			downloadFromOSS(config, attachment, response.getOutputStream());
		} else { // 没有config从本地下载
			boolean unix = "/".equals(System.getProperties().getProperty("file.separator"));
			String filePath = attachment.getFileDirectory() + (unix ? "/" : "\\") + attachment.getFileName();

			File file = new File(filePath);
			if (file.exists()) {
				try (FileInputStream in = new FileInputStream(file);
						ServletOutputStream out = response.getOutputStream()) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.flush();
				}
			} else {
				throw new IscException("ISC-927");
			}
		}
	}

    public static FileInfo rpcDownloadFile(AttachmentConfig config, AttachmentList attachment)
            throws IOException, IscException {
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(attachment,fileInfo);

        ByteArrayOutputStream byteArrayOutputStream;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try{
            if (config != null) { // 有config从美云盘下载
                String bucket = attachment.getApplication() + "-" + attachment.getReferenceTable();
                String address = "http://" + mideaOSSAuthServiceAddr + "/v1/oss/object/download/" + config.getAppId() + "/" + bucket + "/" + attachment.getFileName();
                URL url = new URL(address);
                SimpleDateFormat d = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Date", d.format(TimeZoneConverter.autoTransferTimeZone(d)));
                String certification = mideaOssCertService.certificate(config);
                conn.setRequestProperty("certification", certification);
                conn.connect();
                inputStream = conn.getInputStream();
            } else { // 没有config从本地下载
                boolean unix = "/".equals(System.getProperties().getProperty("file.separator"));
                String filePath = attachment.getFileDirectory() + (unix ? "/" : "\\") + attachment.getFileName();
                File file = new File(filePath);
                inputStream = new FileInputStream(file);
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        }finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
            try {
                conn.disconnect();
            } catch (Exception e) {
            }
        }
        fileInfo.setBody(byteArrayOutputStream.toByteArray());
        return fileInfo;
    }

	/**
	 * 删除永久文件
	 * 
	 * @param bucket
	 *            OSS 卷号
	 * @param fileName
	 *            文件名（包括文件类型后缀但不包括目录）或者文件标识
	 * @param dir
	 *            目录（启用DFS时不需要提供）
	 */
	public static void deleteFile(AttachmentConfig config, AttachmentList attachment) throws Exception {
		if (config != null) {
			String address = "http://" + mideaOSSApplServiceAddr + "/v1/appmanager/object/delete";
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("appid", config.getAppId());
			String certification = mideaOssCertService.certificate(config);
			param.put("certification", certification);
			String bucket = attachment.getApplication() + "-" + attachment.getReferenceTable();
			param.put("bucket", bucket);
			param.put("object", attachment.getFileName());
			String resp = CommUtil.callRestful(address, JacksonUtils.obj2json(param));
			if (resp != null) {
				MideaOSSReturn<HashMap<String, Object>> result = JacksonUtils.json2Object(resp,
						new TypeReference<MideaOSSReturn<HashMap<String, Object>>>() {
						});
				if (!"200".equals(result.getCode())) {
					throw new IOException(result.getMsg());
				}
			}
		} else {
			boolean unix = "/".equals(System.getProperties().getProperty("file.separator"));
			File file = new File(attachment.getFileDirectory() + (unix ? "/" : "\\") + attachment.getFileName());

			if (file.exists() && file.isFile())
				file.delete();
		}
	}

	/**
	 * 从OSS服务器或者DFS服务器下载临时文件
	 * 
	 * @param bucket
	 *            OSS 卷号
	 * @param objectId
	 *            文件标识
	 * @param os
	 *            输出流，下载的文件将保存到此输出流，输出完毕后关闭输出流
	 * @return true-成功下载，false-未启用OSS或者DFS
	 * @throws IscException
	 * @throws Exception
	 */
	private static void downloadFromOSS(AttachmentConfig config, AttachmentList attachment, final OutputStream os)
			throws IOException, IscException {
		try {
			String bucket = attachment.getApplication() + "-" + attachment.getReferenceTable();
			String address = "http://" + mideaOSSAuthServiceAddr + "/v1/oss/object/download/" + config.getAppId() + "/"
					+ bucket + "/" + attachment.getFileName();
			URL url = new URL(address);
			SimpleDateFormat d = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Date", d.format(TimeZoneConverter.autoTransferTimeZone(d)));
			String certification = mideaOssCertService.certificate(config);
			conn.setRequestProperty("certification", certification);
			conn.connect();

			try (InputStream is = conn.getInputStream()) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
			} finally {
				conn.disconnect();
			}

		} finally {
			os.flush();
			os.close();
		}
	}

	/**
	 * 存储文件到美的云
	 * 
	 * @param bucket
	 *            卷号，需要事先在美的云控制台创建
	 * @param file
	 *            文件名
	 * @param length
	 *            文件大小（字节）
	 * @param is
	 *            文件输入流
	 * @return 保存的文件标识
	 * @throws Exception
	 */
	private static String mideaOSSPut(String bucket, String file, long length, InputStream is, AttachmentConfig config)
			throws Exception {
		String fileType = file.substring(file.lastIndexOf(".") == -1 ? file.length() : file.lastIndexOf("."));
		String objectId = UUID.randomUUID().toString() + fileType;
		String address = "http://" + mideaOSSAuthServiceAddr + "/v1/oss/object/upload/" + config.getAppId() + "/"
				+ bucket + "/" + objectId;
		URL url = new URL(address);
		SimpleDateFormat d = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		String certification = mideaOssCertService.certificate(config);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("Date", d.format(TimeZoneConverter.autoTransferTimeZone(d)));
		conn.setRequestProperty("x-amz-acl", "public-read-write");
		conn.setRequestProperty("certification", certification);
		conn.setRequestProperty("content-length", String.valueOf(length));
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();

		try (OutputStream os = conn.getOutputStream()) {
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();

			int responseCode = conn.getResponseCode();
			if (responseCode == 401)
				throw new IscException("ISC-921", certification,
						String.format("Invalid certification s% of Midea OSS.", certification));
			if (responseCode == 404)
				throw new IscException("ISC-922", bucket,
						String.format("Bucket {0} doesn't exist,please create it on Midea OSS Workbench.", bucket));
			if (responseCode == 413)
				throw new IscException("ISC-923", "Request Entity Too Large!");

			log.debug("code:", responseCode + "", ",x-amz-request-id:", conn.getRequestProperty("x-amz-request-id"),
					",ETag:", conn.getRequestProperty("ETag"));

			return responseCode == 200 ? objectId : null;
		} finally {
			conn.disconnect();
		}
	}

	private static String writeLocalFile(String file, InputStream is) throws IOException {
		boolean unix = "/".equals(System.getProperties().getProperty("file.separator"));
		String dir = file.substring(0, file.lastIndexOf(unix ? "/" : "\\"));
		File directory = new File(dir);
		if (!directory.exists())
			directory.mkdirs();

		File outputFile = new File(file);
		try (FileOutputStream writer = new FileOutputStream(outputFile)) {

			outputFile.createNewFile();

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				writer.write(buffer, 0, len);
			}
			return file.substring(file.lastIndexOf(unix ? "/" : "\\") + 1);
		} finally {
			if (is != null)
				is.close();
		}
	}

	public static void docx2pdfx(HttpServletRequest request , HttpServletResponse response , AttachmentList attachment , String ssoToken) throws IOException  {
		Map<String,Object> params = new HashMap<String, Object>();
		String sign = getAtttachmentDocx2pdfxSign();
		String sys = getAtttachmentDocx2pdfxSys();
		String url = OSS.getAtttachmentDocx2pdfxDownloadUrl();
		if(CommUtil.isEmpty(sign) || CommUtil.isEmpty(sys) || CommUtil.isEmpty(url)) {
//			throw new Exception("sign or sys or url cannot be null!!");
			throw new IscException("ISC-999", "sign or sys or url cannot be null!!");
		}
		String[] fileTypes = {"doc","docx","xls","xlsx","ppt","pptx","jpg","bmp","gif","png","jpeg","tif","psd","wmf","cdr","dwg","ai"};
		List<String> fileTypeList = java.util.Arrays.asList(fileTypes);
		if(!fileTypeList.contains(attachment.getFileType())) {
			throw new IscException("ISC-999", "file type is not correct!");
//			throw new Exception("file type is not correct!");
		}
		params.put("fd", attachment.getAttachmentId());
		params.put("ext", attachment.getFileType());
		params.put("sign",  sign);
		params.put("sys", sys);
		params.put("mideatest_sso_token",ssoToken);
		String src = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/downloadFile?attachmentId="+attachment.getAttachmentId();
		params.put("src", src);
		String paramStr = "";
		String urlName = "";
		if (params != null) {
			for (String key : params.keySet()) {
				paramStr = paramStr + "&" + key + "=" + URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8");
			}
			urlName = url + "?" + paramStr;
		}
		URL realUrl  = new URL(urlName);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			response.setContentType("application/pdf");
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			try (InputStream is = conn.getInputStream()) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
			} finally {
				conn.disconnect();
			}
	
		} finally {
			os.flush();
			os.close();
		}
	}
}
