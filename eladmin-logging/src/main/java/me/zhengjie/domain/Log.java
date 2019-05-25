package me.zhengjie.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author
 * @date 2018-11-24
 */
@Data
@TableName("sys_log")
@NoArgsConstructor
public class Log
{
    
    private Long id;
    
    /**
     * 操作用户
     */
    private String username;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 方法名
     */
    private String method;
    
    /**
     * 参数
     */
    private String params;
    
    /**
     * 日志类型
     */
    @TableField("log_type")
    private String logType;
    
    /**
     * 请求ip
     */
    @TableField("request_ip")
    private String requestIp;
    
    /**
     * 请求耗时
     */
    private Long time;
    
    /**
     * 异常详细
     */
    @TableField("exception_detail")
    private String exceptionDetail;
    
    /**
     * 创建日期
     */
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
    
    public Log(String logType, Long time)
    {
        this.logType = logType;
        this.time = time;
    }
}
