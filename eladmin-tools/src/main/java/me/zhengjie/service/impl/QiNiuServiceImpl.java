package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import me.zhengjie.domain.QiniuConfig;
import me.zhengjie.domain.QiniuContent;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapper.QiniuConfigDao;
import me.zhengjie.mapper.QiniuContentDao;
import me.zhengjie.service.QiNiuService;
import me.zhengjie.util.QiNiuUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author
 * @date 2018-12-31
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QiNiuServiceImpl implements QiNiuService
{
    @Autowired
    private QiniuConfigDao qiniuConfigDao;
    
    @Autowired
    private QiniuContentDao qiniuContentDao;
    
    @Value("${qiniu.max-size}")
    private Long maxSize;
    
    private final String TYPE = "公开";
    
    @Override
    public QiniuConfig find()
    {
        QiniuConfig qiniuConfig = qiniuConfigDao.selectById(1l);
        return null == qiniuConfig ? new QiniuConfig() : qiniuConfig;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public QiniuConfig update(QiniuConfig qiniuConfig)
    {
        if (!(qiniuConfig.getHost().toLowerCase().startsWith("http://")
            || qiniuConfig.getHost().toLowerCase().startsWith("https://")))
        {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        QiniuConfig existQiniuConfig = qiniuConfigDao.selectById(1l);
        if (null == existQiniuConfig)
        {
            qiniuConfig.setId(1L);
            qiniuConfigDao.insert(qiniuConfig);
        }
        else
        {
            qiniuConfig.setId(1L);
            qiniuConfigDao.updateById(qiniuConfig);
        }
        return qiniuConfig;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public QiniuContent upload(MultipartFile file, QiniuConfig qiniuConfig)
    {
        
        Long size = maxSize * 1024 * 1024;
        if (file.getSize() > size)
        {
            throw new BadRequestException("文件超出规定大小");
        }
        if (qiniuConfig.getId() == null)
        {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        /**
         * 构造一个带指定Zone对象的配置类
         */
        Configuration cfg = QiNiuUtil.getConfiguration(qiniuConfig.getZone());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try
        {
            Response response =
                uploadManager.put(file.getBytes(), QiNiuUtil.getKey(file.getOriginalFilename()), upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            // 存入数据库
            QiniuContent qiniuContent = new QiniuContent();
            qiniuContent.setBucket(qiniuConfig.getBucket());
            qiniuContent.setType(qiniuConfig.getType());
            qiniuContent.setFileKey(putRet.key);
            qiniuContent.setUrl(qiniuConfig.getHost() + "/" + putRet.key);
            qiniuContent.setSize(file.getSize());
            qiniuContentDao.insert(qiniuContent);
            return qiniuContent;
        }
        catch (Exception e)
        {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Override
    public QiniuContent findByContentId(Long id)
    {
        return qiniuContentDao.selectById(id);
    }
    
    @Override
    public String download(QiniuContent content, QiniuConfig config)
    {
        String finalUrl = null;
        if (TYPE.equals(content.getType()))
        {
            finalUrl = content.getUrl();
        }
        else
        {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            /**
             * 1小时，可以自定义链接过期时间
             */
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(QiniuContent content, QiniuConfig config)
    {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = QiNiuUtil.getConfiguration(config.getZone());
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try
        {
            bucketManager.delete(content.getBucket(), content.getFileKey());
            qiniuContentDao.deleteById(content.getId());
        }
        catch (QiniuException ex)
        {
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(QiniuConfig config)
    {
        if (config.getId() == null)
        {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = QiNiuUtil.getConfiguration(config.getZone());
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        // 文件名前缀
        String prefix = "";
        // 每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        // 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        // 列举空间文件列表
        BucketManager.FileListIterator fileListIterator =
            bucketManager.createFileListIterator(config.getBucket(), prefix, limit, delimiter);
        
        QueryWrapper<QiniuContent> wrapper = new QueryWrapper<QiniuContent>();
        while (fileListIterator.hasNext())
        {
            // 处理获取的file list结果
            QiniuContent qiniuContent = null;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items)
            {
                if (qiniuContentDao.selectCount(wrapper.eq("file_key", item.key)) > 0)
                {
                    continue;
                }
                qiniuContent = new QiniuContent();
                qiniuContent.setSize(item.fsize);
                qiniuContent.setFileKey(item.key);
                qiniuContent.setType(config.getType());
                qiniuContent.setBucket(config.getBucket());
                qiniuContent.setUrl(config.getHost() + "/" + item.key);
                qiniuContentDao.insert(qiniuContent);
            }
        }
    }
    
    @Override
    public IPage<QiniuContent> queryAll(int pageNum, int pageSize, QiniuContent qiniuContent)
    {
        QueryWrapper<QiniuContent> wrapper = new QueryWrapper<QiniuContent>();
        if (null != qiniuContent && StringUtils.isNotBlank(qiniuContent.getFileKey()))
        {
            wrapper.like("file_key", qiniuContent.getFileKey());
        }
        return qiniuContentDao.selectPage(new Page<QiniuContent>(pageNum, pageSize), wrapper.orderByDesc("id"));
    }
    
}
