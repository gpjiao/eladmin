package me.zhengjie.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 邮件配置类，数据存覆盖式存入数据存
 * 
 * @author
 * @date 2018-12-26
 */
@Data
@TableName("sys_email_config")
public class EmailConfig implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    /**
     * 邮件服务器SMTP地址
     */
    @NotBlank
    private String host;
    
    /**
     * 邮件服务器SMTP端口
     */
    @NotBlank
    private String port;
    
    /**
     * 发件者用户名，默认为发件人邮箱前缀
     */
    @NotBlank
    private String user;
    
    @NotBlank
    private String pass;
    
    /**
     * 收件人
     */
    @NotBlank
    @TableField("from_user")
    private String fromUser;
}
