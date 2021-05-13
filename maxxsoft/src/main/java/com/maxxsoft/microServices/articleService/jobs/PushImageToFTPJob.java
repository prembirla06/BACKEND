/*******************************************************
* Copyright (C) 2020, TecMaXX GmbH
* All Rights Reserved.
* 
* NOTICE: All information contained herein is, and remains
* the property of TecMaXX GmbH and its suppliers,
* if any. The intellectual and technical concepts contained
* herein are proprietary to TecMaXX GmbH
* and its suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from TecMaXX GmbH.
* 
* TecMaXX GmbH
* Auf der Suend 18, DE-91757 Treuchtlingen
*******************************************************/
package com.maxxsoft.microServices.articleService.jobs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.articleService.utility.SftpUploadUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class PushImageToFTPJob {

	@Value("${sftp.location.article}")
	private String articleImageLocation;

	@Value("${sftp.location.articleSet}")
	private String articleSetImageLocation;

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private SftpUploadUtility sftpUploadUtility;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ImageService imageService;

	// @Scheduled(fixedRate = 10000000)
	public void runPushImageToFTPJob() {
		log.info("start...PushImageToFTPJob..");
		articleRepository.findAll().forEach(article -> {

			if (article.isStandalone() && article.getArticleId() < 11) {
				System.out.println("Article Id--- " + article.getArticleId());
				imageService.findArticleImageByArticleId(article.getArticleId()).forEach(image -> {
					InputStream targetStream = new ByteArrayInputStream(image.getPicByte());
					try {
						sftpUploadUtility.uploadFile(targetStream, image.getOrderNumber() + "_" + image.getName(),
								articleImageLocation + article.getArticleId());
					} catch (JSchException | SftpException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});
			}
		});
		articleSetRepository.findAll().forEach(articleSet -> {
			if (articleSet.getArticleSetId() < 11) {
				System.out.println("Article Set Id--- " + articleSet.getArticleSetId());
				imageService.findArticleSetImageByArticleSetId(articleSet.getArticleSetId()).forEach(image -> {
					InputStream targetStream = new ByteArrayInputStream(image.getPicByte());
					try {
						sftpUploadUtility.uploadFile(targetStream, image.getOrderNumber() + "_" + image.getName(),
								articleSetImageLocation + articleSet.getArticleSetId());
					} catch (JSchException | SftpException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});
			}

		});
		log.info("end...PushImageToFTPJob..");
	}

}