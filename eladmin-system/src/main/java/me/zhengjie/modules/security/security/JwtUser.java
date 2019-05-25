package me.zhengjie.modules.security.security;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author
 * @date 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    @JSONField(serialize = false)
    private final Long id;
    
    private final String username;
    
    @JSONField(serialize = false)
    private final String password;
    
    private final String avatar;
    
    private final String email;
    
    @JSONField(serialize = false)
    private final Collection<? extends GrantedAuthority> authorities;
    
    private final boolean enabled;
    
    private Timestamp createTime;
    
    @JSONField(serialize = false)
    private final Date lastPasswordResetDate;
    
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired()
    {
        return true;
    }
    
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked()
    {
        return true;
    }
    
    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired()
    {
        return true;
    }
    
    @Override
    @JSONField(serialize = false)
    public String getPassword()
    {
        return password;
    }
    
    @Override
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public Collection<?> getRoles()
    {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
