package me.zhengjie.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.Picture;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.PictureService;
import me.zhengjie.utils.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/09/20 14:13:32
 */
@RestController
@RequestMapping("/admin")
public class PictureController
{
    
    @Autowired
    private PictureService pictureService;
    
    @Log("查询图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_SELECT')")
    @GetMapping(value = "/pictures")
    public ApiResponse<List<Picture>> getRoles(Picture resources, Pageable pageable)
    {
        IPage<Picture> page =
            pictureService.findAll((long)pageable.getPageNumber(), (long)pageable.getPageSize(), resources);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
    
    /**
     * 
     * @title 上传图片
     * @description <功能详细描述>
     * @param file
     * @return
     */
    @Log("上传图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_UPLOAD')")
    @PostMapping(value = "/pictures")
    public ApiResponse<Map<String, Object>> upload(@RequestParam MultipartFile file)
    {
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        String userName = userDetails.getUsername();
        Picture picture = pictureService.upload(file, userName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errno", 0);
        map.put("id", picture.getId());
        map.put("data", new String[] {picture.getUrl()});
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.add.success").body(map);
    }
    
    /**
     * 
     * @title 删除图片
     * @description <功能详细描述>
     * @param id
     * @return
     */
    @Log("删除图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/pictures/{id}")
    public ApiResponse<?> delete(@PathVariable Long id)
    {
        pictureService.delete(pictureService.findById(id));
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
}
