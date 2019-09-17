package com.ben.mysql.jdbctemplate.utils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 配置jdbcTemplate <br>
 *
 * @author: Benjamin <br>
 * @date: 2019/9/11 <br>
 */
@Component
public class JdbcTemplateConfig {

    @Autowired
    DataSource testDataSource;

    @Primary
    @Bean("testJdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(testDataSource);
    }
}
