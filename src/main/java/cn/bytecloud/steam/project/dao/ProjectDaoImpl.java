package cn.bytecloud.steam.project.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.project.dto.ProjectPageDto;
import cn.bytecloud.steam.project.entity.Member;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
@Slf4j
public class ProjectDaoImpl extends BaseDao<Project> implements ProjectDao {
    @Autowired
    private ProjectRepository repository;

    @Autowired
    private MongoTemplate template;

    @Override
    public List<Project> findByCategoryId(String categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public String getNumber(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.PROJECT_CATEGORY_ID).is(categoryId));
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        List<Project> list = template.find(query, Project.class);
        String num;
        if (list.size() == 0) {
            num = "000001";
        } else {
            String number = list.get(0).getNumber();
            num = number.substring(number.length() - 6);
            num = Integer.parseInt(num) + 1 + "";
            while (num.length() < 6) {
                num = "0" + num;
            }
        }
        return num;
    }

    @Override
    public synchronized Project save(Project project) throws ByteException {
        //判断身份证是否报名
        List<Project> list = findByCategoryId(project.getCategoryId());

        if (EmptyUtil.isEmpty(project.getId())) {

            List<String> collect = list.stream().map(Project::getMembers).flatMap(Collection::stream).map
                    (Member::getIdCard).collect(Collectors.toList());

            for (Member memberDto : project.getMembers()) {
                if (!IDCardUtil.isValidate18Idcard(memberDto.getIdCard())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"idCard"});
                }
                if (collect.contains(memberDto.getIdCard())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"身份证号码:" + memberDto.getIdCard()
                            + "已经报名,"});
                }
                if (!MatchUtil.checkEmail(memberDto.getEmail())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"email"});
                }
                if (!MatchUtil.checkTelephone(memberDto.getTelephone())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"telephone"});
                }
            }

            project.setId(UUIDUtil.getUUID());
            project.setCreateTime(System.currentTimeMillis());
            project.setCreatorId(UserUtil.getUserId());
            String number = getNumber(project.getCategoryId());
            project.setNumber(project.getNumber() + number);
        } else {
            Project old = repository.findOne(project.getId());
            project.setCreateTime(old.getCreateTime());
            project.setCreatorId(old.getCreatorId());

            List<String> collect = list.stream().filter(item -> !project.getId().equals(item.getId())).map
                    (Project::getMembers).flatMap
                    (Collection::stream).map
                    (Member::getIdCard).collect(Collectors.toList());

            for (Member memberDto : project.getMembers()) {
                if (!IDCardUtil.isValidate18Idcard(memberDto.getIdCard())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"idCard"});
                }
                if (collect.contains(memberDto.getIdCard())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"身份证号码:" + memberDto.getIdCard()
                            + "已经报名,"});
                }
                if (!MatchUtil.checkEmail(memberDto.getEmail())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"email"});
                }
                if (!MatchUtil.checkTelephone(memberDto.getTelephone())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"telephone"});
                }
            }
        }

        project.setUpdateTime(System.currentTimeMillis());
        repository.save(project);
        return project;
    }

    @Override
    public void submit(String id) {
        Project project = repository.findOne(id);
        project.setZipFlag(true);
        project.setSubmitFlag(true);
        project.getSubmitTime().add(System.currentTimeMillis());
        repository.save(project);
    }

    @Override
    public List<HashMap> myProject() {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.group(ID)
                .push(CREATOR_ID).as(CREATOR_ID)
                .push(PROJECT_CATEGORY_ID).as(PROJECT_CATEGORY_ID)
                .push(PROJECT_NAME).as(PROJECT_NAME)
                .push(PROJECT_TEACHERS + "." + PROJECT_TEACHER_NAME).as(PROJECT_TEACHER_NAME)
                .push(PROJECT_MEMBERS + "." + PROJECT_MEMBER_ID_CARD).as(PROJECT_MEMBER_ID_CARD)
                .push(PROJECT_MEMBERS + "." + PROJECT_MEMBER_NAME).as(PROJECT_MEMBER_NAME)
                .push(PROJECT_SUBMIT_FLAG).as(PROJECT_SUBMIT_FLAG)
                .push(PROJECT_SUBMIT_STATUS).as(PROJECT_SUBMIT_STATUS)
                .first(CREATE_TIME).as(CREATE_TIME)
        );
        list.add(Aggregation.unwind(CREATOR_ID));
        list.add(Aggregation.unwind(CREATE_TIME));
        list.add(Aggregation.unwind(PROJECT_CATEGORY_ID));
        list.add(Aggregation.unwind(PROJECT_MEMBER_ID_CARD));

        list.add(Aggregation.match(new Criteria().orOperator(Criteria.where(CREATOR_ID).is(UserUtil.getUserId()),
                Criteria.where(PROJECT_MEMBER_ID_CARD).elemMatch(new Criteria().in(UserUtil.getUser().getUsername()))
        )));
        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
//        list.add(Aggregation.unwind("user"));
        list.add(LookupOperation.newLookup()
                .from(T_CATEGORY)
                .localField(PROJECT_CATEGORY_ID)
                .foreignField(ID)
                .as("category")
        );
        list.add(Aggregation.unwind("category"));

        list.add(Aggregation.unwind(PROJECT_TEACHER_NAME));
        list.add(Aggregation.unwind(PROJECT_MEMBER_NAME));
        list.add(Aggregation.unwind(PROJECT_SUBMIT_FLAG));
        list.add(Aggregation.unwind(PROJECT_SUBMIT_STATUS));

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));
        list.add(Aggregation.project()
                .and("category." + CATEGORY_NAME).as("categoryName")
                .and("category." + CATEGORY_SATGES).as("categoryStage")
                .and("user." + USER_USERNAME).as("creator")
                .and(ID).as("id")

                .and(PROJECT_NAME).as("projectName")
                .and(PROJECT_TEACHER_NAME).as("teacherName")
                .and(PROJECT_MEMBER_NAME).as("memberName")
                .and(PROJECT_SUBMIT_FLAG).as("submitFlag")
                .and(PROJECT_SUBMIT_STATUS).as("submitStage")
                .andExclude(ID)
        );
        return aggregat(list, T_PROJECT);
    }

    @Override
    public List<HashMap> getExportData(ProjectPageDto dto) {
        List<AggregationOperation> list = new ArrayList<>();

        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();

        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).gte(startTime)));
            }
        }


        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_NAME).regex(dto.getName())));
        }

        if (null != dto.getGroup()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_GROUP).is(GroupType.getType(dto.getGroup()).name())));
        }

        if (null != dto.getDocFlag()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_DOC_SUBMIT_FLAG).is(dto.getDocFlag())));
        }

        if (EmptyUtil.isNotEmpty(dto.getPrize())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_PRIZE).regex(dto.getPrize())));
        }
        if (null != dto.getSubmitFlag()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_SUBMIT_FLAG).is(dto.getSubmitFlag())));
        }

        if (EmptyUtil.isNotEmpty(dto.getPrize())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_PRIZE).regex(dto.getPrize())));
        }

        if (EmptyUtil.isNotEmpty(dto.getNumber())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_NUMBER).regex(dto.getNumber())));
        }

        list.add(LookupOperation.newLookup()
                .from(T_SCHOOL)
                .localField(PROJECT_SCHOOL_ID)
                .foreignField(ID)
                .as("school")
        );
        list.add(Aggregation.unwind("school"));

        list.add(LookupOperation.newLookup()
                .from(T_CATEGORY)
                .localField(PROJECT_CATEGORY_ID)
                .foreignField(ID)
                .as("category")
        );
        list.add(Aggregation.unwind("category"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField(PROJECT_AREA_ID)
                .foreignField(ID)
                .as("area")
        );
        list.add(Aggregation.unwind("area"));

        if (EmptyUtil.isNotEmpty(dto.getDistrict())) {
            list.add(Aggregation.match(Criteria.where("area._id").regex(dto.getDistrict())));
        }

        if (EmptyUtil.isNotEmpty(dto.getCategory())) {
            list.add(Aggregation.match(Criteria.where("category." + CATEGORY_NAME).regex(dto.getCategory())));
        }
        if (EmptyUtil.isNotEmpty(dto.getSchool())) {
            list.add(Aggregation.match(Criteria.where("school." + AREA_NAME).regex(dto.getSchool())));
        }

        list.add(Aggregation.unwind(PROJECT_MEMBERS));

        list.add(LookupOperation.newLookup()
                .from(T_SCHOOL)
                .localField(PROJECT_MEMBERS+"."+PROJECT_MEMBER_SCHOOL_ID)
                .foreignField(ID)
                .as("mSchool")
        );
        list.add(Aggregation.unwind("mSchool"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField(PROJECT_MEMBERS+"."+PROJECT_MEMBER_AREA_ID)
                .foreignField(ID)
                .as("district")
        );
        list.add(Aggregation.unwind("district"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField("district."+AREA_PID)
                .foreignField(ID)
                .as("city")
        );
        list.add(Aggregation.unwind("city"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField("city."+AREA_PID)
                .foreignField(ID)
                .as("province")
        );
        list.add(Aggregation.unwind("province"));

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(PROJECT_NAME).as("name")
                .and(PROJECT_NUMBER).as("number")
                .and("category." + CATEGORY_NAME).as("category")
                .and(PROJECT_MEMBERS+"." + PROJECT_MEMBER_NAME).as("memberName")
                .and(PROJECT_MEMBERS + "." + PROJECT_MEMBER_ID_CARD).as("idCard")
                .and(PROJECT_MEMBERS+"." + PROJECT_MEMBER_TELEPHONE).as("memberTelephone")
                .and("province." + AREA_NAME).as("province")
                .and("city." + AREA_NAME).as("city")
                .and("district." + AREA_NAME).as("district")
                .and( PROJECT_TEACHERS).as("teachers")
                .and( PROJECT_ADDRESS).as("address")
                .and( PROJECT_TELEPHONE).as("telephone")
                .and(PROJECT_MEMBERS+"." + PROJECT_MEMBER_GROUP).as("group")
                .and(PROJECT_MEMBERS+"." + PROJECT_MEMBER_GRADE).as("grade")
                .and("mSchool." + SCHOOL_NAME).as("school")
                .and(PROJECT_DOC_SUBMIT_FLAG).as("docFlag")
                .and(PROJECT_SUBMIT_FLAG).as("submitFlag")
                .and(PROJECT_PRIZE).as("prize")
                .and(CREATE_TIME).as("createTime")
                .and(PROJECT_TELEPHONE).as("telephone")
                .and(PROJECT_SUBMIT_TIME).as("submiTime")
                .and(ID).as("id")
                .andExclude(ID)
        );
        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> data = template.aggregate(aggregation, T_PROJECT, HashMap.class).getMappedResults();

        data.forEach(item -> {
            List<Long> submitTime = (List<Long>) item.remove("submiTime");
            if (submitTime.size() == 0) {
                item.put("submiTime", "");
            } else {
                item.put("submitTime", StringUtil.getTime(new Date(submitTime.get(submitTime.size() - 1))));
            }
            item.put("createTime", StringUtil.getTime(new Date((long) item.remove("createTime"))));

            item.put("group", GroupType.valueOf((String) item.get("group")).getEnumValue());
        });
        return data;
    }

    @Override
    public List<HashMap> getExportStudent() {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(PROJECT_CATEGORY_ID).is("fea3d12f5a0b4becb4b1279349416d96")));

        list.add(Aggregation.unwind(PROJECT_MEMBERS));
        list.add(LookupOperation.newLookup()
                .from(T_SCHOOL)
                .localField(PROJECT_MEMBERS+"."+PROJECT_MEMBER_SCHOOL_ID)
                .foreignField(ID)
                .as("school")
        );
        list.add(Aggregation.unwind("school"));
        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(PROJECT_MEMBERS+"."+PROJECT_MEMBER_ID_CARD)
                .foreignField(USER_USERNAME)
                .as("user")
        );
        list.add(Aggregation.unwind("user"));
        list.add(Aggregation.project()
                .and(PROJECT_MEMBERS+"."+PROJECT_MEMBER_NAME).as("name")
                .and(PROJECT_NUMBER).as("number")
                .and(PROJECT_MEMBERS+"."+PROJECT_MEMBER_TELEPHONE).as("telephone")
                .and(PROJECT_MEMBERS+"."+PROJECT_MEMBER_GROUP).as("group")
                .and(PROJECT_MEMBERS+"."+PROJECT_MEMBER_ID_CARD).as("idcard")
                .and("user."+USER_PASSWORD).as("password")
                .and("school._id").as("schoolId")
                .and("school."+SCHOOL_NAME).as("schoolName")
                .and(ID).as("id")
                .andExclude(ID)
        );
        return  aggregat(list, T_PROJECT);
    }

    @Override
    public List<HashMap> getExportSchool() {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(PROJECT_CATEGORY_ID).is("fea3d12f5a0b4becb4b1279349416d96")));

        list.add(Aggregation.unwind(PROJECT_MEMBERS));

        list.add(LookupOperation.newLookup()
                .from(T_SCHOOL)
                .localField(PROJECT_MEMBERS+"."+PROJECT_MEMBER_SCHOOL_ID)
                .foreignField(ID)
                .as("school")
        );
        list.add(Aggregation.unwind("school"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField(PROJECT_MEMBERS+"."+PROJECT_MEMBER_AREA_ID)
                .foreignField(ID)
                .as("district")
        );
        list.add(Aggregation.unwind("district"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField("district."+AREA_PID)
                .foreignField(ID)
                .as("city")
        );
        list.add(Aggregation.unwind("city"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField("city."+AREA_PID)
                .foreignField(ID)
                .as("province")
        );
        list.add(Aggregation.unwind("province"));

        list.add(Aggregation.group("school._id")
                .first("province." + AREA_NAME).as("province")
                .first("city." + AREA_NAME).as("city")
                .first("district." + AREA_NAME).as("district")
                .first("district._id").as("districtId")
                .first("school._id").as("id")
                .first("school."+SCHOOL_NAME).as("name")
        );

        list.add(Aggregation.unwind("province"));
        list.add(Aggregation.unwind("districtId"));
        list.add(Aggregation.unwind("city"));
        list.add(Aggregation.unwind("district"));
        list.add(Aggregation.unwind("name"));

        return  aggregat(list, T_PROJECT);
    }

    @Override
    public PageModel<HashMap> list(ProjectPageDto dto) {
        List<AggregationOperation> list = new ArrayList<>();

        Integer start = dto.getStart();
        Integer count = dto.getCount();
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();

        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).gte(startTime)));
            }
        }


        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_NAME).regex(dto.getName())));
        }

        if (null != dto.getGroup()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_GROUP).is(GroupType.getType(dto.getGroup()).name())));
        }

        if (null != dto.getDocFlag()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_DOC_SUBMIT_FLAG).is(dto.getDocFlag())));
        }

        if (EmptyUtil.isNotEmpty(dto.getPrize())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_PRIZE).regex(dto.getPrize())));
        }
        if (null != dto.getSubmitFlag()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_SUBMIT_FLAG).is(dto.getSubmitFlag())));
        }

        if (EmptyUtil.isNotEmpty(dto.getPrize())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_PRIZE).regex(dto.getPrize())));
        }

        if (EmptyUtil.isNotEmpty(dto.getNumber())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_NUMBER).regex(dto.getNumber())));
        }

        list.add(LookupOperation.newLookup()
                .from(T_SCHOOL)
                .localField(PROJECT_SCHOOL_ID)
                .foreignField(ID)
                .as("school")
        );
        list.add(Aggregation.unwind("school"));

        list.add(LookupOperation.newLookup()
                .from(T_CATEGORY)
                .localField(PROJECT_CATEGORY_ID)
                .foreignField(ID)
                .as("category")
        );
        list.add(Aggregation.unwind("category"));

        list.add(LookupOperation.newLookup()
                .from(T_AREAT)
                .localField(PROJECT_AREA_ID)
                .foreignField(ID)
                .as("area")
        );
        list.add(Aggregation.unwind("area"));

        if (EmptyUtil.isNotEmpty(dto.getDistrict())) {
            list.add(Aggregation.match(Criteria.where("area._id").regex(dto.getDistrict())));
        }

        if (EmptyUtil.isNotEmpty(dto.getCategory())) {
            list.add(Aggregation.match(Criteria.where("category." + CATEGORY_NAME).regex(dto.getCategory())));
        }
        if (EmptyUtil.isNotEmpty(dto.getSchool())) {
            list.add(Aggregation.match(Criteria.where("school." + AREA_NAME).regex(dto.getSchool())));
        }

        AggregationOperation totalCount = Aggregation.count().as("totalCount");
        list.add(totalCount);

        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> dataCount = template.aggregate(aggregation, T_PROJECT, HashMap.class).getMappedResults();
        if (dataCount.size() == 0) {
            return PageModel.isEmpty();
        }

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));
        list.remove(totalCount);

        if (start != null && count != null) {
            list.add(Aggregation.skip(start));
            list.add(Aggregation.limit(count));
        }


        list.add(Aggregation.project()
                .and(PROJECT_NAME).as("name")
                .and(PROJECT_STAGE).as("stage")
                .and(PROJECT_NUMBER).as("number")
                .and(PROJECT_STAGE).as("stage")
                .and("category." + CATEGORY_NAME).as("category")
                .and(PROJECT_GROUP).as("group")
                .and("area." + AREA_NAME).as("district")
                .and("school." + SCHOOL_NAME).as("school")
                .and(PROJECT_DOC_SUBMIT_FLAG).as("docFlag")
                .and(PROJECT_SUBMIT_FLAG).as("submitFlag")
                .and(PROJECT_PRIZE).as("prize")
                .and(CREATE_TIME).as("createTime")
                .and(PROJECT_TELEPHONE).as("telephone")
                .and(PROJECT_SUBMIT_TIME).as("submiTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);

        List<HashMap> data = template.aggregate(aggregation, T_PROJECT, HashMap.class).getMappedResults();

        data.forEach(item -> {
            List<Long> submitTime = (List<Long>) item.remove("submiTime");
//            item.put("stage", StageType.valueOf((String) item.remove("stage")).getEnumValue());
            if (submitTime.size() == 0) {
                item.put("submiTime", "");
            } else {
                item.put("submitTime", StringUtil.getTime(new Date(submitTime.get(submitTime.size() - 1))));
            }
            item.put("createTime", StringUtil.getTime(new Date((long) item.remove("createTime"))));

            item.put("group", GroupType.valueOf((String) item.get("group")).getEnumType());
        });
        return new PageModel<HashMap>((int) (dataCount.get(0).get("totalCount")), data);
    }

    @Override
    public List<Project> findByDate(Date date, String field) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).gte(date.getTime()));
        return template.find(query, Project.class);
    }

    @Override
    public List<HashMap> signUpStats(StatsDto dto) {
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();
        List<AggregationOperation> list = new ArrayList<>();
        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(PROJECT_SIGN_UP_TIME).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(PROJECT_SIGN_UP_TIME).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(PROJECT_SIGN_UP_TIME).gte(startTime)));
            }
        }

        if (EmptyUtil.isNotEmpty(dto.getCategoryId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_CATEGORY_ID).is(dto.getCategoryId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getAreaId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_AREA_ID).is(dto.getAreaId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getSchoolId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_SCHOOL_ID).is(dto.getSchoolId())));
        }
        if (dto.getGroup().isPresent()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_GROUP).is(dto.getGroup().get().name())));
        }

        list.add(Aggregation.unwind(PROJECT_MEMBERS));
        if (EmptyUtil.isNotEmpty(dto.getGender())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_MEMBERS + "." + PROJECT_MEMBER_GENDER).is(dto.getGender
                    ())));
        }
        list.add(Aggregation.group(ID).count().as("count").first(PROJECT_SIGN_UP_TIME).as("time"));

        list.add(Aggregation.unwind("time"));
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, "time")));

        list.add(Aggregation.project().andExclude(ID));
        return aggregat(list, T_PROJECT);
    }

    @Override
    public List<HashMap> wordStats(StatsDto dto) {
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();
        List<AggregationOperation> list = new ArrayList<>();

        list.add(Aggregation.unwind(PROJECT_DOC_SUBMIT_TIME));
        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(PROJECT_DOC_SUBMIT_TIME).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(PROJECT_DOC_SUBMIT_TIME).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(PROJECT_DOC_SUBMIT_TIME).gte(startTime)));
            }
        }

        if (EmptyUtil.isNotEmpty(dto.getCategoryId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_CATEGORY_ID).is(dto.getCategoryId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getAreaId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_AREA_ID).is(dto.getAreaId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getSchoolId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_SCHOOL_ID).is(dto.getSchoolId())));
        }
        if (dto.getGroup().isPresent()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_GROUP).is(dto.getGroup().get().name())));
        }

        list.add(Aggregation.group(PROJECT_DOC_SUBMIT_TIME).count().as("count").first(PROJECT_DOC_SUBMIT_TIME).as
                ("time"));

        list.add(Aggregation.unwind("time"));
        list.add(Aggregation.sort(Sort.Direction.ASC, "time"));

        list.add(Aggregation.project().andExclude(ID));
        return aggregat(list, T_PROJECT);
    }

    @Override
    public List<HashMap> submitStats(StatsDto dto) {
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();
        List<AggregationOperation> list = new ArrayList<>();

        list.add(Aggregation.unwind(PROJECT_SUBMIT_TIME));
        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(PROJECT_SUBMIT_TIME).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(PROJECT_SUBMIT_TIME).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(PROJECT_SUBMIT_TIME).gte(startTime)));
            }
        }

        if (EmptyUtil.isNotEmpty(dto.getCategoryId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_CATEGORY_ID).is(dto.getCategoryId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getAreaId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_AREA_ID).is(dto.getAreaId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getSchoolId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_SCHOOL_ID).is(dto.getSchoolId())));
        }
        if (dto.getGroup().isPresent()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_GROUP).is(dto.getGroup().get().name())));
        }

        list.add(Aggregation.group(PROJECT_SUBMIT_TIME).count().as("count").first(PROJECT_SUBMIT_TIME).as("time"));

        list.add(Aggregation.unwind("time"));
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, "time")));

        list.add(Aggregation.project().andExclude(ID));
        return aggregat(list, T_PROJECT);
    }

    @Override
    public List<HashMap> prizeStats(StatsDto dto) {
        List<AggregationOperation> list = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(dto.getCategoryId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_CATEGORY_ID).is(dto.getCategoryId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getAreaId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_AREA_ID).is(dto.getAreaId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getSchoolId())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_SCHOOL_ID).is(dto.getSchoolId())));
        }
        if (dto.getGroup().isPresent()) {
            list.add(Aggregation.match(Criteria.where(PROJECT_GROUP).is(dto.getGroup().get().name())));
        }
        list.add(Aggregation.match(Criteria.where(PROJECT_PRIZE).ne(null)));
        list.add(Aggregation.unwind(PROJECT_MEMBERS));
        if (EmptyUtil.isNotEmpty(dto.getGender())) {
            list.add(Aggregation.match(Criteria.where(PROJECT_MEMBERS + "." + PROJECT_MEMBER_GENDER).is(dto.getGender
                    ())));
        }

        list.add(Aggregation.group(PROJECT_PRIZE).count().as("count").push(PROJECT_PRIZE).as("prize"));
        list.add(Aggregation.unwind("prize"));
        list.add(Aggregation.project().andExclude(ID));
        return aggregat(list, T_PROJECT);

    }

    @Override
    public void eliminate(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).is(id));
        Update update = new Update();
        update.set(ModelConstant.PROJECT_ELIMINATE_FLAG, true);
        template.updateFirst(query, update, Project.class);
    }
}
