package me.zhengjie.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import me.zhengjie.modules.system.domain.Menu;

public interface MenuDao extends BaseMapper<Menu>
{
    
    /**
     * 根据角色id查询菜单列表
     * 
     * @param id
     * @return
     */
    List<Menu> findByRole(@Param("roleId") Long roleId);
    
}
