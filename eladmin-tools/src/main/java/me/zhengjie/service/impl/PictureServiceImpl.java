package me.zhengjie.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.zhengjie.domain.Picture;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapper.PictureDao;
import me.zhengjie.service.PictureService;
import me.zhengjie.utils.ElAdminConstant;
import me.zhengjie.utils.FileUtil;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.HashMap;

/**
 * @author
 * @date 2018-12-27
 */
@Service(value = "pictureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PictureServiceImpl implements PictureService
{
    
    @Autowired
    private PictureDao pictureDao;
    
    public static final String SUCCESS = "success";
    
    public static final String CODE = "code";
    
    public static final String MSG = "msg";
    
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Picture upload(MultipartFile multipartFile, String username)
    {
        File file = FileUtil.toFile(multipartFile);
        
        HashMap<String, Object> paramMap = new HashMap<>();
        
        paramMap.put("smfile", file);
        String result = HttpUtil.post(ElAdminConstant.Url.SM_MS_URL, paramMap);
        
        JSONObject jsonObject = JSONUtil.parseObj(result);
        Picture picture = null;
        if (!jsonObject.get(CODE).toString().equals(SUCCESS))
        {
            throw new BadRequestException(jsonObject.get(MSG).toString());
        }
        // 转成实体类
        picture = JSON.parseObject(jsonObject.get("data").toString(), Picture.class);
        picture.setSize(picture.getSize());
        picture.setUsername(username);
        picture.setFilename(multipartFile.getOriginalFilename());
        pictureDao.insert(picture);
        // 删除临时文件
        FileUtil.deleteFile(file);
        return picture;
        
    }
    
    @Override
    public Picture findById(Long id)
    {
        return pictureDao.selectById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Picture picture)
    {
        try
        {
            String result = HttpUtil.get(picture.getDeleteUrl());
            pictureDao.deleteById(picture.getId());
        }
        catch (Exception e)
        {
            pictureDao.deleteById(picture.getId());
        }
    }
    
    @Override
    public IPage<Picture> findAll(Long pageNum, Long pageSize, Picture picture)
    {
        QueryWrapper<Picture> wrapper = new QueryWrapper<Picture>();
        if (StringUtils.isNotBlank(picture.getFilename()))
        {
            wrapper.like("filename", picture.getFilename());
        }
        return pictureDao.selectPage(new Page<Picture>(pageNum, pageSize), wrapper);
    }
}
