package me.zhengjie.modules.system.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import me.zhengjie.modules.system.domain.Permission;

public interface PermissionDao extends BaseMapper<Permission>
{
    /**
     * 角色权限
     * 
     * @param roleId
     * @return
     */
    Set<Permission> findByRole(@Param("roleId") Long roleId);
    
}
