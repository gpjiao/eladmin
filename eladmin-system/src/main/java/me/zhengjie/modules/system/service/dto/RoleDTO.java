package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author
 * @date 2018-11-23
 */
@Data
public class RoleDTO implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String name;
    
    private String remark;
    
    private Set<PermissionDTO> permissions;
    
    private Set<MenuDTO> menus;
    
    private Timestamp createTime;
}
