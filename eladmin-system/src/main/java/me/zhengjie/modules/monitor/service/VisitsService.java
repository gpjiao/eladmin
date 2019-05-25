package me.zhengjie.modules.monitor.service;

import org.springframework.scheduling.annotation.Async;

import me.zhengjie.modules.monitor.domain.Visits;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @date 2018-12-13
 */
public interface VisitsService
{
    
    /**
     * 提供给定时任务，每天0点执行
     */
    Visits save();
    
    /**
     * 新增记录
     * 
     * @param request
     */
    @Async
    void count(HttpServletRequest request);
    
    /**
     * 获取数据
     * 
     * @return
     */
    Map<String, Object> get();
    
    /**
     * getChartData
     * 
     * @return
     */
    Map<String, Object> getChartData();
}
