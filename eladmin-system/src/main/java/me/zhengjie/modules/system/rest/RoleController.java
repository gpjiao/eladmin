package me.zhengjie.modules.system.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.dto.RoleDTO;

/**
 * @author
 * @date 2018-12-03
 */
@RestController
@RequestMapping("admin")
public class RoleController
{
    
    @Autowired
    private RoleService roleService;
    
    private static final String ENTITY_NAME = "role";
    
    @GetMapping(value = "/roles/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ApiResponse<RoleDTO> getRoles(@PathVariable Long id)
    {
        RoleDTO roleDTO = roleService.findById(id);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(roleDTO);
    }
    
    /**
     * 返回全部的角色，新增用户时下拉选择
     * 
     * @return
     */
    @GetMapping(value = "/roles/tree")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_SELECT')")
    public ApiResponse<List<Map<String, Object>>> getRoleTree()
    {
        List<Map<String, Object>> roleTree = roleService.getRoleTree();
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(roleTree);
    }
    
    @Log("查询角色")
    @GetMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ApiResponse<List<Role>> getRoles(@RequestParam(required = false) String name, Pageable pageable)
    {
        IPage<Role> page = roleService.queryAll(pageable.getPageNumber(), pageable.getPageSize(), name);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
    
    @Log("新增角色")
    @PostMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_CREATE')")
    public ApiResponse<?> create(@Validated @RequestBody Role resources)
    {
        if (resources.getId() != null)
        {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        roleService.create(resources);
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.add.success").build();
    }
    
    @Log("修改角色")
    @PutMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ApiResponse<?> update(@Validated(Role.Update.class) @RequestBody Role resources)
    {
        roleService.update(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("修改角色权限")
    @PutMapping(value = "/roles/permission")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ApiResponse<?> updatePermission(@RequestBody Role resources)
    {
        roleService.updatePermission(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("修改角色菜单")
    @PutMapping(value = "/roles/menu")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ApiResponse<?> updateMenu(@RequestBody Role resources)
    {
        roleService.updateMenu(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("删除角色")
    @DeleteMapping(value = "/roles/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_DELETE')")
    public ApiResponse<?> delete(@PathVariable Long id)
    {
        roleService.delete(id);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
}
