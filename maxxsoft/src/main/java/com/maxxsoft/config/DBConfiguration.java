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
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import liquibase.integration.spring.SpringLiquibase;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = false)
@EnableJpaRepositories(entityManagerFactoryRef = "maxxsoftEntityManagerFactory", transactionManagerRef = "maxxxsoftTransactionManager", basePackages = "com.maxxsoft.microServices")
public class DBConfiguration {

	@Primary
	@Bean(name = "maxxsoftDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "maxxsoftEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean maxdevEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("maxxsoftDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.maxxsoft.microServices").persistenceUnit("spring").build();
	}

	@Primary
	@Bean(name = "maxxxsoftTransactionManager")
	public PlatformTransactionManager maxdevTransactionManager(
			@Qualifier("maxxsoftEntityManagerFactory") EntityManagerFactory maxdevEntityManagerFactory) {
		return new JpaTransactionManager(maxdevEntityManagerFactory);
	}

	@Bean
	public LiquibaseProperties liquibaseProperties() {
		return new LiquibaseProperties();
	}

	@Bean
	@DependsOn(value = "maxxsoftEntityManagerFactory")
	public SpringLiquibase liquibase() {
		LiquibaseProperties liquibaseProperties = liquibaseProperties();
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(liquibaseProperties.getChangeLog());
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDataSource(dataSource());
		liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		liquibase.setShouldRun(true);
		liquibase.setLabels(liquibaseProperties.getLabels());
		liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
		return liquibase;
	}
}