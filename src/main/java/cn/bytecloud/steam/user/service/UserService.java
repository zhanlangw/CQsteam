package cn.bytecloud.steam.user.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.user.dto.*;
import cn.bytecloud.steam.user.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface UserService {
    User findOne(String id);

    void register(UserDto dto, HttpServletRequest request) throws ByteException;

    void captcha(String telephone, HttpServletRequest request, Integer type) throws ByteException;

    Object item(String id);

    void retrievePassword(UserDto dto, HttpServletRequest request) throws ByteException;

    void updPassword(UpdPasswordDto dto, HttpServletRequest request) throws ByteException;

    Object save(AddUserDto dto) throws ByteException;

    List<User> findByRoleId(String id);

    void resetPasswrod(ResetPasswordDto dto) throws ByteException;

    void del(String id) throws ByteException;

    Object list(PageUserDto dto, boolean b);

    void save(Set<String> idCards);

    Object item();

    Object myProject() throws ByteException;

    List<User> findByDate(Date date);

    List<String> registerStats(StatsDto dto);


    Integer registerCount();

    void export(HttpServletResponse response, HttpServletRequest request) throws IOException;

    Object upd(UpdUserDto dto) throws ByteException;

    void disable(String id, Boolean flag);
}
