package me.zhengjie.modules.monitor.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.zhengjie.http.ApiResponse;
import me.zhengjie.modules.monitor.service.VisitsService;
import me.zhengjie.utils.RequestHolder;

/**
 * @author
 * @date 2018-12-13
 */
@RestController
@RequestMapping("admin")
public class VisitsController
{
    
    @Autowired
    private VisitsService visitsService;
    
    @PostMapping(value = "/visits")
    public ApiResponse<?> create()
    {
        visitsService.count(RequestHolder.getHttpServletRequest());
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.add.success").build();
    }
    
    @GetMapping(value = "/visits")
    public ApiResponse<Map<String, Object>> get()
    {
        Map<String, Object> data = visitsService.get();
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.add.success").body(data);
    }
    
    @GetMapping(value = "/visits/chartData")
    public ApiResponse<Map<String, Object>> getChartData()
    {
        Map<String, Object> data = visitsService.getChartData();
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(data);
    }
}
