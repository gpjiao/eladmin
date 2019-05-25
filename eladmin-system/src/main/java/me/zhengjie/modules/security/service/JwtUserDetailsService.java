package me.zhengjie.modules.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.security.security.JwtUser;
import me.zhengjie.modules.system.domain.Permission;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.mapper.PermissionDao;
import me.zhengjie.modules.system.mapper.RoleDao;
import me.zhengjie.modules.system.mapper.UserDao;
import me.zhengjie.utils.ValidationUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author
 * @date 2018-11-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService
{
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private PermissionDao permissionDao;
    
    @Override
    public UserDetails loadUserByUsername(String username)
    {
        User user = null;
        if (ValidationUtil.isEmail(username))
        {
            user = userDao.selectOne(new QueryWrapper<User>().eq("email", username));
        }
        else
        {
            user = userDao.selectOne(new QueryWrapper<User>().eq("username", username));
        }
        
        if (user == null)
        {
            throw new EntityNotFoundException(User.class, "name", username);
        }
        else
        {
            return create(user);
        }
    }
    
    public UserDetails create(User user)
    {
        return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getAvatar(), user.getEmail(),
            mapToGrantedAuthorities(roleDao.findByUser(user.getId()), permissionDao), user.getEnabled(),
            user.getCreateTime(), user.getLastPasswordResetTime());
    }
    
    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles, PermissionDao permissionDao)
    {
        
        Set<Permission> permissions = new HashSet<>();
        for (Role role : roles)
        {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            permissions.addAll(permissionDao.findByRole(role.getId()));
        }
        return permissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());
    }
}
