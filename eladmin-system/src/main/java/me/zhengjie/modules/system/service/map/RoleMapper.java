package me.zhengjie.modules.system.service.map;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import me.zhengjie.mapper.EntityMapper;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.service.dto.RoleDTO;

/**
 * @author
 * @date 2018-11-23
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class,
    MenuMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends EntityMapper<RoleDTO, Role>
{
    
}
