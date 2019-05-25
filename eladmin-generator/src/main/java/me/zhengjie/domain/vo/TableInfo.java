package me.zhengjie.domain.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表的数据信息
 * 
 * @author jie
 * @date 2019-01-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo
{
    /** 表名称 **/
    private String tableName;
    
    /** 说明 **/
    private String tableComment;
    
    /** 创建日期 **/
    private Timestamp createTime;
    
}
