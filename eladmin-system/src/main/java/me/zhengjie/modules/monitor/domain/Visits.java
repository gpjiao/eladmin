package me.zhengjie.modules.monitor.domain;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * pv 与 ip 统计
 *
 * @author
 * @date 2018-12-13
 */
@Data
@TableName("sys_visits")
public class Visits
{
    private Long id;
    
    private String date;
    
    @TableField("pv_counts")
    private Long pvCounts;
    
    @TableField("ip_counts")
    private Long ipCounts;
    
    @TableField("week_day")
    private String weekDay;
    
    @TableField("create_time")
    private Timestamp createTime = Timestamp.valueOf(LocalDateTime.now());
}
