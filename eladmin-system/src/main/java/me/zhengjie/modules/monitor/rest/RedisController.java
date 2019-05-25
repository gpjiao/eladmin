package me.zhengjie.modules.monitor.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.aop.log.Log;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.modules.monitor.domain.vo.RedisVo;
import me.zhengjie.modules.monitor.service.RedisService;

/**
 * @author
 * @date 2018-12-10
 */
@RestController
@RequestMapping("admin")
public class RedisController
{
    
    @Autowired
    private RedisService redisService;
    
    @Log("查询Redis缓存")
    @GetMapping(value = "/redis")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_SELECT')")
    public ApiResponse<List<RedisVo>> getRedis(String key, Pageable pageable)
    {
        IPage<RedisVo> page = redisService.findByKey(key, pageable);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
    
    @Log("新增Redis缓存")
    @PostMapping(value = "/redis")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_CREATE')")
    public ApiResponse<?> create(@Validated @RequestBody RedisVo resources)
    {
        redisService.save(resources);
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.add.success").build();
    }
    
    @Log("修改Redis缓存")
    @PutMapping(value = "/redis")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_EDIT')")
    public ApiResponse<?> update(@Validated @RequestBody RedisVo resources)
    {
        redisService.save(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("删除Redis缓存")
    @DeleteMapping(value = "/redis")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ApiResponse<?> delete(@RequestBody RedisVo resources)
    {
        redisService.delete(resources.getKey());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
    
    @Log("清空Redis缓存")
    @DeleteMapping(value = "/redis/all")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ApiResponse<?> deleteAll()
    {
        redisService.flushdb();
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
}
