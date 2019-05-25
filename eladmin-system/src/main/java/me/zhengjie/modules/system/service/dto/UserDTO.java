package me.zhengjie.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author
 * @date 2018-11-23
 */
@Data
public class UserDTO implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long id;
    
    private String username;
    
    private String avatar;
    
    private String email;
    
    private Boolean enabled;
    
    @JSONField(serialize = false)
    private String password;
    
    private Timestamp createTime;
    
    private Date lastPasswordResetTime;
    
    @ApiModelProperty(hidden = true)
    private Set<RoleDTO> roles;
}
