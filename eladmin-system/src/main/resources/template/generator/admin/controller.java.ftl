package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

import me.zhengjie.apo.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.http.ApiResponse;

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName}
{
</#if>
    @Autowired
    private ${table.serviceName} ${table.serviceName};
	
	@Log("查询${entity}")
    @GetMapping(value = "/${table.entityPath}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<${entity}>> get${entity}s(${entity} entity, Pageable pageable)
	{
		IPage<${entity}> page = ${table.serviceName}.listPage(pageable.getPageNumber(), pageable.getPageSize(), entity);
		return new ApiResponse.Builder<List<${entity}>>(HttpStatus.OK).page(page).i18n("msg.operation.success").build();
    }

    @Log("新增${entity}")
    @PostMapping(value = "/${table.entityPath}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<${entity}> create(@Validated @RequestBody ${entity} resources)
	{
        if (resources.getId() != null)
		{
            throw new BadRequestException("A new ${entity} cannot already have an ID");
        }
        ${entity} entity = ${table.serviceName}.create(resources);
        return new ApiResponse.Builder<${entity}>(HttpStatus.CREATED).content(entity).i18n("msg.operation.success").build();
    }

    @Log("修改${entity}")
    @PutMapping(value = "/${table.entityPath}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<?> update(@Validated @RequestBody ${entity} resources)
	{
        if (resources.getId() == null)
		{
            throw new BadRequestException("${entity} ID Can not be empty");
        }
        ${table.serviceName}.updateById(resources);
		return new ApiResponse.Builder<>(HttpStatus.OK).i18n("msg.operation.success").build();
    }

    @Log("删除${entity}")
    @DeleteMapping(value = "/${table.entityPath}/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<?> delete(@PathVariable Long id)
	{
        ${table.serviceName}.removeById(id);
        return new ApiResponse.Builder<>(HttpStatus.OK).i18n("msg.operation.success").build();
    }

}
</#if>
