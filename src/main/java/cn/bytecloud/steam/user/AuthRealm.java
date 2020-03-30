package cn.bytecloud.steam.user;

import cn.bytecloud.steam.permission.dao.PermissionRepository;
import cn.bytecloud.steam.permission.entity.Permission;
import cn.bytecloud.steam.role.dao.RoleDao;
import cn.bytecloud.steam.role.dao.RoleRepository;
import cn.bytecloud.steam.user.dao.UserRepository;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.entity.UserType;
import cn.bytecloud.steam.util.UUIDUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Set<String> set;
        if (user.getType() == UserType.ROOT) {
            set = permissionRepository.findAll()
                    .stream()
                    .map(Permission::getInterfaceUrl)
                    .collect(Collectors.toSet());
        } else if (user.getType() == UserType.ADMIN) {
            Set<String> roleIds = user.getRoleIds();
            set = roleDao.findByIds(roleIds)
                    .stream()
                    .flatMap(item -> item.getPermissions().stream())
                    .collect(Collectors.toSet());
        } else {
            return null;
        }

        authorizationInfo.addStringPermissions(set);
        return authorizationInfo;
    }


    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String[] split = username.split("_");
        username = split[0];
        boolean adminFlag = split.length == 2;
        User user;
        List<User> list = repository.findByUsername(username);
        if (list.size() != 1) {
            throw new AuthenticationException();
        } else {
            user = list.get(0);
            if (adminFlag && user.getType() == UserType.USER) {
                throw new AuthenticationException();
            }
            if (!adminFlag && !user.isUserflag()) {
                throw new AuthenticationException();
            }
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    public static void main(String[] args) {
        System.out.println(UUIDUtil.getUUID());
    }
}

