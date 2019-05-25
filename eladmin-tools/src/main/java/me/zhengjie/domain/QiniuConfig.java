package me.zhengjie.domain;

import lombok.Data;
import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 七牛云对象存储配置类
 * 
 * @author
 * @date 2018-12-31
 */
@Data
@TableName("sys_qiniu_config")
public class QiniuConfig implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    /**
     * 一个账号最多拥有两对密钥(Access/Secret Key)
     */
    @NotBlank
    @TableField("access_key")
    private String accessKey;
    
    /**
     * 一个账号最多拥有两对密钥(Access/Secret Key)
     */
    @NotBlank
    @TableField("secret_key")
    private String secretKey;
    
    /**
     * 存储空间名称作为唯一的 Bucket 识别符
     */
    @NotBlank
    private String bucket;
    
    /**
     * Zone表示与机房的对应关系 华东 Zone.zone0() 华北 Zone.zone1() 华南 Zone.zone2() 北美 Zone.zoneNa0() 东南亚 Zone.zoneAs0()
     */
    @NotBlank
    private String zone;
    
    /**
     * 外链域名，可自定义，需在七牛云绑定
     */
    @NotBlank
    private String host;
    
    /**
     * 空间类型：公开/私有
     */
    private String type = "公开";
}
