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

@MapperScan(value = "com.harmoni.auth.mapper")
@Configuration
public class DbAuthSQLConfig {

    @Primary
    @Bean(name = "authSQLDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.auth.mysql")
    public DataSource authSQLDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

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

    @Primary
    @Bean(name = "authSQLTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager (
            @Qualifier("authSQLDataSource") DataSource authSqlDatasource) {
        return new DataSourceTransactionManager(authSqlDatasource);
    }

}
