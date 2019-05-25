package me.zhengjie.modules.system.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.modules.system.domain.Permission;
import me.zhengjie.modules.system.service.PermissionService;
import me.zhengjie.modules.system.service.dto.PermissionDTO;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2018-12-03
 */
@RestController
@RequestMapping("admin")
public class PermissionController
{
    
    @Autowired
    private PermissionService permissionService;
    
    private static final String ENTITY_NAME = "permission";
    
    /**
     * 返回全部的权限，新增角色时下拉选择
     * 
     * @return
     */
    @GetMapping(value = "/permissions/tree")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_SELECT','ROLES_SELECT','ROLES_ALL')")
    public ApiResponse<List<Map<String, Object>>> getPermissionTree()
    {
        List<Map<String, Object>> permissionTree = permissionService.getPermissionTree(permissionService.findByPid(0L));
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(permissionTree);
    }
    
    @Log("查询权限")
    @GetMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_SELECT')")
    public ApiResponse<List<PermissionDTO>> getPermissions(@RequestParam(required = false) String name)
    {
        List<PermissionDTO> permissionDTOS = permissionService.queryAll(name);
        List<PermissionDTO> permissions = permissionService.buildTree(permissionDTOS);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(permissions);
    }
    
    @Log("新增权限")
    @PostMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_CREATE')")
    public ApiResponse<?> create(@Validated @RequestBody Permission resources)
    {
        if (resources.getId() != null)
        {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        permissionService.create(resources);
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.add.success").build();
    }
    
    @Log("修改权限")
    @PutMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_EDIT')")
    public ApiResponse<?> update(@Validated(Permission.Update.class) @RequestBody Permission resources)
    {
        permissionService.update(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("删除权限")
    @DeleteMapping(value = "/permissions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_DELETE')")
    public ApiResponse<?> delete(@PathVariable Long id)
    {
        permissionService.delete(id);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
}
