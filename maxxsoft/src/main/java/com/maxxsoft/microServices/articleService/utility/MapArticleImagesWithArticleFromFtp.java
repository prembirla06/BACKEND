package com.maxxsoft.microServices.articleService.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.model.request.ImageRequest;
import com.maxxsoft.microServices.articleService.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MapArticleImagesWithArticleFromFtp {
	@Autowired
	ImageService imageService;

	@Value("${ftp.username2}")
	private String username;
	@Value("${ftp.password2}")
	private String password;
	@Value("${ftp.host2}")
	private String host;
	@Value("${ftp.port2}")
	private int port;

	private DefaultFtpSessionFactory defaultFtpSessionFactory() {
		DefaultFtpSessionFactory defaultSftpSessionFactory = new DefaultFtpSessionFactory();
		defaultSftpSessionFactory.setPassword(password);
		defaultSftpSessionFactory.setUsername(username);
		defaultSftpSessionFactory.setHost(host);
		defaultSftpSessionFactory.setPort(port);
		// defaultSftpSessionFactory.setAllowUnknownKeys(true);
		return defaultSftpSessionFactory;
	}

	// @Scheduled(fixedRate = 1000000)
	public void downloadArticleImages(Long articleId, String articleNumber) {
		// only add images if there are no images added before
		if (imageService.findArticleImageByArticleId(articleId).isEmpty()) {
			System.out.println("*******ArticleId*******" + articleId);
			System.out.println("*******ArticleId*******" + "/moebel/" + articleNumber + "/");
			String refactoredAtricleNumber = refactorArticleNumber(articleNumber);
			System.out
					.println("*******refactoredAtricleNumber**path*****" + "/moebel/" + refactoredAtricleNumber + "/");
			// SftpSession session = new
			// SftpSessionFactoryHandler().defaultSftpSessionFactory().getSession();
			FtpSession session = defaultFtpSessionFactory().getSession();
			try {
				System.out.println(session.exists("/moebel/" + refactoredAtricleNumber + "/"));
				String[] files = session.listNames("/moebel/" + refactoredAtricleNumber + "/");
				Arrays.stream(files).forEach(file -> {
					System.out.println("files++++outer++++++++==" + file);
					String notAnImage = file.split("\\/")[3];
					if (!notAnImage.equalsIgnoreCase("aufbauanleitung") && !notAnImage.equalsIgnoreCase(".")
							&& !notAnImage.equalsIgnoreCase("..")) {
						try {
							System.out.println(session.isOpen());
							if (session.isOpen()) {
								FtpSession session2 = defaultFtpSessionFactory().getSession();
								FTPFile[] fileNames = session2.list(file);
								if (fileNames != null) {
									Arrays.stream(fileNames).forEach(fileName -> {
										if (!fileName.getName().equals(".") && !fileName.getName().equals("..")) {
											System.out.println("files+++++inner+++++++==" + fileName.getName());
											InputStream inputStream;
											try {
												inputStream = session2.readRaw(file + "/" + fileName.getName());

												ByteArrayOutputStream bos = new ByteArrayOutputStream();
												byte[] buf = new byte[9000000];
												try {
													for (int readNum; (readNum = inputStream.read(buf)) != -1;) {
														bos.write(buf, 0, readNum);
													}
												} catch (IOException ex) {
													System.out.println(ex);
												}
												byte[] bytes = bos.toByteArray();
												ImageRequest imageRequest = new ImageRequest(fileName.getName(),
														Integer.valueOf(file.substring(file.length() - 1)), bytes);
												imageService.addArticleImage(articleId, imageRequest);
												System.out.println("Done");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});
								}
							}

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("Article: " + articleNumber + " has already images");
		}
	}

	public void downloadArticleSetImages(Long articleSetId, String articleNumber) {
		// only add images if there are no images added before
		if (imageService.findArticleSetImageByArticleSetId(articleSetId).isEmpty()) {
			System.out.println("*******articleSetId*******" + articleSetId);
			System.out.println("*******articleNumber*******" + "/moebel/" + articleNumber + "/");
			String refactoredAtricleNumber = refactorArticleNumber(articleNumber);
			System.out
					.println("*******refactoredAtricleNumber**path*****" + "/moebel/" + refactoredAtricleNumber + "/");
			// SftpSession session = new
			// SftpSessionFactoryHandler().defaultSftpSessionFactory().getSession();
			FtpSession session = defaultFtpSessionFactory().getSession();
			try {
				System.out.println(session.exists("/moebel/" + refactoredAtricleNumber + "/"));
				String[] files = session.listNames("/moebel/" + refactoredAtricleNumber + "/");
				Arrays.stream(files).forEach(file -> {
					System.out.println("files++++outer++++++++==" + file);
					String notAnImage = file.split("\\/")[3];
					if (!notAnImage.equalsIgnoreCase("aufbauanleitung") && !notAnImage.equalsIgnoreCase(".")
							&& !notAnImage.equalsIgnoreCase("..")) {
						try {
							System.out.println(session.isOpen());
							if (session.isOpen()) {
								FtpSession session2 = defaultFtpSessionFactory().getSession();
 								FTPFile[] fileNames = session2.list(file);
								if (fileNames != null) {
									Arrays.stream(fileNames).forEach(fileName -> {
										if (!fileName.getName().equals(".") && !fileName.getName().equals("..")) {
											System.out.println("files+++++inner+++++++==" + fileName.getName());
											InputStream inputStream;
											try {
												inputStream = session2.readRaw(file + "/" + fileName.getName());

												ByteArrayOutputStream bos = new ByteArrayOutputStream();
												byte[] buf = new byte[9000000];
												try {
													for (int readNum; (readNum = inputStream.read(buf)) != -1;) {
														bos.write(buf, 0, readNum);
													}
												} catch (IOException ex) {
													System.out.println(ex);
												}
												byte[] bytes = bos.toByteArray();
												// get position
												int position = 0;
												if (file.substring(file.length() - 2).equals("0")) {
													position = Integer.valueOf(file.substring(file.length() - 1));
												} else {
													position = Integer.valueOf(file.substring(file.length() - 2));
												}
												System.out.println("image position: " + position);
												ImageRequest imageRequest = new ImageRequest(fileName.getName(),
														position, bytes);
												imageService.addArticleSetImage(articleSetId, imageRequest);
												System.out.println("Done");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});
								}
							}

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("ArticleSet: " + articleNumber + " has already images");
		}
	}

	private String refactorArticleNumber(String articleNumber) {
		String result;
		result = articleNumber.replace(' ', '-');
		result = result.replace('/', '-');
		result = result.replace('_', '-');
		result = result.replace('.', '-');
		result = result.replace("+", "--");
		result = result.replace("ß", "ss");
		return result;
	}
}