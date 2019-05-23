package com.legicycle.backend.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(basePackages = "com.legicycle.backend.daos.awsrds", entityManagerFactoryRef = "awsrdsEntityManager", transactionManagerRef = "awsrdsTransactionManager")
public class AwsrdsMultiConfig {
    @Autowired
    private Environment env;

    public AwsrdsMultiConfig() {
        super();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.second-datasource")
    public DataSource awsrdsDataSource() {
        return DataSourceBuilder.create().build();
    }

    //

    @Bean
    public LocalContainerEntityManagerFactoryBean awsrdsEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(awsrdsDataSource());
        em.setPackagesToScan("com.legicycle.backend.models.awsrds");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.second-datasource.ddl"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.second-datasource.dialect"));
        properties.put("hibernate.jdbc.lob.non_contextual_creation",
                env.getProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager awsrdsTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(awsrdsEntityManager().getObject());
        return transactionManager;
    }

}
