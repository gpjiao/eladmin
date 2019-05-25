package me.zhengjie.modules.system.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.Permission;
import me.zhengjie.modules.system.mapper.PermissionDao;
import me.zhengjie.modules.system.service.PermissionService;
import me.zhengjie.modules.system.service.dto.PermissionDTO;
import me.zhengjie.modules.system.service.map.PermissionMapper;

import java.util.*;

/**
 * @author
 * @date 2018-12-03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService
{
    @Autowired
    private PermissionDao permissionDao;
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Override
    public PermissionDTO findById(long id)
    {
        Permission permission = permissionDao.selectById(id);
        return permissionMapper.toDto(permission);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO create(Permission resources)
    {
        if (permissionDao.selectCount(new QueryWrapper<Permission>().eq("name", resources.getName())) > 0)
        {
            throw new EntityExistException(Permission.class, "name", resources.getName());
        }
        permissionDao.insert(resources);
        return permissionMapper.toDto(resources);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Permission resources)
    {
        Permission permission = permissionDao.selectById(resources.getId());
        
        Permission permission1 =
            permissionDao.selectOne(new QueryWrapper<Permission>().eq("name", resources.getName()));
        
        if (permission1 != null && !permission1.getId().equals(permission.getId()))
        {
            throw new EntityExistException(Permission.class, "name", resources.getName());
        }
        
        permission.setName(resources.getName());
        permission.setAlias(resources.getAlias());
        permission.setPid(resources.getPid());
        permissionDao.updateById(permission);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id)
    {
        permissionDao.delete(new QueryWrapper<Permission>().eq("pid", id));
        permissionDao.deleteById(id);
    }
    
    @Override
    public List<Map<String, Object>> getPermissionTree(List<Permission> permissions)
    {
        List<Map<String, Object>> list = new LinkedList<>();
        permissions.forEach(permission -> {
            if (permission != null)
            {
                List<Permission> permissionList =
                    permissionDao.selectList(new QueryWrapper<Permission>().eq("pid", permission.getId()));
                Map<String, Object> map = new HashMap<>();
                map.put("id", permission.getId());
                map.put("label", permission.getAlias());
                if (permissionList != null && permissionList.size() != 0)
                {
                    map.put("children", getPermissionTree(permissionList));
                }
                list.add(map);
            }
        });
        return list;
    }
    
    @Override
    public List<Permission> findByPid(long pid)
    {
        return permissionDao.selectList(new QueryWrapper<Permission>().eq("pid", pid));
    }
    
    @Override
    public List<PermissionDTO> buildTree(List<PermissionDTO> permissionDTOS)
    {
        List<PermissionDTO> trees = new ArrayList<PermissionDTO>();
        
        if (CollectionUtils.isNotEmpty(permissionDTOS))
        {
            for (PermissionDTO permissionDTO : permissionDTOS)
            {
                if ("0".equals(permissionDTO.getPid().toString()))
                {
                    trees.add(permissionDTO);
                }
                
                for (PermissionDTO it : permissionDTOS)
                {
                    if (it.getPid().equals(permissionDTO.getId()))
                    {
                        if (permissionDTO.getChildren() == null)
                        {
                            permissionDTO.setChildren(new ArrayList<PermissionDTO>());
                        }
                        permissionDTO.getChildren().add(it);
                    }
                }
            }
        }
        return trees;
    }
    
    @Override
    public List<PermissionDTO> queryAll(String name)
    {
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        if (StringUtils.isNotBlank(name))
        {
            wrapper.like("name", name);
        }
        return permissionMapper.toDto(permissionDao.selectList(wrapper));
    }
}
