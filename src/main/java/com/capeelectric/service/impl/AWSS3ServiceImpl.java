package com.capeelectric.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class AWSS3ServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;


	public byte[] findByName(String fileName) throws Exception {
		log.info("Downloading file with name {}", fileName);
		S3Object s3Object = amazonS3.getObject(s3BucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (Exception e) {
			log.debug("Downloading file failed -->" + fileName);
			throw new Exception("Downloading file failed -->" + fileName);
		}
	}


}