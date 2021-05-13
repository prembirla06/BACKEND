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
package com.maxxsoft.microServices.articleService.exportJob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Set;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.maxxsoft.microServices.magentoService.model.MagentoCustomAttributeValue;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class CommonExportConfig {

	public static int bearbeitungszeitSpedition = 7;
	public static int bearbeitungszeitPaket = 2;

	public static int lieferzeitAufschlag = 0;

	@Value("${export.urlWebsite}")
	public String urlWebsite;
	@Value("${export.mediaWebsite}")
	public String mediaURL;

	@Value("${ftp.host}")
	public String ftpHost;
	@Value("${ftp.username}")
	public String ftpUsername;
	@Value("${ftp.password}")
	public String ftpPassword;
	@Value("${ftp.port}")
	public int ftpPort;

	public FTPClient ftpClient;
	public int returnCode;

	@Value("${app.magento.url}")
	public String magentoUrl;
	@Value("${app.magento.password}")
	public String password;
	@Value("${app.magento.user}")
	public String user;
	@Value("${export.output.dir}")
	public String outputDir;
	@Value("${export.template.dir}")
	public String templateDir;

	FTPClient ftp = null;
	byte pic[] = null;

	public String categoryPath;
	public String lastCategory;
	public String produktweltString;
	public String kategorie;
	public String unterkategorie;
	public String wohnwelt;
	public String stilwelt;
	public String farbe1;
	public String farbe2;
	public String trendfarbe;
	public String hoehe;
	public String tiefe;
	public String besonderheit;
	public String breite;
	public String urlProduct;
	public String gtin;

	public Configuration getConfiguration() {

		try {
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
			cfg.setDirectoryForTemplateLoading(new File(templateDir));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			return cfg;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Connects to a remote FTP server
	 */

	public void connect() throws SocketException, IOException {
		ftpClient = new FTPClient();
		ftpClient.connect(ftpHost, ftpPort);
		ftpClient.enterLocalPassiveMode();
		returnCode = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(returnCode)) {
			throw new IOException("Could not connect");
		}
		boolean loggedIn = ftpClient.login(ftpUsername, ftpPassword);
		if (!loggedIn) {
			throw new IOException("Could not login");
		}
		log.info("JSCH: Connected and logged in.");
	}
	
	public void connect(String ftpHost, int ftpPort, String ftpUsername, String ftpPassword) throws SocketException, IOException {
		ftpClient = new FTPClient();
		ftpClient.connect(ftpHost, ftpPort);
		ftpClient.enterLocalPassiveMode();
		returnCode = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(returnCode)) {
			throw new IOException("Could not connect");
		}
		boolean loggedIn = ftpClient.login(ftpUsername, ftpPassword);
		if (!loggedIn) {
			throw new IOException("Could not login");
		}
		log.info("CommonExportConig.java/JSCH: Connected and logged in.");
	}

	
	public ChannelSftp setupJsch() throws JSchException {
	    JSch jsch = new JSch();
	    Session jschSession = jsch.getSession(ftpUsername, ftpHost, ftpPort);
	    java.util.Properties config = new java.util.Properties();
	    config.put("StrictHostKeyChecking", "no");
	    jschSession.setConfig(config);
	    jschSession.setPassword(ftpPassword);
	    jschSession.connect();
	    return (ChannelSftp) jschSession.openChannel("sftp");
	}
	
	public boolean uploadSftpFromPath(String localFile, String sftpFile) {
	    ChannelSftp channelSftp = null;
	    FileInputStream localFileInputStream = null;
	    try {
	      channelSftp = setupJsch();
	    } catch (JSchException e) {
	    	log.error("CommonExportConig.java: " + e.getMessage());
	    }
	    try {
	      channelSftp.connect();
	    } catch (JSchException e) {
	    	log.error("CommonExportConig.java: " + e.getMessage());
	    }
	    try{
	    	localFileInputStream = new FileInputStream(localFile);
	      channelSftp.rm(sftpFile);
	      channelSftp.put(localFileInputStream, sftpFile, ChannelSftp.OVERWRITE);
	      log.info("CommonExportConig.java/JSCH: Upload Complete");
	    } catch (SftpException | FileNotFoundException e) {
	    	log.error("CommonExportConig.java: " + e.getMessage());
	    }
	    channelSftp.exit();
	    return true;
	  }
	
	
	/**
	 * Logs out and disconnects from the server
	 */
	public void logout() throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
			log.info("CommonExportConig.java/JSCH: Logged out");
		}
	}

	public boolean isBetween(int x, int lower, int upper) {
		return lower <= x && x <= upper;
	}

	public void setCustomAttributes(Set<MagentoCustomAttributeValue> magentoCustomAttributes) {
		categoryPath = "";
		lastCategory = "";
		produktweltString = "";
		kategorie = "";
		unterkategorie = "";
		wohnwelt = "";
		stilwelt = "";
		farbe1 = "";
		farbe2 = "";
		trendfarbe = "";
		hoehe = "";
		tiefe = "";
		besonderheit = "";
		breite = "";
		urlProduct = "";
		gtin = "";

		magentoCustomAttributes.forEach(magentoCustomAttribute -> {
			String valueName = magentoCustomAttribute.getAttributeValueName();
			String value = magentoCustomAttribute.getAttributeValue();
			if (magentoCustomAttribute.getAttributeName().equals("ms_product_world")) {
				produktweltString = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_category")) {
				kategorie = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_sub_category")) {
				unterkategorie = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_living_world")) {
				wohnwelt = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_style_world")) {
				stilwelt = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_color_world")) {
				farbe1 = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_second_color")) {
				farbe2 = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_trend_color")) {
				trendfarbe = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_width")) {
				breite = value;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_height")) {
				hoehe = value;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_depth")) {
				tiefe = value;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_feature")) {
				besonderheit = valueName;
			}
			if (magentoCustomAttribute.getAttributeName().equals("url_key")) {
				urlProduct = value;
			}
			if (magentoCustomAttribute.getAttributeName().equals("ms_googleshopping_gtin")) {
				gtin = value;
			}

			}
		);
		if (!produktweltString.isEmpty() && !produktweltString.equals(" ")  && !kategorie.isEmpty() && !kategorie.equals(" ") && !unterkategorie.isEmpty()  && !unterkategorie.equals(" ")) {
			categoryPath = produktweltString + " > " + kategorie + " > " + unterkategorie;
			lastCategory = unterkategorie;
		} else if (!produktweltString.isEmpty() && !produktweltString.equals(" ") && !kategorie.isEmpty() && !kategorie.equals(" ")) {
			categoryPath = produktweltString + " > " + kategorie;
			lastCategory = kategorie;
		} else if (!produktweltString.isEmpty() && !produktweltString.equals(" ")) {
			categoryPath = produktweltString;
			lastCategory = produktweltString;
		}
		
	}
}
