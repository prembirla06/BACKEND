package com.maxxsoft.microServices.articleService.utility;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SftpUploadUtility {

	@Value("${sftp.username}")
	private String username;
	@Value("${sftp.password}")
	private String password;
	@Value("${sftp.host}")
	private String host;
	@Value("${sftp.port}")
	private int port;

	public void uploadFile(InputStream src, String name, String destinationPath)
			throws JSchException, SftpException, IOException {
		Session session = null;
		ChannelSftp channelSftp = null;
		try {

			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
			createRemoteFolderViaSFTP(channelSftp, destinationPath);
			channelSftp.cd(destinationPath);
			channelSftp.put(src, name, ChannelSftp.OVERWRITE);

			channelSftp.disconnect();
			session.disconnect();
		} finally {
			if (channelSftp != null && channelSftp.isConnected()) {
				channelSftp.disconnect();
			}

			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
	}

	private static void createRemoteFolderViaSFTP(ChannelSftp channelSftp, String remotePath) {
		String[] folders = remotePath.split("/");
		String remoteTempPath = "";
		for (String folder : folders) {
			if (StringUtils.isNotBlank(folder)) {
				remoteTempPath += "/" + folder;
				boolean flag = true;
				try {
					channelSftp.cd(remoteTempPath);
				} catch (SftpException e) {
					flag = false;
				}
				// log.info("change working directory : " + remoteTempPath +
				// "-->" + (flag ? "SUCCESS" : "FAIL"));
				if (!flag) {
					try {
						channelSftp.mkdir(remoteTempPath);
						flag = true;
					} catch (SftpException ignored) {
					}
					// log.info("make directory : " + remoteTempPath + "-->" +
					// (flag ? "SUCCESS" : "FAIL"));
				}
			}
		}
	}

}
