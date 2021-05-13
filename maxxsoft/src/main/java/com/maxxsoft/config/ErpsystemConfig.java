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
package com.maxxsoft.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Configuration
@EnableTransactionManagement(proxyTargetClass = false)
@EnableJpaRepositories(entityManagerFactoryRef = "erpsystemEntityManagerFactory", transactionManagerRef = "erpsystemTransactionManager", basePackages = "com.maxxsoft.erpMicroServices")
public class ErpsystemConfig {

	@Bean(name = "erpsystemDataSource")
	@ConfigurationProperties(prefix = "db2.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "erpsystemEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean erpsystemEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("erpsystemDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.maxxsoft.erpMicroServices").persistenceUnit("db2").build();
	}

	@Bean(name = "erpsystemTransactionManager")
	public PlatformTransactionManager erpsystemTransactionManager(
			@Qualifier("erpsystemEntityManagerFactory") EntityManagerFactory erpsystemEntityManagerFactory) {
		return new JpaTransactionManager(erpsystemEntityManagerFactory);
	}

}
