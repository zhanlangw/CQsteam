package cn.bytecloud.steam.category.service;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.category.dao.CategoryDao;
import cn.bytecloud.steam.category.dto.AddCategoryDto;
import cn.bytecloud.steam.category.dto.CategoryPageDto;
import cn.bytecloud.steam.category.dto.StageDto;
import cn.bytecloud.steam.category.dto.UpdCategoryDto;
import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.category.entity.CategoryStatus;
import cn.bytecloud.steam.category.entity.Stage;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.service.UserService;
import cn.bytecloud.steam.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    /**
     * 添加类别
     *
     * @param dto
     */
    @Override
    public Map<String, Object> save(final AddCategoryDto dto) throws ByteException {
        List<Category> names = categoryDao.findByName(dto.getName());
        List<Category> abbreviations = categoryDao.findByAbbreviation(dto.getAbbreviation());
        if (names.size() > 0) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"name"});
        }
        if (abbreviations.size() > 0) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"abbreviations"});
        }
        checkStage(dto.getStages(), dto.getSignUpTime());

        Category category = dto.toDate();
        if (category.getSignUpTime() < System.currentTimeMillis()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"报名时间"});
        } else {
            category.setStatus(CategoryStatus.NOT_START);
        }
        categoryDao.save(category);
        return category.toDto();
    }

    private void checkStage(List<StageDto> stages, long signUpTime) throws ByteException {
        for (int i = 0; i < stages.size(); i++) {
            if (i == 0 && StringUtil.getTime(stages.get(i).getEndTime()).getTime() < signUpTime) {
                throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"stages[" + i + "]endTime"});
            }
            if (i > 0 && StringUtil.getTime(stages.get(i).getEndTime()).getTime() < StringUtil.getTime(stages.get(i -
                    1).getEndTime()).getTime()) {
                throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"stages[" + i + "]endTime"});
            }
        }
    }

    /**
     * 详情
     */
    @Override
    public Object item(final String id) {
        Category category = categoryDao.findById(id);
        return category.toDto();
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     * @throws ByteException
     */
    @Override
    public Map<String, Object> upd(final UpdCategoryDto dto) throws ByteException {
        Category old = categoryDao.findById(dto.getId());
        int size = projectService.findByCategoryId(dto.getId()).size();

        if (dto.getStatus() == 1 && old.getSignUpTime() <= System.currentTimeMillis()) {
            if (size > 0) {
                throw new ByteException(ErrorCode.FAILURE, "报名开始时间");
            }
        }
        for (int i = 0; i < old.getStages().size(); i++) {
            Stage stage = old.getStages().get(i);
            if (stage.getType().getEnumType() == dto.getStatus() - 1 && System.currentTimeMillis() > StringUtil.getTime
                    (dto.getStages().get(i).getEndTime()).getTime()) {
                throw new ByteException(ErrorCode.FAILURE, stage.getType().getEnumValue() + "时间只能往后延");
            }
        }

        List<Category> names = categoryDao.findByName(dto.getName());
        List<Category> abbreviations = categoryDao.findByAbbreviation(dto.getAbbreviation());

        Optional<Category> first = names.stream()
                .filter(item -> !dto.getId().equals(item.getId()))
                .findFirst();
        if (first.isPresent()) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"name"});
        }

        first = abbreviations.stream()
                .filter(item -> !dto.getId().equals(item.getId()))
                .findFirst();
        if (first.isPresent()) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"abbreviation"});
        }

        checkStage(dto.getStages(), dto.getSignUpTime());

        Category category = dto.toDate();
        categoryDao.save(category);
        return category.toDto();
    }


    /**
     * 列表
     *
     * @param pageDto
     * @param
     * @return
     */
    @Override
    public Object list(CategoryPageDto pageDto, boolean timeFlag) {
        PageModel<Category> page = categoryDao.list(pageDto, timeFlag);
        PageModel<Map> pageModel = new PageModel<Map>(page.getTotalCount());
        page.getValue().forEach(item -> {
            Map map = new HashMap();
            map.put("id", item.getId());
            map.put("name", item.getName());
            User user = userService.findOne(item.getCreatorId());
            map.put("creator", user == null ? "" : user.getName());
            map.put("updateTime", StringUtil.getTime(new Date(item.getUpdateTime())));
            pageModel.addToValue(map);
        });
        return pageModel;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(final String id) throws ByteException {
        List<Project> list = projectService.findByCategoryId(id);
        if (list.size() > 0) {
            throw new ByteException(ErrorCode.DELETION_FORBIDDEN);
        }
        categoryDao.del(id);
    }

    @Override
    public Category findOne(String categoryId) {
        return categoryDao.findById(categoryId);
    }

    @Override
    public Object stage(String id) {
        List list = new ArrayList();
        Category category = categoryDao.findById(id);
        category.getStages().forEach(item->{
            Map map = new HashMap();
            map.put("title", item.getType().getEnumValue());
            map.put("value", item.getType().getEnumType());
            list.add(map);
        });
        return list;
    }
}
