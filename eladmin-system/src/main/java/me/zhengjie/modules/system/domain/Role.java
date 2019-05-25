package me.zhengjie.modules.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 角色
 * 
 * @author
 * @date 2018-11-22
 */
@Getter
@Setter
@TableName("sys_role")
public class Role implements Serializable
{
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @NotNull(groups = {Update.class})
    private Long id;
    
    @NotBlank
    private String name;
    
    private String remark;
    
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
    
    @JSONField(serialize = false)
    @TableField(exist = false)
    private Set<User> users;
    
    @TableField(exist = false)
    private Set<Permission> permissions;
    
    @TableField(exist = false)
    private Set<Menu> menus;
    
    @Override
    public String toString()
    {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + ", remark='" + remark + '\'' + ", createDateTime="
            + createTime + '}';
    }
    
    public interface Update
    {
    }
}
