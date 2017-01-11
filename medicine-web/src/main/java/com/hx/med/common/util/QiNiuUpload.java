package com.hx.med.common.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tengxianfei
 * @description
 * @since 4.0.0
 */
public final class QiNiuUpload {

    /**
     * 日志对象.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(QiNiuUpload.class.getName());
    /**
     * 构造器.
     */
    private QiNiuUpload() {
    }
    /**
     * 上传文件.
     * @param base64Str base64Str
     * @param qiniuFilePrefix qiniuFilePrefix
     * @param qiniuAccessKey qiniuAccessKey
     * @param qiniuSecretKey qiniuSecretKey
     * @param qiniuBucketName qiniuBucketName
     * @return String
     */
    public static String upload(final String base64Str, final String qiniuFilePrefix, final String qiniuAccessKey,
                                final String qiniuSecretKey, final String qiniuBucketName) {
//        LOGGER.debug("upload --> qiniuFilePrefix {}", qiniuFilePrefix);
//        LOGGER.debug("upload --> qiniuAccessKey {}", qiniuAccessKey);
//        LOGGER.debug("upload --> qiniuSecretKey {}", qiniuSecretKey);
//        LOGGER.debug("upload --> qiniuBucketName {}", qiniuBucketName);
        String address = "";
        final File file = new File("anchorhead.jpg");
        try {
            final OutputStream os = new FileOutputStream(file);
            final InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(base64Str));
            int bytesRead = 0;
            final int bufferSize = 8192;
            final byte[] buffer = new byte[bufferSize];
            while ((bytesRead = ins.read(buffer, 0, bufferSize)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (final IOException e) {
            LOGGER.error("[system error] upload file error: ", e);
        }

        final String token = getUploadToken(qiniuAccessKey, qiniuSecretKey, qiniuBucketName);
        final String fileName = file.getName();
        final StringBuilder key = new StringBuilder();
        key.append(WebExt.getMD5Str(Objects.toString(System.currentTimeMillis())))
                .append('/')
                .append(fileName);

        try {
            final Response response = new UploadManager().put(file, key.toString(), token);
            LOGGER.info("upload body result=[{}]", response.bodyString());
            if (response.isOK()) {
                final StringMap result = response.jsonToMap();
                final String fileKey = Objects.toString(result.get("key"));
                if (!WebExt.isBlank(fileKey)) {
                    address = qiniuFilePrefix + fileKey;
                }
            }
        } catch (final QiniuException var) {
            final Response r = var.response;
            LOGGER.error("[system error] upload file error: ", var);
        } catch (final Exception e) {
            LOGGER.error("[system error] upload file error: ", e);
        }
        return address;
    }

    /**
     * 获取上传token.
     * @param qiniuAccessKey qiniuAccessKey
     * @param qiniuSecretKey qiniuSecretKey
     * @param qiniuBucketName qiniuBucketName
     * @return String
     */
    public static String getUploadToken(final String qiniuAccessKey, final String qiniuSecretKey, final String qiniuBucketName) {
        final Auth auth = Auth.create(qiniuAccessKey, qiniuSecretKey);
        return auth.uploadToken(qiniuBucketName);
    }
}
