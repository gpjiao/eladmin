package me.zhengjie.domain;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 上传成功后，存储结果
 * 
 * @author
 * @date 2018-12-31
 */
@Data
@TableName("sys_qiniu_content")
public class QiniuContent implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    /**
     * 文件名，如qiniu.jpg
     */
    @TableField("file_key")
    private String fileKey;
    
    /**
     * 空间名
     */
    private String bucket;
    
    /**
     * 大小
     */
    private Long size;
    
    /**
     * 文件地址
     */
    private String url;
    
    /**
     * 空间类型：公开/私有
     */
    private String type = "公开";
    
    /**
     * 更新时间
     */
    @TableField(value = "update_time", update = "now()")
    private Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());
}
