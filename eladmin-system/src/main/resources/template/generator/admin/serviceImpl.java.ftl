package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}>
    implements ${table.serviceName}
{
    @Override
    public ${entity} create(${entity} entity)
    {
        super.save(entity);
        return entity;
    }
    
    @Override
    public IPage<${entity}> listPage(int pageNum, int pageSize, ${entity} entity)
    {
        return super.page(new Page<${entity}>(pageNum, pageSize),
            new QueryWrapper<${entity}>(entity));
    }

}
</#if>
