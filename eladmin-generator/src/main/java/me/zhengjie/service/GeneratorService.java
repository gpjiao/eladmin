package me.zhengjie.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.domain.GenConfig;
import me.zhengjie.domain.vo.TableField;
import me.zhengjie.domain.vo.TableInfo;

/**
 * @author jie
 * @date 2019-01-02
 */
public interface GeneratorService
{
    
    /**
     * 
     * @title 查询数据库表
     * @description <功能详细描述>
     * @date 2019年3月14日 下午4:08:41
     * @param name
     * @return
     */
    // List<TableInfoDTO> listTables(String name);
    
    /**
     * 
     * @title 查询数据库表
     * @description <功能详细描述>
     * @date 2019年3月14日 下午5:13:16
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    IPage<TableInfo> listTables(Integer pageNum, Integer pageSize, String name);
    
    /**
     * 
     * @title 表字段列表
     * @description <功能详细描述>
     * @date 2019年3月15日 上午9:29:52
     * @param tableName
     * @return
     */
    List<TableField> listFields(String tableName);
    
    /**
     * 生成代码
     * 
     * @param columnInfos
     * @param genConfig
     * @param tableName
     */
    void generator(List<TableField> tableFields, GenConfig genConfig, String tableName);
}
