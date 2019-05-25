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
 * @author
 * @date 2018-12-17
 */
@Getter
@Setter
@TableName("sys_menu")
public class Menu implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @NotNull(groups = {Update.class})
    private Long id;
    
    @NotBlank
    private String name;
    
    private Long sort;
    
    private String path;
    
    private String component;
    
    private String icon;
    
    /**
     * 上级菜单ID
     */
    private Long pid;
    
    /**
     * 是否为外链 true/false
     */
    @TableField("i_frame")
    private Boolean iFrame;
    
    @JSONField(serialize = false)
    @TableField(exist = false)
    private Set<Role> roles;
    
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
    
    public interface Update
    {
    }
}
