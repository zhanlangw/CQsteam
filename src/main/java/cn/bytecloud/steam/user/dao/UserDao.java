package cn.bytecloud.steam.user.dao;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.user.dto.PageUserDto;
import cn.bytecloud.steam.user.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface UserDao {
    List<User> findByName(String asdf);

    User findOne(String id);

    User save(User user) throws ByteException;

    void updPassword(String telephone, String password);

    List<User> findByRoleId(String id);

    void resetPasswrod(String id, String md5);

    Object list(PageUserDto dto, boolean flag);


    List<User> findByData(Date date);

    List<HashMap> registerStats(StatsDto dto);

    void disable(String id, Boolean flag);

}
