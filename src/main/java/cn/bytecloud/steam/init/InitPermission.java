package cn.bytecloud.steam.init;

import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.menu.service.MenuService;
import cn.bytecloud.steam.permission.service.PermissionService;
import cn.bytecloud.steam.util.UUIDUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InitPermission {

    @Value("${scan.packages}")
    private String scanPackages;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionService service;

    //    @PostConstruct
    public void initPermission() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Menu.class));
        scanner.findCandidateComponents(scanPackages).forEach(item -> {
            String className = item.getBeanClassName();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
                Menu menu = clazz.getAnnotation(Menu.class);
                cn.bytecloud.steam.menu.entity.Menu tMenu = initMenu(menu.value());
                initPermission(clazz, tMenu);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void initPermission(Class<?> clazz, cn.bytecloud.steam.menu.entity.Menu menu) {

        List<String> permissionIds = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            List<String> collect = Arrays.stream(method.getDeclaredAnnotations()).map(Annotation::annotationType).map
                    (Class::getName).collect(Collectors.toList());
            if (collect.contains(Permission.class.getName()) && collect.contains(RequiresPermissions.class.getName())) {
                Permission permission = method.getDeclaredAnnotation(Permission.class);
                String interfaceUrl = method.getDeclaredAnnotation(RequiresPermissions.class).value()[0];

                cn.bytecloud.steam.permission.entity.Permission perm = null;
                if ((perm = service.findFirstByInterfaceUrl(interfaceUrl)) != null) {
                    permissionIds.add(perm.getId());
                    continue;
                }

                perm = new cn.bytecloud.steam.permission.entity.Permission();
                perm.setName(permission.value());
                perm.setInterfaceUrl(interfaceUrl);
                perm.setId(UUIDUtil.getUUID());
                service.save(perm);
                permissionIds.add(perm.getId());
            }
        }
        menu.setPermissionIds(permissionIds);
        menuService.save(menu);
    }


    public cn.bytecloud.steam.menu.entity.Menu initMenu(String name) {
        cn.bytecloud.steam.menu.entity.Menu tMenu = menuService.findFirstByName(name);
        if (tMenu == null) {
            tMenu = new cn.bytecloud.steam.menu.entity.Menu();
            tMenu.setId(UUIDUtil.getUUID());
            tMenu.setName(name);
            menuService.save(tMenu);
        }
        return tMenu;
    }
}

