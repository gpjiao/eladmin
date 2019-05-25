package me.zhengjie.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 代码生成配置
 * 
 * @author jie
 * @date 2019-01-03
 */
@Data
@TableName("sys_gen_config")
public class GenConfig
{
    
    private Long id;
    
    /** 包路径 **/
    private String pack;
    
    /** 模块名 **/
    @TableField("module_name")
    private String moduleName;
    
    /** 前端文件路径 **/
    private String path;
    
    /** 作者 **/
    private String author;
    
    /** 是否覆盖 **/
    private Boolean cover;
}
