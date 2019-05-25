package me.zhengjie.modules.system.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.vo.MenuVo;
import me.zhengjie.modules.system.service.dto.MenuDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @date 2018-12-17
 */
@CacheConfig(cacheNames = "menu")
public interface MenuService
{
    
    /**
     * get
     * 
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    MenuDTO findById(long id);
    
    /**
     * create
     * 
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    MenuDTO create(Menu resources);
    
    /**
     * update
     * 
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Menu resources);
    
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
    List<Map<String, Object>> getMenuTree(List<Menu> menus);
    
    /**
     * findByPid
     * 
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<Menu> findByPid(long pid);
    
    /**
     * build Tree
     * 
     * @param menuDTOS
     * @return
     */
    List<MenuDTO> buildTree(List<MenuDTO> menuDTOS);
    
    /**
     * findByRoles
     * 
     * @param roles
     * @return
     */
    List<MenuDTO> findByRoles(Set<Role> roles);
    
    /**
     * buildMenus
     * 
     * @param byRoles
     * @return
     */
    List<MenuVo> buildMenus(List<MenuDTO> byRoles);
    
    /**
     * 根据名称模糊匹配全部<br>
     * 不分页
     * 
     * @param name
     * @return
     */
    @Cacheable(key = "'queryAll:'+#p0")
    List<MenuDTO> queryAll(String name);
}
