package com.damon.rmq.admin.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.damon.rmq.api.admin.model.po.SysResource;
import com.damon.rmq.api.admin.model.po.SysRole;
import com.damon.rmq.api.admin.model.po.SysUser;
import com.damon.rmq.api.admin.service.ISysResourceService;
import com.damon.rmq.api.admin.service.ISysRoleService;
import com.damon.rmq.api.admin.service.ISysUserService;

/**
 * <p>
 * ShiroRealm 实现类
 * </p>
 *
 */
@Component
public class AdminShiroRealm extends AuthorizingRealm {

    @DubboReference
    private ISysUserService sysUserService;
    @DubboReference
    private ISysRoleService sysRoleService;
    @DubboReference
    private ISysResourceService sysResourceService;

    /**
     * <p>
     * 获取用户权限信息
     * </p>
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录时输入的用户名
        String username = (String) principalCollection.fromRealm(getName()).iterator().next();
        // 到数据库查是否有此对象
        List<SysUser> users = sysUserService.selectByUserName(username);
        if (null == users || users.size() <= 0) {
            return null;
        }

        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及资源（resource）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<SysRole> roles = sysRoleService.selectByUserId(users.get(0).getSysUserId());
        if (null == roles || roles.size() == 0) {
            return null;
        }

        Set<String> roleNames = new HashSet<>();
        for (SysRole role : roles) {
            roleNames.add(role.getRoleName());
        }

        info.setRoles(roleNames);
        List<SysResource> resources = sysResourceService.selectByUserId(users.get(0).getSysUserId());
        for (SysResource resource : resources) {
            if (StringUtils.isEmpty(resource.getPermission())) {
                continue;
            }
            info.addStringPermission(resource.getPermission());
        }
        return info;
    }

    /**
     * <p>
     * 获取用户登录认证信息
     * </p>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        List<SysUser> users = sysUserService.selectByUserName(token.getUsername());
        if (null == users || users.size() <= 0) {
            return null;
        }

        SysUser user = users.get(0);
        return new SimpleAuthenticationInfo(user.getUserName(), user.getUserPwd(), getName());
    }
}
