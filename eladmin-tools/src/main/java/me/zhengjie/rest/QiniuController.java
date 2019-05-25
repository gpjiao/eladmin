package me.zhengjie.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.QiniuConfig;
import me.zhengjie.domain.QiniuContent;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.QiNiuService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件
 * 
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@RestController
@RequestMapping("admin")
public class QiniuController
{
    @Autowired
    private QiNiuService qiniuService;
    
    @GetMapping(value = "/qiniuConfig")
    public ApiResponse<QiniuConfig> get()
    {
        QiniuConfig qiniuConfig = qiniuService.find();
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(qiniuConfig);
    }
    
    @Log("配置七牛云存储")
    @PutMapping(value = "/qiniuConfig")
    public ApiResponse<?> qiniuConfig(@Validated @RequestBody QiniuConfig qiniuConfig)
    {
        qiniuService.update(qiniuConfig);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
    
    @Log("查询文件")
    @GetMapping(value = "/qiniuContent")
    public ApiResponse<List<QiniuContent>> getRoles(QiniuContent resources, Pageable pageable)
    {
        IPage<QiniuContent> page = qiniuService.queryAll(pageable.getPageNumber(), pageable.getPageSize(), resources);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
    
    /**
     * 上传文件到七牛云
     * 
     * @param file
     * @return
     */
    @Log("上传文件")
    @PostMapping(value = "/qiniuContent")
    public ApiResponse<Map<String, Object>> upload(@RequestParam MultipartFile file)
    {
        QiniuContent qiniuContent = qiniuService.upload(file, qiniuService.find());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", qiniuContent.getId());
        map.put("errno", 0);
        map.put("data", new String[] {qiniuContent.getUrl()});
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.add.success").body(map);
    }
    
    /**
     * 同步七牛云数据到数据库
     * 
     * @return
     */
    @Log("同步七牛云数据")
    @PostMapping(value = "/qiniuContent/synchronize")
    public ApiResponse<?> synchronize()
    {
        qiniuService.synchronize(qiniuService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
    
    /**
     * 下载七牛云文件
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @Log("下载文件")
    @GetMapping(value = "/qiniuContent/download/{id}")
    public ApiResponse<String> download(@PathVariable Long id)
    {
        String url = qiniuService.download(qiniuService.findByContentId(id), qiniuService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(url);
    }
    
    /**
     * 删除七牛云文件
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @Log("删除文件")
    @DeleteMapping(value = "/qiniuContent/{id}")
    public ApiResponse<?> delete(@PathVariable Long id)
    {
        qiniuService.delete(qiniuService.findByContentId(id), qiniuService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
    
}
