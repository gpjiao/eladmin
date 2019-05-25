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
 * @date 2018-12-03
 */
@Getter
@Setter
@TableName("sys_permission")
public class Permission implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @NotNull(groups = {Update.class})
    private Long id;
    
    @NotBlank
    private String name;
    
    /**
     * 上级类目
     */
    @NotNull
    private Long pid;
    
    @NotBlank
    private String alias;
    
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
    
    @JSONField(serialize = false)
    @TableField(exist = false)
    private Set<Role> roles;
    
    @Override
    public String toString()
    {
        return "Permission{" + "id=" + id + ", name='" + name + '\'' + ", pid=" + pid + ", alias='" + alias + '\''
            + ", createTime=" + createTime + '}';
    }
    
    public interface Update
    {
    }
}
