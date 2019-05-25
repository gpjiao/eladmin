package me.zhengjie.modules.system.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.service.dto.RoleDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @date 2018-12-03
 */
@CacheConfig(cacheNames = "role")
public interface RoleService
{
    
    /**
     * get
     * 
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    RoleDTO findById(long id);
    
    /**
     * create
     * 
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    RoleDTO create(Role resources);
    
    /**
     * update
     * 
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Role resources);
    
    /**
     * delete
     * 
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
    
    /**
     * role tree
     * 
     * @return
     */
    @Cacheable(key = "'tree'")
    List<Map<String, Object>> getRoleTree();
    
    /**
     * 用户角色列表
     * 
     * @param userId
     * @return
     */
    Set<Role> findByUser(Long userId);
    
    /**
     * updatePermission
     * 
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void updatePermission(Role resources);
    
    /**
     * updateMenu
     * 
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void updateMenu(Role resources);
    
    /**
     * 分页
     */
    @Cacheable(keyGenerator = "keyGenerator")
    IPage<Role> queryAll(int pageNum, int pageSize, String roleName);
}
