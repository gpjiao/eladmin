package me.zhengjie.modules.system.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import me.zhengjie.modules.system.domain.Role;

public interface RoleDao extends BaseMapper<Role>
{
    
    Set<Role> findByUser(@Param("userId") Long userId);
    
    /**
     * 删除角色权限
     * 
     * @param id
     * @return
     */
    @Delete(value = "delete from sys_roles_permissions where role_id = #{id}")
    int deletePermission(Long id);
    
    /**
     * 新增角色权限
     * 
     * @param roleId
     * @param permissionId
     * @return
     */
    @Insert(value = "insert into sys_roles_permissions(role_id, permission_id) values(#{roleId}, #{permissionId})")
    int insertPermission(Long roleId, Long permissionId);
    
    /**
     * 刪除角色菜单
     * 
     * @param id
     * @return
     */
    @Delete(value = "delete from sys_roles_menus where role_id = #{id}")
    int deleteMenu(Long id);
    
    /**
     * 新增角色菜单
     * 
     * @param roleId
     * @param menuId
     * @return
     */
    @Insert(value = "insert into sys_roles_menus(role_id, menu_id) values(#{roleId}, #{menuId})")
    int insertMenu(Long roleId, Long menuId);
    
}
