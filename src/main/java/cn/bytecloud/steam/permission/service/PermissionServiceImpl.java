package cn.bytecloud.steam.permission.service;

import cn.bytecloud.steam.permission.dao.PermissionDao;
import cn.bytecloud.steam.permission.dao.PermissionRepository;
import cn.bytecloud.steam.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao dao;

    @Autowired
    private PermissionRepository repository;

    @Override
    public void save(Permission perm) {
        repository.save(perm);
    }

    @Override
    public Permission findFirstByInterfaceUrl(String interfaceUrl) {
        return repository.findFirstByInterfaceUrl(interfaceUrl);
    }

    @Override
    public List<Permission> findByIds(List<String> permissionIds) {
        return dao.findByIds(permissionIds);
    }
}
