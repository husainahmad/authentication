package com.harmoni.auth.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Configuration class for setting up the MyBatis SQL session and transaction management
 * for the authentication module.
 * <p>
 * This configuration provides:
 * <ul>
 *     <li>A primary {@link DataSource} bean configured via application properties</li>
 *     <li>An associated {@link SqlSessionFactory} bean for MyBatis</li>
 *     <li>A {@link DataSourceTransactionManager} for transaction handling</li>
 * </ul>
 */
@MapperScan(value = "com.harmoni.auth.mapper")
@Configuration
public class DbAuthSQLConfig {

    /**
     * Primary datasource bean for the authentication module.
     * <p>Configuration is loaded from the property prefix: {@code spring.datasource.auth.mysql}</p>
     *
     * @return the configured {@link DataSource}
     */
    @Primary
    @Bean(name = "authSQLDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.auth.mysql")
    public DataSource authSQLDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Primary {@link SqlSessionFactory} bean that enables MyBatis to create SQL sessions
     * using the authentication datasource.
     *
     * @param authSQLDataSource   the primary datasource for auth
     * @param applicationContext  Spring application context (optional, can be used to load mappers/config)
     * @return the configured {@link SqlSessionFactory}
     * @throws Exception if the session factory cannot be created
     */
    @Primary
    @Bean(name = "authSqlSessionFactory")
    public SqlSessionFactory authSqlSessionFactory(
            @Qualifier("authSQLDataSource") DataSource authSQLDataSource,
            ApplicationContext applicationContext
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(authSQLDataSource);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * Primary transaction manager for handling SQL transactions in the authentication module.
     *
     * @param authSqlDatasource the auth module datasource
     * @return a {@link DataSourceTransactionManager}
     */
    @Primary
    @Bean(name = "authSQLTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager(
            @Qualifier("authSQLDataSource") DataSource authSqlDatasource) {
        return new DataSourceTransactionManager(authSqlDatasource);
    }
}
