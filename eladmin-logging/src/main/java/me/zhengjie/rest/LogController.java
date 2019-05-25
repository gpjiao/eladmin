package me.zhengjie.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.domain.Log;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.LogService;

/**
 * @author
 * @date 2018-11-24
 */
@RestController
@RequestMapping("admin")
public class LogController
{
    @Autowired
    private LogService logService;
    
    @GetMapping(value = "/logs")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<Log>> getLogs(Log log, Pageable pageable)
    {
        log.setLogType("INFO");
        IPage<Log> page = logService.findAll(pageable.getPageNumber(), pageable.getPageSize(), log);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
    
    @GetMapping(value = "/logs/error")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<Log>> getErrorLogs(Log log, Pageable pageable)
    {
        log.setLogType("ERROR");
        IPage<Log> page = logService.findAll(pageable.getPageNumber(), pageable.getPageSize(), log);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
}
