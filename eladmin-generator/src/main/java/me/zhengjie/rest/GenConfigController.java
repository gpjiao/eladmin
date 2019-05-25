package me.zhengjie.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import me.zhengjie.domain.GenConfig;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.GenConfigService;

/**
 * @author jie
 * @date 2019-01-14
 */
@RestController
@RequestMapping("api")
public class GenConfigController {

    @Autowired
    private GenConfigService genConfigService;
    
    /**
     * 查询生成器配置
     * 
     * @return
     */
    @GetMapping(value = "/genConfig")
    public ApiResponse<GenConfig> get()
    {
        return ApiResponse.code(HttpStatus.OK).body(genConfigService.find());
    }
    
    @PutMapping(value = "/genConfig")
    public ApiResponse<GenConfig> emailConfig(@Validated @RequestBody GenConfig genConfig)
    {
        return ApiResponse.code(HttpStatus.OK).body(genConfigService.update(genConfig));
    }
}
