package me.zhengjie.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.domain.Log;

/**
 * @author jie
 * @date 2018-11-24
 */
public interface LogService {

    /**
     * 新增日志
     * @param joinPoint
     * @param log
     */
    @Async
    void save(ProceedingJoinPoint joinPoint, Log log);
    
    /**
     * 分页查询
     * 
     * @param pageNum
     * @param pageSize
     * @param log
     * @return
     */
    IPage<Log> findAll(int pageNum, int pageSize, Log log);
    
}
