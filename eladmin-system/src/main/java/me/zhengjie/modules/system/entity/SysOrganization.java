package me.zhengjie.modules.system.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author auth
 * @since 2019-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysOrganization implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 机构id
     */
    private Long id;
    
    /**
     * 机构代码
     */
    private String code;
    
    /**
     * 机构名称
     */
    private String name;
    
    /**
     * 机构全称
     */
    private String fullName;
    
    /**
     * 负责人
     */
    private String leader;
    
    /**
     * 联系方式
     */
    private String contact;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 机构说明
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
}
