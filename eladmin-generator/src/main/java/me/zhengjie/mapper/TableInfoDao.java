package me.zhengjie.mapper;


import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.domain.vo.TableInfo;

public interface TableInfoDao
{
    /**
     * 
     * @title 分页查询
     * @description <功能详细描述>
     * @date 2019年3月14日 下午5:04:57
     * @param page
     * @param name
     * @return
     */
    // @Select("select table_name tableName, table_comment tableComment, create_time createTime from
    // information_schema.tables where table_schema = (select database())")
    IPage<TableInfo> list(@Param("page") Page<TableInfo> page, @Param("tableName") String tableName);
    
}
