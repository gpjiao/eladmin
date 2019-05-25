package me.zhengjie.modules.system.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Permission;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.mapper.MenuDao;
import me.zhengjie.modules.system.mapper.PermissionDao;
import me.zhengjie.modules.system.mapper.RoleDao;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.dto.RoleDTO;
import me.zhengjie.modules.system.service.map.RoleMapper;

import java.util.*;

/**
 * @author
 * @date 2018-12-03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService
{
    
    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private PermissionDao permissionDao;
    
    @Autowired
    private MenuDao menuDao;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public RoleDTO findById(long id)
    {
        Role role = roleDao.selectById(id);
        return roleMapper.toDto(role);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO create(Role resources)
    {
        if (roleDao.selectCount(new QueryWrapper<Role>().eq("name", resources.getName())) > 0)
        {
            throw new EntityExistException(Role.class, "name", resources.getName());
        }
        roleDao.insert(resources);
        return roleMapper.toDto(resources);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources)
    {
        
        Role role = roleDao.selectById(resources.getId());
        
        Role role1 = roleDao.selectOne(new QueryWrapper<Role>().eq("name", resources.getName()));
        
        if (role1 != null && !role1.getId().equals(role.getId()))
        {
            throw new EntityExistException(Role.class, "name", resources.getName());
        }
        
        role.setName(resources.getName());
        role.setRemark(resources.getRemark());
        roleDao.updateById(role);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Role role)
    {
        if (null != role && CollectionUtils.isNotEmpty(role.getPermissions()))
        {
            roleDao.deletePermission(role.getId());
            for (Permission permission : role.getPermissions())
            {
                roleDao.insertPermission(role.getId(), permission.getId());
            }
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Role role)
    {
        if (null != role && CollectionUtils.isNotEmpty(role.getMenus()))
        {
            roleDao.deleteMenu(role.getId());
            for (Menu menu : role.getMenus())
            {
                roleDao.insertMenu(role.getId(), menu.getId());
            }
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id)
    {
        roleDao.deletePermission(id);
        roleDao.deleteMenu(id);
        roleDao.deleteById(id);
    }
    
    @Override
    public List<Map<String, Object>> getRoleTree()
    {
        List<Role> roleList = roleDao.selectList(new QueryWrapper<Role>());
        
        List<Map<String, Object>> list = new ArrayList<>();
        for (Role role : roleList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("id", role.getId());
            map.put("label", role.getName());
            list.add(map);
        }
        return list;
    }
    
    @Override
    public Set<Role> findByUser(Long userId)
    {
        return roleDao.findByUser(userId);
    }
    
    @Override
    public IPage<Role> queryAll(int pageNum, int pageSize, String roleName)
    {
        QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
        if (StringUtils.isNotBlank(roleName))
        {
            wrapper.like("name", roleName);
        }
        IPage<Role> page = roleDao.selectPage(new Page<Role>(pageNum, pageSize), wrapper);
        if (null != page && CollectionUtils.isNotEmpty(page.getRecords()))
        {
            List<Role> records = page.getRecords();
            Set<Menu> menus = null;
            for (Role item : records)
            {
                // 查询菜单
                menus = new LinkedHashSet<Menu>(menuDao.findByRole(item.getId()));
                item.setMenus(menus);
                // 查询权限
                item.setPermissions(permissionDao.findByRole(item.getId()));
            }
            page.setRecords(records);
        }
        return page;
    }
}
