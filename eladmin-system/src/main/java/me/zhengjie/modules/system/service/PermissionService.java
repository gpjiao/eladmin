package me.zhengjie.modules.system.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import me.zhengjie.modules.system.domain.Permission;
import me.zhengjie.modules.system.service.dto.PermissionDTO;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2018-12-08
 */
@CacheConfig(cacheNames = "permission")
public interface PermissionService
{
    
    /**
     * get
     * 
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    PermissionDTO findById(long id);
    
    /**
     * create
     * 
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    PermissionDTO create(Permission resources);
    
    /**
     * update
     * 
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Permission resources);
    
    /**
     * delete
     * 
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
    
    /**
     * permission tree
     * 
     * @return
     */
    @Cacheable(key = "'tree'")
    List<Map<String, Object>> getPermissionTree(List<Permission> permissions);
    
    /**
     * findByPid
     * 
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<Permission> findByPid(long pid);
    
    /**
     * build Tree
     * 
     * @param permissionDTOS
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<PermissionDTO> buildTree(List<PermissionDTO> permissionDTOS);
    
    /**
     * 不分页
     * 
     * @param name
     * @return
     */
    @Cacheable(key = "'queryAll:'+#p0")
    public List<PermissionDTO> queryAll(String name);
}
