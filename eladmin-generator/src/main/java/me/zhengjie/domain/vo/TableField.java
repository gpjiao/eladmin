package me.zhengjie.domain.vo;

import lombok.Data;

@Data
public class TableField
{
    private boolean keyFlag;
    
    /** 表字段名称 **/
    private String columnName;
    
    /** 表字段类型 **/
    private String dataType;
    
    /** 表字段说明 **/
    private String columnComment;
    
    /** 表字段key类型 **/
    private String columnKey;
    
    /** 实体字段名称 **/
    private String propertyName;
    
    /** 实体字段类型 **/
    private String propertyType;
    
    /** 是否在列表显示 **/
    private String columnShow = "true";
    
    /** 是否根据该条件搜索 **/
    private String query = "false";
    
}
