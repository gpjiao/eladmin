package me.zhengjie.domain;

import lombok.Data;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * sm.ms图床
 *
 * @author
 * @date 2018-12-27
 */
@Data
@TableName("sys_picture")
public class Picture implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String filename;
    
    private String url;
    
    private Long size;
    
    private Integer height;
    
    private Integer width;
    
    /**
     * delete URl
     */
    @JSONField(serialize = false)
    @TableField("delete_url")
    private String deleteUrl;
    
    private String username;
    
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
    
    @Override
    public String toString()
    {
        return "Picture{" + "filename='" + filename + '\'' + '}';
    }
}
