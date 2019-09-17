package com.ben.mysql.jdbctemplate.utils.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * — <br>
 *
 * @author: Benjamin <br>
 * @date: 2019/9/11 <br>
 */
@Getter
@Setter
public class SysLog {
    private Integer id;
    private Integer userId;
    private String userName;
    private String operation;
    private Integer time;
    private String method;
}
