package me.zhengjie.modules.system.service.map;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.Permission;
import me.zhengjie.modules.system.service.dto.PermissionDTO;

/**
 * @author
 * @date 2018-11-23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission>
{
    
}
