package me.zhengjie.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.vo.MenuMetaVo;
import me.zhengjie.modules.system.domain.vo.MenuVo;
import me.zhengjie.modules.system.mapper.MenuDao;
import me.zhengjie.modules.system.service.MenuService;
import me.zhengjie.modules.system.service.dto.MenuDTO;
import me.zhengjie.modules.system.service.map.MenuMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService
{
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private MenuDao menuDao;
    
    @Override
    public MenuDTO findById(long id)
    {
        Menu menu = menuDao.selectById(id);
        return menuMapper.toDto(menu);
    }
    
    @Override
    public List<MenuDTO> findByRoles(Set<Role> roles)
    {
        Set<Menu> menus = new LinkedHashSet<>();
        for (Role role : roles)
        {
            menus.addAll(menuDao.findByRole(role.getId()));
        }
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }
    
    @Override
    public MenuDTO create(Menu resources)
    {
        if (menuDao.selectOne(new QueryWrapper<Menu>().eq("name", resources.getName())) != null)
        {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }
        if (resources.getIFrame())
        {
            if (!(resources.getPath().toLowerCase().startsWith("http://")
                || resources.getPath().toLowerCase().startsWith("https://")))
            {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        int rows = menuDao.insert(resources);
        return menuMapper.toDto(resources);
    }
    
    @Override
    public void update(Menu resources)
    {
        if (resources.getIFrame())
        {
            if (!(resources.getPath().toLowerCase().startsWith("http://")
                || resources.getPath().toLowerCase().startsWith("https://")))
            {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        Menu menu = menuDao.selectById(resources.getId());
        Menu menu1 = menuDao.selectOne(new QueryWrapper<Menu>().eq("name", resources.getName()));
        
        if (menu1 != null && !menu1.getId().equals(menu.getId()))
        {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }
        menu.setName(resources.getName());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setSort(resources.getSort());
        menuDao.updateById(menu);
    }
    
    @Override
    public void delete(Long id)
    {
        // 删除子菜单
        menuDao.delete(new QueryWrapper<Menu>().eq("pid", id));
        menuDao.deleteById(id);
    }
    
    @Override
    public List<Map<String, Object>> getMenuTree(List<Menu> menus)
    {
        List<Map<String, Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
            if (menu != null)
            {
                List<Menu> menuList = menuDao.selectList(new QueryWrapper<Menu>().eq("pid", menu.getId()));
                Map<String, Object> map = new HashMap<>();
                map.put("id", menu.getId());
                map.put("label", menu.getName());
                if (menuList != null && menuList.size() != 0)
                {
                    map.put("children", getMenuTree(menuList));
                }
                list.add(map);
            }
        });
        return list;
    }
    
    @Override
    public List<Menu> findByPid(long pid)
    {
        return menuDao.selectList(new QueryWrapper<Menu>().eq("pid", pid));// menuRepository.findByPid(pid);
    }
    
    @Override
    public List<MenuDTO> buildTree(List<MenuDTO> menuDTOS)
    {
        List<MenuDTO> trees = new ArrayList<MenuDTO>();
        
        if (CollectionUtils.isNotEmpty(menuDTOS))
        {
            for (MenuDTO menuDTO : menuDTOS)
            {
                if ("0".equals(menuDTO.getPid().toString()))
                {
                    trees.add(menuDTO);
                }
                
                for (MenuDTO it : menuDTOS)
                {
                    if (it.getPid().equals(menuDTO.getId()))
                    {
                        if (menuDTO.getChildren() == null)
                        {
                            menuDTO.setChildren(new ArrayList<MenuDTO>());
                        }
                        menuDTO.getChildren().add(it);
                    }
                }
            }
        }
        return trees;
    }
    
    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDTOS)
    {
        List<MenuVo> list = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
            if (menuDTO != null)
            {
                List<MenuDTO> menuDTOList = menuDTO.getChildren();
                MenuVo menuVo = new MenuVo();
                menuVo.setName(menuDTO.getName());
                menuVo.setPath(menuDTO.getPath());
                
                // 如果不是外链
                if (!menuDTO.getIFrame())
                {
                    if (menuDTO.getPid().equals(0L))
                    {
                        // 一级目录需要加斜杠，不然访问 会跳转404页面
                        menuVo.setPath("/" + menuDTO.getPath());
                        menuVo
                            .setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                    }
                    else if (!StrUtil.isEmpty(menuDTO.getComponent()))
                    {
                        menuVo.setComponent(menuDTO.getComponent());
                    }
                }
                menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon()));
                if (menuDTOList != null && menuDTOList.size() != 0)
                {
                    menuVo.setAlwaysShow(true);
                    menuVo.setRedirect("noredirect");
                    menuVo.setChildren(buildMenus(menuDTOList));
                    // 处理是一级菜单并且没有子菜单的情况
                }
                else if (menuDTO.getPid().equals(0L))
                {
                    MenuVo menuVo1 = new MenuVo();
                    menuVo1.setMeta(menuVo.getMeta());
                    // 非外链
                    if (!menuDTO.getIFrame())
                    {
                        menuVo1.setPath("index");
                        menuVo1.setName(menuVo.getName());
                        menuVo1.setComponent(menuVo.getComponent());
                    }
                    else
                    {
                        menuVo1.setPath(menuDTO.getPath());
                    }
                    menuVo.setName(null);
                    menuVo.setMeta(null);
                    menuVo.setComponent("Layout");
                    List<MenuVo> list1 = new ArrayList<MenuVo>();
                    list1.add(menuVo1);
                    menuVo.setChildren(list1);
                }
                list.add(menuVo);
            }
        });
        return list;
    }
    
    @Override
    public List<MenuDTO> queryAll(String name)
    {
        QueryWrapper<Menu> wrapper = new QueryWrapper<Menu>();
        if (StringUtils.isNotBlank(name))
        {
            wrapper.like("name", name);
        }
        return menuMapper.toDto(menuDao.selectList(wrapper));
    }
}
