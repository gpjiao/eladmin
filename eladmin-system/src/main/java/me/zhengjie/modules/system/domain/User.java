package me.zhengjie.modules.system.domain;

import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * @author
 * @date 2018-11-22
 */
@Getter
@Setter
@TableName("sys_user")
public class User implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @NotNull(groups = Update.class)
    private Long id;
    
    @NotBlank
    private String username;
    
    private String avatar;
    
    @NotBlank
    @Pattern(regexp = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}", message = "格式错误")
    private String email;
    
    @NotNull
    private Boolean enabled;
    
    @JSONField(serialize = false)
    private String password;
    
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
    
    @TableField("last_password_reset_time")
    private Date lastPasswordResetTime;
    
    @TableField("org_id")
    private Long orgId;
    
    @TableField(exist = false)
    private Set<Role> roles;
    
    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", avatar='" + avatar + '\'' + ", email='"
            + email + '\'' + ", enabled=" + enabled + ", password='" + password + '\'' + ", createTime=" + createTime
            + ", lastPasswordResetTime=" + lastPasswordResetTime + '}';
    }
    
    public @interface Update
    {
    }
}