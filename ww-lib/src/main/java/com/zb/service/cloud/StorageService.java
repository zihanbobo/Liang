package com.zb.service.cloud;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.zb.common.Constant.ContentType;
import com.zb.common.Constant.ReCode;
import com.zb.common.crypto.MDUtil;
import com.zb.common.utils.DateUtil;
import com.zb.core.conf.Config;
import com.zb.core.web.ReMsg;
import com.zb.service.BaseService;

@Service
public class StorageService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(StorageService.class);

	String accessKeyId = "mhr4OMEPQMZl0RFX";
	String accessKeySecret = "t2uu4MjhaJdZmSxKqGa5TI4sqTitiV";

	// private static OSSClient CLIENT = null;
	// private static final String END_POINT_OUT =
	// "https://oss-cn-beijing.aliyuncs.com";
	private static final String END_POINT_internal = "https://oss-cn-beijing-internal.aliyuncs.com";
	private static final String END_POINT_HANGZHOU = "https://oss-cn-hangzhou-internal.aliyuncs.com";
	private static String TEMP_BUCKET = "imgt";
	private static String DEF_BUCKET = "imgzb";
	private static String QUZI_BUCKET = "zbim";
	private static String ZGIF_BUCKET = "zgif";
	private static String SHOW_BUCKET = "zim";
	private static String VIDEO_BUCKET = "zbvideo";
	private static String AUDIO_BUCKET = "zbaudio";
	private static String AUDIO_BUCKET_CDN = "audio";
	private static final String domain = ".zhuangdianbi.com";

	String endpoint = null;
	OSSClient CLIENT;

	public StorageService() {
		endpoint = Config.cur().get("aliyun.oos.endpoint", END_POINT_internal);
		CLIENT = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	public OSSClient getClient() {
		return CLIENT;// new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	public String saveFile(BufferedImage bi, boolean isTmp, String formatName) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = null;
		String url = null;
		try {
			imOut = ImageIO.createImageOutputStream(bs);
			ImageIO.write(bi, formatName, imOut);
			url = saveFile(bs, isTmp, formatName);
		} catch (IOException e) {
			log.error("close Err:" + e);
		} finally {
			try {
				if (null != imOut)
					imOut.close();
				if (null != bs)
					bs.close();
				if (bi != null) {
					bi.flush();
				}
			} catch (IOException e) {
				log.error("close Err:" + e);
			}
		}

		return url;
	}

	public String saveFile(ByteArrayOutputStream out, boolean isTmp, String formatName) {
		String bucket = isTmp ? TEMP_BUCKET : DEF_BUCKET;
		ObjectMetadata objectMeta = new ObjectMetadata();
		InputStream is = null;
		String key = new ObjectId().toHexString() + "." + formatName;
		String url = null;
		try {
			is = new ByteArrayInputStream(out.toByteArray());
			objectMeta.setContentLength(out.size());
			objectMeta.setContentType(getMIME(formatName));
			PutObjectResult result = getClient().putObject(bucket, key, is, objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				url = "http://" + bucket + domain + "/" + key;
			}
		} finally {
			try {
				if (null != is) {
					is.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				log.error("close Err:" + e);
			}
		}

		return url;
	}

	public String saveVideoFile(ByteArrayOutputStream out, String formatName) {
		String bucket = VIDEO_BUCKET;
		ObjectMetadata objectMeta = new ObjectMetadata();
		InputStream is = null;
		String key = new ObjectId().toHexString() + "." + formatName;
		String url = null;
		try {
			is = new ByteArrayInputStream(out.toByteArray());
			objectMeta.setContentLength(out.size());
			objectMeta.setContentType(getMIME(formatName));
			PutObjectResult result = new OSSClient(Config.cur().get("aliyun.oos.endpointhz", END_POINT_HANGZHOU),
					accessKeyId, accessKeySecret).putObject(bucket, key, is, objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				url = "http://" + bucket + domain + "/" + key;
			}
		} finally {
			try {
				if (null != is) {
					is.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				log.error("close Err:" + e);
			}
		}

		return url;
	}

	public String saveFile(String filePath, boolean isTmp) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(filePath));
			String formatName = filePath.substring(filePath.lastIndexOf(".") + 1);
			return saveFile(bi, isTmp, formatName);
		} catch (IOException e) {
			log.error("" + e);
		} finally {
			if (bi != null) {
				bi.flush();
			}
		}
		return null;

	}

	private String getMIME(String formatName) {
		formatName = formatName.toLowerCase();
		if ("png".equals(formatName)) {
			return "image/png";
		}
		if ("gif".equals(formatName)) {
			return "image/gif";
		}
		if ("jpeg".equals(formatName) || "jpg".equals(formatName)) {
			return "image/jpeg";
		}
		if ("bmp".equals(formatName)) {
			return "image/bmp";
		}
		return "image/png";
	}

	public ReMsg uploadVideo(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		String bucket = VIDEO_BUCKET;
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.getSize());
		objectMeta.setContentType(file.getContentType());
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
		String key = "tmps/" + DateUtil.dateFormat(new Date(), "MMdd") + "/" + new ObjectId().toHexString() + suffix;
		long st = System.currentTimeMillis();
		OSSClient client = null;
		try {
			String endpoint = Config.cur().get("hangzhou.aliyun.oos.endpoint", END_POINT_HANGZHOU);
			client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			PutObjectResult result = client.putObject(bucket, key, file.getInputStream(), objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				String url = "http://" + bucket + domain + "/" + key;
				return new ReMsg(ReCode.OK, url);
			}
		} catch (OSSException | ClientException | IOException e) {
			log.error("Upload err:" + e);
		} finally {
			client.shutdown();
		}
		System.out.println(System.currentTimeMillis() - st);
		return new ReMsg(ReCode.FAIL);
	}

	public ReMsg uploadAudio(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		String bucket = AUDIO_BUCKET;
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.getSize());
		objectMeta.setContentType(file.getContentType());
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
		String key = "tmps/" + DateUtil.dateFormat(new Date(), "MMdd") + "/" + new ObjectId().toHexString() + suffix;
		long st = System.currentTimeMillis();
		OSSClient client = null;
		try {
			String endpoint = Config.cur().get("aliyun.oos.endpoint", END_POINT_internal);
			client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			PutObjectResult result = client.putObject(bucket, key, file.getInputStream(), objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				String url = "http://" + AUDIO_BUCKET_CDN + domain + "/" + key;
				return new ReMsg(ReCode.OK, url);
			}
		} catch (OSSException | ClientException | IOException e) {
			log.error("Upload err:" + e);
		} finally {
			client.shutdown();
		}
		System.out.println(System.currentTimeMillis() - st);
		return new ReMsg(ReCode.FAIL);
	}

	public ReMsg upload(MultipartFile file, boolean isTmp, HttpServletRequest request, HttpServletResponse response) {
		String bucket = isTmp ? TEMP_BUCKET : DEF_BUCKET;
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.getSize());
		objectMeta.setContentType(file.getContentType());
		// String suffix = file.getOriginalFilename()
		// .substring(file.getOriginalFilename().lastIndexOf("."))
		// .toLowerCase();
		String key = new ObjectId().toHexString();// + suffix;
		long st = System.currentTimeMillis();
		try {
			PutObjectResult result = getClient().putObject(bucket, key, file.getInputStream(), objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				String url = "http://" + bucket + domain + "/" + key;
				return new ReMsg(ReCode.OK, url);
			}
		} catch (OSSException | ClientException | IOException e) {
			log.error("Upload err:" + e);
		}
		System.out.println(System.currentTimeMillis() - st);
		return new ReMsg(ReCode.FAIL);
	}

	public ReMsg upload(boolean isTmp, HttpServletRequest request, HttpServletResponse response) {
		long size = request.getParameter("size") != null ? Long.parseLong(request.getParameter("size")) : 0L;
		String type = request.getParameter("type");
		String bucket = isTmp ? TEMP_BUCKET : DEF_BUCKET;
		ObjectMetadata objectMeta = new ObjectMetadata();
		if (size > 0)
			objectMeta.setContentLength(size);
		objectMeta.setContentType(type);
		// String suffix = file.getOriginalFilename()
		// .substring(file.getOriginalFilename().lastIndexOf("."))
		// .toLowerCase();
		String key = new ObjectId().toHexString();// + suffix;
		long st = System.currentTimeMillis();
		try {
			PutObjectResult result = getClient().putObject(bucket, key, request.getInputStream(), objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				String url = "http://" + bucket + domain + "/" + key;
				return new ReMsg(ReCode.OK, url);
			}
		} catch (OSSException | ClientException | IOException e) {
			log.error("Upload err:" + e);
		}
		System.out.println(System.currentTimeMillis() - st);
		return new ReMsg(ReCode.FAIL);
	}

	public ReMsg uploadZim(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.getSize());
		objectMeta.setContentType(file.getContentType());
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
		String bucket = QUZI_BUCKET;
		if (".gif".equals(suffix)) {
			bucket = ZGIF_BUCKET;
		}
		String key = null;
		try {
			key = MDUtil.MD5.digest2HEX(file.getBytes(), true) + suffix;
		} catch (IOException e1) {
			key = new ObjectId().toHexString() + suffix;
		}
		// String key = new ObjectId().toHexString() + suffix;
		long st = System.currentTimeMillis();
		try {
			PutObjectResult result = save(bucket, key, file.getInputStream(), objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				String url = null;
				if (".gif".equals(suffix)) {
					url = "http://" + ZGIF_BUCKET + domain + "/" + key;
				} else {
					url = "http://" + SHOW_BUCKET + domain + "/" + key;
				}
				return new ReMsg(ReCode.OK, url);
			}
		} catch (OSSException | ClientException | IOException e) {
			log.error("Upload err:" + e);
		}
		System.out.println(System.currentTimeMillis() - st);
		return new ReMsg(ReCode.FAIL);
	}
	
	public ReMsg uploadZim(HttpEntity en) {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(en.getContentLength());
		Header h = en.getContentType();
		
		objectMeta.setContentType(h.getValue());
		String suffix = ContentType.get(h.getValue());
		String bucket = QUZI_BUCKET;
		if ("gif".equals(suffix)) {
			bucket = ZGIF_BUCKET;
		}
		String key = new ObjectId().toHexString() + "."+suffix;
		long st = System.currentTimeMillis();
		try {
			PutObjectResult result = save(bucket, key, en.getContent(), objectMeta);
			if (result.getETag() != null && !"".equals(result.getETag())) {
				String url = null;
				if ("gif".equals(suffix)) {
					url = "http://" + ZGIF_BUCKET + domain + "/" + key;
				} else {
					url = "http://" + SHOW_BUCKET + domain + "/" + key;
				}
				return new ReMsg(ReCode.OK, url);
			}
		} catch (OSSException | ClientException | IOException e) {
			log.error("Upload err:" + e);
		}
		System.out.println(System.currentTimeMillis() - st);
		return new ReMsg(ReCode.FAIL);
	}

	public PutObjectResult save(String bucket, String fileName, InputStream is, ObjectMetadata objectMeta) {
		if (fileName == null) {
			fileName = new ObjectId().toHexString();
		}
		try {
			if (objectMeta == null) {
				return getClient().putObject(bucket, fileName, is);
			} else {
				return getClient().putObject(bucket, fileName, is, objectMeta);
			}
		} catch (OSSException e) {
			log.error("Upload err:" + e);
		}
		return null;
	}

	public void deleteObject(String url) {
		// 删除Object
		getClient().deleteObject(getBucketName(url), getKeyName(url));
	}

	public OSSObject getObject(String url) {
		// 获取Object，返回结果为OSSObject对象
		return getClient().getObject(getBucketName(url), getKeyName(url));

		// // 获取Object Metadata
		// ObjectMetadata metadata = object.getObjectMetadata();
		//
		// // 获取Object的输入流
		// InputStream objectContent = object.getObjectContent();
		//
		// // 处理Object
		// ...
		//
		// // 关闭流，请注意，需要显式关闭，否则会造成资源泄露。
		// objectContent.close();
	}

	private static String getBucketName(String url) {
		return url.substring(url.indexOf("://") + 3, url.indexOf("."));
	}

	private static String getKeyName(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	private static final int S_DEF = 2;
	private static final int S_TO_DEL = 1;

	private void saveLog(String url, int status) {
		DBObject dbo = new BasicDBObject("_id", url).append("status", status).append("createTime",
				System.currentTimeMillis());

		super.getC("images").save(dbo);
	}
}
