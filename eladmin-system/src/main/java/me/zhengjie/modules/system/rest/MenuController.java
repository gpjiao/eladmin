package me.zhengjie.modules.system.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.MenuVo;
import me.zhengjie.modules.system.service.MenuService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.MenuDTO;
import me.zhengjie.utils.SecurityContextHolder;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2018-12-03
 */
@RestController
@RequestMapping("admin")
public class MenuController
{
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    private static final String ENTITY_NAME = "menu";
    
    /**
     * 构建前端路由所需要的菜单
     * 
     * @return
     */
    @GetMapping(value = "/menus/build")
    public ApiResponse<List<MenuVo>> buildMenus()
    {
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        User user = userService.findByName(userDetails.getUsername());
        List<MenuDTO> menuDTOList = menuService.findByRoles(roleService.findByUser(user.getId()));
        List<MenuDTO> menuDTOTree = menuService.buildTree(menuDTOList);
        List<MenuVo> menuVoList = menuService.buildMenus(menuDTOTree);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(menuVoList);
    }
    
    /**
     * 返回全部的菜单
     * 
     * @return
     */
    @GetMapping(value = "/menus/tree")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT','ROLES_SELECT','ROLES_ALL')")
    public ApiResponse<List<Map<String, Object>>> getMenuTree()
    {
        List<Map<String, Object>> menuTree = menuService.getMenuTree(menuService.findByPid(0L));
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(menuTree);
    }
    
    @Log("查询菜单")
    @GetMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public ApiResponse<List<MenuDTO>> getMenus(@RequestParam(required = false) String name)
    {
        List<MenuDTO> menuDTOList = menuService.queryAll(name);
        List<MenuDTO> menuTree = menuService.buildTree(menuDTOList);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(menuTree);
    }
    
    @Log("新增菜单")
    @PostMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE')")
    public ApiResponse<MenuDTO> create(@Validated @RequestBody Menu resources)
    {
        if (resources.getId() != null)
        {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        MenuDTO menuDTO = menuService.create(resources);
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.add.success").body(menuDTO);
    }
    
    @Log("修改菜单")
    @PutMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ApiResponse<?> update(@Validated(Menu.Update.class) @RequestBody Menu resources)
    {
        menuService.update(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("删除菜单")
    @DeleteMapping(value = "/menus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_DELETE')")
    public ApiResponse<?> delete(@PathVariable Long id)
    {
        menuService.delete(id);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
}
