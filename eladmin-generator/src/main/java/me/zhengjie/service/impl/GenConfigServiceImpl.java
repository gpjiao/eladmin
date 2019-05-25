package me.zhengjie.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zhengjie.domain.GenConfig;
import me.zhengjie.mapper.GenConfigDao;
import me.zhengjie.service.GenConfigService;

/**
 * @author jie
 * @date 2019-01-14
 */
@Service
public class GenConfigServiceImpl implements GenConfigService
{
    
    @Autowired
    private GenConfigDao genConfigDao;
    
    @Override
    public GenConfig find()
    {
        GenConfig genConfig = genConfigDao.selectById(1L);
        if (null == genConfig)
        {
            genConfig = new GenConfig();
        }
        return genConfig;
    }
    
    @Override
    public GenConfig update(GenConfig genConfig)
    {
        GenConfig existGenConfig = genConfigDao.selectById(1L);
        genConfig.setId(1L);
        if (null == existGenConfig)
        {
            genConfigDao.insert(genConfig);
        }
        else
        {
            genConfigDao.updateById(genConfig);
        }
        return genConfig;
    }
}
