package cn.bytecloud.steam.role.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.role.dto.AddRoleDto;
import cn.bytecloud.steam.role.dto.PageRoleDto;
import cn.bytecloud.steam.role.dto.UpdRoleDto;
import cn.bytecloud.steam.role.entity.Role;

public interface RoleService {
    Role findOne(String id);

    Object save(AddRoleDto dto) throws ByteException;

    Object item(String id);

    Object upd(UpdRoleDto dto) throws ByteException;

    void del(String id) throws ByteException;

    Object list(PageRoleDto dto);
}
