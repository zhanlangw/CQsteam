package cn.bytecloud.steam.user.service;

import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.user.dao.UserDao;
import cn.bytecloud.steam.user.dao.UserRepository;
import cn.bytecloud.steam.user.dto.*;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.entity.UserType;
import cn.bytecloud.steam.user.thread.SMSHandler;
import cn.bytecloud.steam.util.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    public static final String REGISTER_CODE = "registerCode";
    public static final String REGISTER_TIME = "registerTime";
    public static final String PASSWORD_UPD_CODE = "passwordUpdCode";
    public static final String PASSWORD_UPD_TIME = "passwordUpdTime";

    @Autowired
    private UserDao dao;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SMSHandler handler;

    @Override
    public User findOne(final String id) {
        return dao.findOne(id);
    }

    @Override
    public void register(UserDto dto, HttpServletRequest request) throws ByteException {
        validDto(dto, request, 1);
        dao.save(dto.toData());
    }

    @Override
    public void captcha(@Valid String telephone, HttpServletRequest request, Integer type) throws ByteException {
        if (!MatchUtil.checkTelephone(telephone)) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"telephone"});
        }
        String code = new Random().nextInt(90000) + 10000 + "";
        String key = type.equals(1) ? REGISTER_CODE + telephone : PASSWORD_UPD_CODE + telephone;
        String time = type.equals(1) ? REGISTER_TIME + telephone : PASSWORD_UPD_TIME + telephone;
        request.getSession().setAttribute(key, code);
        request.getSession().setAttribute(time, System.currentTimeMillis());
        handler.getInstance().addCaptchaMsg(telephone, code);
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }

    @Override
    public void retrievePassword(UserDto dto, HttpServletRequest request) throws ByteException {
        validDto(dto, request, 2);
        dao.updPassword(dto.getUsername(), MD5Util.getMD5(dto.getPassword()));
    }

    @Override
    public void updPassword(UpdPasswordDto dto, HttpServletRequest request) throws ByteException {
        User user = UserUtil.getUser();
        String oldPassword = MD5Util.getMD5(dto.getOldPassword());
        String newPassword = MD5Util.getMD5(dto.getNewPassword());
        if (!oldPassword.equals(user.getPassword())) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"原密码"});
        }
        if (!MatchUtil.checkPassword(dto.getNewPassword())) {
            throw new ByteException(ErrorCode.PARAMETER, "密码由8到16数字和字母组成");
        }
        dao.updPassword(user.getUsername(), newPassword);

    }

    @Override
    public Object upd(UpdUserDto dto) throws ByteException {
        if (!MatchUtil.checkTelephone(dto.getLoginName())) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"登录名"});
        }

        User user = dto.toData();
        user.setType(UserType.ADMIN);
        List<User> list = repository.findByUsername(dto.getLoginName());
        for (User item : list) {
            if (item.getType() != UserType.USER && !item.getId().equals(user.getId())) {
                throw new ByteException(ErrorCode.FAILURE, "登录名重复");
            } else {
                user.setId(item.getId());
            }
        }
        user.setPassword(repository.findOne(user.getId()).getPassword());
        return dao.save(user).toDto();
    }

    @Override
    public void disable(String id, Boolean flag) {
        dao.disable(id,flag);
    }

    @Override
    public Object save(AddUserDto dto) throws ByteException {
        if (!MatchUtil.checkTelephone(dto.getLoginName())) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"loginName"});
        }

        if (!MatchUtil.checkPassword(dto.getPassword())) {
            throw new ByteException(ErrorCode.PARAMETER, "密码必须由数字和英文组成");
        }
        User user = dto.toData();
        user.setType(UserType.ADMIN);
        List<User> list = repository.findByUsername(dto.getLoginName());
        for (User item : list) {
            if (item.getType() != UserType.USER) {
                throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"loginName"});
            } else {
                user.setId(item.getId());
            }
        }
        return dao.save(user).toDto();
    }

    @Override
    public List<User> findByRoleId(String id) {
        return dao.findByRoleId(id);
    }

    @Override
    public void resetPasswrod(ResetPasswordDto dto) throws ByteException {
        if (!MatchUtil.checkPassword(dto.getPassword())) {
            throw new ByteException(ErrorCode.PARAMETER, "密码必须由数字和英文组成");
        }
        User user = repository.findOne(dto.getId());
        if (user.getType() == UserType.ROOT && UserUtil.getUser().getType() != UserType.ROOT) {
            throw new ByteException(ErrorCode.AUTHORIZATION);
        }
        dao.resetPasswrod(dto.getId(), MD5Util.getMD5(dto.getPassword()));
    }

    @Override
    public void del(String id) throws ByteException {
        if (repository.findOne(id).getType() == UserType.ROOT) {
            throw new ByteException(ErrorCode.FAILURE, "禁止删除");
        }
        if (UserUtil.getUserId().equals(id)) {
            throw new ByteException(ErrorCode.FAILURE, "禁止删除");
        }

        repository.delete(id);
        if (UserUtil.getUserId().equals(id)) {
            SecurityUtils.getSubject().logout();
        }
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.getTime("2019/04/04 16:35:48").getTime());
    }

    @Override
    public Object list(PageUserDto dto, boolean flag) {
        return dao.list(dto, flag);
    }

    @Override
    public void save(Set<String> idCards) {
        idCards.forEach(idCard -> {
            if (null == repository.findFirstByUsername(idCard)) {
                User user = new User();
                user.setId(UUIDUtil.getUUID());
                user.setUsername(idCard);
                user.setType(UserType.USER);
                user.setPassword(MD5Util.getMD5(idCard.substring(idCard.length() - 6)));
                user.setCreateTime(System.currentTimeMillis());
                user.setUpdateTime(System.currentTimeMillis());
                repository.save(user);
            }
        });
    }

    @Override
    public Object item() {
        User user = UserUtil.getUser();
        String name;
        String telephone = "无";
        String idCard = "无";
        Map map = new HashMap();
        if (EmptyUtil.isNotEmpty(user.getName())) {
            name = user.getName();
        } else {
            name = user.getUsername();
        }
        if (MatchUtil.checkTelephone(user.getUsername())) {
            telephone = user.getUsername();
        } else {
            idCard = user.getUsername();
        }
        map.put("name", name);
        map.put("telephone", telephone);
        map.put("idCard", idCard);
        return map;
    }

    @Override
    public Object myProject() throws ByteException {
        return projectService.myProject();
    }

    @Override
    public List<User> findByDate(Date date) {
        return dao.findByData(date);
    }

    @Override
    public List<String> registerStats(StatsDto dto) {
        return dao.registerStats(dto).stream().mapToLong(map -> (long) map.get("time")).sorted()
                .mapToObj(Date::new).map(StringUtil::getStatsTime).collect(Collectors.toList());

    }


    @Override
    public Integer registerCount() {
        return repository.findByType(UserType.USER).size();
    }

    @Override
    public void export(HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<User> list = repository.findByType(UserType.USER, new Sort(Sort.Direction.DESC, ModelConstant
                .CREATE_TIME));
        Vector<Vector<String>> rowName = new Vector<>();
        for (User user : list) {
            Vector<String> row = new Vector<>();
            row.add(user.getUsername());
            row.add(user.getPassword());
            row.add(StringUtil.getTime(new Date(user.getCreateTime())));
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("电话/身份证");
        rowTopName.add("密码");
        rowTopName.add("注册时间");


        String fileName = "用户数据_" + StringUtil.getExportTime();


        ServletOutputStream out = response.getOutputStream();

        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {//非IE内核
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        fileName += ".xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);
        out.flush();
        out.close();
    }




    private void validDto(UserDto dto, HttpServletRequest request, int type) throws ByteException {
        String code = (String) request.getSession().getAttribute(type == 1 ? REGISTER_CODE + dto.getUsername() :
                PASSWORD_UPD_CODE + dto.getUsername());
        Long time = (Long) request.getSession().getAttribute(type == 1 ? REGISTER_TIME + dto.getUsername() :
                PASSWORD_UPD_TIME + dto.getUsername());
        if (EmptyUtil.isEmpty(code) || !code.equals(dto.getCaptcha())) {
            throw new ByteException(ErrorCode.VERIFICATION_CODE);
        }
        if (System.currentTimeMillis() - time > 1000 * 60 * 5) {
            throw new ByteException(ErrorCode.VERIFICATION_CODE, "验证码过期");
        }
        if (!MatchUtil.checkTelephone(dto.getUsername())) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"username"});
        }

        if (!MatchUtil.checkPassword(dto.getPassword())) {
            throw new ByteException(ErrorCode.PARAMETER, "密码必须由数字和英文组成");
        }
        List<User> list = repository.findByUsername(dto.getUsername());
        if (type == 1 && list.size() > 0) {
            throw new ByteException(ErrorCode.PARAMETER, "该用户已经被注册");
        } else if (type == 2 && list.size() != 1) {
            throw new ByteException(ErrorCode.PARAMETER, "该用户未注册注册");
        }
    }
}
