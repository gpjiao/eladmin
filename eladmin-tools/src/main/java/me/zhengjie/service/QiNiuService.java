package me.zhengjie.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.domain.QiniuConfig;
import me.zhengjie.domain.QiniuContent;

/**
 * @author
 * @date 2018-12-31
 */
@CacheConfig(cacheNames = "qiniu")
public interface QiNiuService
{
    
    /**
     * 查配置
     * 
     * @return
     */
    @Cacheable(key = "'1'")
    QiniuConfig find();
    
    /**
     * 修改配置
     * 
     * @param qiniuConfig
     * @return
     */
    @CachePut(key = "'1'")
    QiniuConfig update(QiniuConfig qiniuConfig);
    
    /**
     * 上传文件
     * 
     * @param file
     * @param qiniuConfig
     */
    @CacheEvict(allEntries = true)
    QiniuContent upload(MultipartFile file, QiniuConfig qiniuConfig);
    
    /**
     * 查询文件
     * 
     * @param id
     * @return
     */
    @Cacheable(key = "'content:'+#p0")
    QiniuContent findByContentId(Long id);
    
    /**
     * 下载文件
     * 
     * @param content
     * @param config
     * @return
     */
    String download(QiniuContent content, QiniuConfig config);
    
    /**
     * 删除文件
     * 
     * @param content
     * @param config
     * @return
     */
    @CacheEvict(allEntries = true)
    void delete(QiniuContent content, QiniuConfig config);
    
    /**
     * 同步数据
     * 
     * @param config
     */
    @CacheEvict(allEntries = true)
    void synchronize(QiniuConfig config);
    
    @Cacheable(keyGenerator = "keyGenerator")
    public IPage<QiniuContent> queryAll(int pageNum, int pageSize, QiniuContent qiniuContent);
}
