package cn.bytecloud.steam.role.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.role.dao.RoleDao;
import cn.bytecloud.steam.role.dao.RoleRepository;
import cn.bytecloud.steam.role.dto.AddRoleDto;
import cn.bytecloud.steam.role.dto.PageRoleDto;
import cn.bytecloud.steam.role.dto.UpdRoleDto;
import cn.bytecloud.steam.role.entity.Role;
import cn.bytecloud.steam.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao dao;

    @Autowired
    private RoleRepository repository;

    @Autowired
    private UserService userService;

    @Override
    public Role findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public Object save(AddRoleDto dto) throws ByteException {
        if (repository.findFirstByName(dto.getName()) != null) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"name"});
        }
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }

    @Override
    public Object upd(UpdRoleDto dto) throws ByteException {
        List<String> list = repository.findByName(dto.getName()).stream().map(Role::getId).filter(id -> !id.equals(dto.getId())).collect
                (Collectors.toList());
        if (list.size() > 0) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"name"});
        }
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public void del(String id) throws ByteException {
        if (userService.findByRoleId(id).size() > 0) {
            throw new ByteException(ErrorCode.DELETION_FORBIDDEN);
        }
        repository.delete(id);
    }

    @Override
    public Object list(PageRoleDto dto) {
        return dao.list(dto);
    }
}
