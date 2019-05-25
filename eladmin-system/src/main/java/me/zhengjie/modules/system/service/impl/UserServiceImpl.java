package me.zhengjie.modules.system.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.security.security.JwtUser;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.mapper.RoleDao;
import me.zhengjie.modules.system.mapper.UserDao;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.map.UserMapper;
import me.zhengjie.utils.ValidationUtil;

import java.util.Date;
import java.util.List;

/**
 * @author
 * @date 2018-11-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDTO findById(long id)
    {
        User user = userDao.selectById(id);
        return userMapper.toDto(user);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(User resources)
    {
        
        if (userDao.selectCount(new QueryWrapper<User>().eq("username", resources.getUsername())) > 0)
        {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        
        if (userDao.selectCount(new QueryWrapper<User>().eq("email", resources.getEmail())) > 0)
        {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        
        if (resources.getRoles() == null || resources.getRoles().size() == 0)
        {
            throw new BadRequestException("角色不能为空");
        }
        
        // 默认密码 123456，此密码是 MD5加密后的字符
        resources.setPassword("14e1b600b1fd579f47433b88e8d85291");
        resources.setAvatar("https://i.loli.net/2018/12/06/5c08894d8de21.jpg");
        userDao.insert(resources);
        // 保存角色
        if (CollectionUtils.isNotEmpty(resources.getRoles()))
        {
            for (Role role : resources.getRoles())
            {
                userDao.saveRole(resources.getId(), role.getId());
            }
        }
        return userMapper.toDto(resources);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources)
    {
        
        User user = userDao.selectById(resources.getId());
        
        User user1 = userDao.selectOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        User user2 = userDao.selectOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        
        if (resources.getRoles() == null || resources.getRoles().size() == 0)
        {
            throw new BadRequestException("角色不能为空");
        }
        
        if (user1 != null && !user.getId().equals(user1.getId()))
        {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        
        if (user2 != null && !user.getId().equals(user2.getId()))
        {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        // 先删除，再保存角色
        userDao.deleteAllRole(user.getId());
        for (Role role : resources.getRoles())
        {
            userDao.saveRole(user.getId(), role.getId());
        }
        userDao.updateById(user);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id)
    {
        userDao.deleteAllRole(id);
        userDao.deleteById(id);
    }
    
    @Override
    public User findByName(String userName)
    {
        User user = null;
        if (ValidationUtil.isEmail(userName))
        {
            user = userDao.selectOne(new QueryWrapper<User>().eq("email", userName));
        }
        else
        {
            user = userDao.selectOne(new QueryWrapper<User>().eq("username", userName));
        }
        
        if (user == null)
        {
            throw new EntityNotFoundException(User.class, "name", userName);
        }
        else
        {
            return user;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(JwtUser jwtUser, String pass)
    {
        userDao.updatePass(jwtUser.getId(), pass, new Date());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(JwtUser jwtUser, String url)
    {
        userDao.updateAvatar(jwtUser.getId(), url);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(JwtUser jwtUser, String email)
    {
        userDao.updateEmail(jwtUser.getId(), email);
    }
    
    @Override
    public IPage<User> queryAll(int pageNum, int pageSize, UserDTO user)
    {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        if (null != user.getId())
        {
            wrapper.eq("id", user.getId());
        }
        if (null != user.getEnabled())
        {
            wrapper.eq("enabled", user.getEnabled());
        }
        
        if (StringUtils.isNotBlank(user.getUsername()))
        {
            wrapper.like("username", user.getUsername());
        }
        
        if (StringUtils.isNotBlank(user.getEmail()))
        {
            wrapper.like("email", user.getEmail());
        }
        IPage<User> page = userDao.selectPage(new Page<User>(pageNum, pageSize), wrapper);
        if (null != page && CollectionUtils.isNotEmpty(page.getRecords()))
        {
            List<User> records = page.getRecords();
            // 查询角色
            for (User item : records)
            {
                item.setRoles(this.roleDao.findByUser(item.getId()));
            }
            page.setRecords(records);
        }
        return page;
    }
}
