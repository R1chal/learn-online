package com.richal.learnonline.userdetail;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.Login;
import com.richal.learnonline.domain.Permission;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.service.ILoginService;
import com.richal.learnonline.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginUserDetailService implements UserDetailsService {

    @Autowired
    private ILoginService loginLogService;

    @Autowired
    private IPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Wrapper<Login> ww = new EntityWrapper<>();
        ww.eq("username", username);
        Login login = loginLogService.selectOne(ww);
        if(login == null){
            throw new GlobleBusinessException("参数不正确");
        }
        List<Permission> permissions = permissionService.queryPermissonByLoginId(login.getId());
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        permissions.forEach(permission -> {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(permission.getSn()));
        });

        User user = new User(
            username,
            login.getPassword(),
            login.getEnabled(),
            login.getAccountNonExpired(),
            login.getCredentialsNonExpired(),
            login.getAccountNonLocked(),
            simpleGrantedAuthorities);
            
        return user;
    }
}
