package me.zhengjie.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.zhengjie.domain.vo.TableField;

public interface TableFieldDao
{
    /**
     * 
     * @title 表字段列表
     * @description <功能详细描述>
     * @date 2019年3月15日 上午9:31:14
     * @param tableName
     * @return
     */
    List<TableField> list(@Param("tableName") String tableName);
}
