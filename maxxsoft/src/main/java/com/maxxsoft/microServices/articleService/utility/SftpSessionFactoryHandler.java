package com.maxxsoft.microServices.articleService.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SftpSessionFactoryHandler {
	@Value("${ftp.username}")
	private String username;
	@Value("${ftp.password}")
	private String password;
	@Value("${ftp.host}")
	private String host;
	@Value("${ftp.port}")
	private int port;

	// public DefaultSftpSessionFactory defaultSftpSessionFactory() {
	// DefaultSftpSessionFactory defaultSftpSessionFactory = new
	// DefaultSftpSessionFactory();
	// defaultSftpSessionFactory.setPassword(password);
	// defaultSftpSessionFactory.setUser(username);
	// defaultSftpSessionFactory.setHost(host);
	// defaultSftpSessionFactory.setPort(port);
	// defaultSftpSessionFactory.setAllowUnknownKeys(true);
	// return defaultSftpSessionFactory;
	// }
}
