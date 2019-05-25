package ${package.Service};

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}>
{
    /**
     * 
     * @description <描述>
     * @param entity
     * @return
     */
    ${entity} create(${entity} entity);
    
    /**
     * 
     * @description <描述>
     * @param pageNum
     * @param pageSize
     * @param entity
     * @return
     */
    IPage<${entity}> listPage(int pageNum, int pageSize, ${entity} entity);

}
</#if>
