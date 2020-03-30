package cn.bytecloud.steam.event.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.event.dto.EventPageDto;
import cn.bytecloud.steam.event.entity.Event;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.StringUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class EventDaoImpl extends BaseDao<Event> implements EventDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private EventRepository repository;

    @Override
    public Event save(Event event) {
        if (EmptyUtil.isEmpty(event.getId())) {
            event.setId(UUIDUtil.getUUID());
            event.setCreatorId(UserUtil.getUserId());
            event.setCreateTime(System.currentTimeMillis());
        } else {
            Event old = repository.findOne(event.getId());
            event.setCreatorId(old.getCreatorId());
            event.setCreateTime(old.getCreateTime());
        }
        event.setUpdateTime(System.currentTimeMillis());
        repository.save(event);
        return event;
    }

    @Override
    public PageModel<HashMap> list(EventPageDto dto) {

        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(EVENT_TITLE).regex(dto.getTitle())));
        }
        if (EmptyUtil.isNotEmpty(dto.getStage())) {
            list.add(Aggregation.match(Criteria.where(EVENT_STAGE).regex(dto.getStage())));
        }
        if (EmptyUtil.isNotEmpty(dto.getAddress())) {
            list.add(Aggregation.match(Criteria.where(EVENT_ADDRESS).regex(dto.getAddress())));
        }
        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
        if (EmptyUtil.isNotEmpty(dto.getCreator())) {
            list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(dto.getCreator())));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, EVENT_START_TIME)));

        Map map = new HashMap();
        map.put(EVENT_TITLE, "$" + EVENT_TITLE);
        map.put("id", "$" + ID);
        map.put(EVENT_ADDRESS, "$" + EVENT_ADDRESS);
        map.put(EVENT_START_TIME, "$" + EVENT_START_TIME);
        map.put(EVENT_END_TIME, "$" + EVENT_END_TIME);
        map.put(EVENT_STAGE, "$" + EVENT_STAGE);
        map.put(CREATE_TIME, "$" + CREATE_TIME);
        map.put(USER_NAME, "$user." + USER_NAME);

        list.add(Aggregation.group(EVENT_TITLE).push(map).as(T_EVENT));
        list.add(Aggregation.unwind(T_EVENT));


        list.add(Aggregation.project()
                .and(T_EVENT + "." + EVENT_TITLE).as("title")
                .and(T_EVENT + "." + EVENT_START_TIME).as("startTime")
                .and(T_EVENT + "." + EVENT_END_TIME).as("endTime")
                .and(T_EVENT + "." + EVENT_STAGE).as("stage")
                .and(T_EVENT + "." + EVENT_ADDRESS).as("address")
                .and(T_EVENT + "." + USER_NAME).as("creator")
                .and(T_EVENT + "." + CREATE_TIME).as("createTime")
                .and(T_EVENT+".id").as("id")
                .andExclude(ID)
        );

        PageModel<HashMap> pageModel = pageList(list, dto, T_EVENT, CREATE_TIME);
        List<HashMap> value = pageModel.getValue()
                .stream()
                .peek(item -> item.put("startTime", StringUtil.getTime(new Date((long) item.remove("startTime")))))
                .peek(item -> item.put("endTime", StringUtil.getTime(new Date((long) item.remove("endTime")))))
                .collect(Collectors.toList());
        pageModel.setValue(value);
        return pageModel;
    }

    @Override
    public Object homeList() {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, EVENT_START_TIME)));

        Map map = new HashMap();
        map.put(EVENT_TITLE, "$" + EVENT_TITLE);
        map.put(EVENT_START_TIME, "$" + EVENT_START_TIME);
        map.put(EVENT_END_TIME, "$" + EVENT_END_TIME);
        map.put(EVENT_STAGE, "$" + EVENT_STAGE);
        map.put(CREATE_TIME, "$" + CREATE_TIME);
        map.put(USER_NAME, "$user." + USER_NAME);

        list.add(Aggregation.group(EVENT_TITLE).push(map).as(T_EVENT));
        list.add(Aggregation.unwind(T_EVENT));

        list.add(Aggregation.project()
                .and(T_EVENT + "." + EVENT_TITLE).as("title")
                .and(T_EVENT + "." + EVENT_START_TIME).as("startTime")
                .and(T_EVENT + "." + EVENT_END_TIME).as("endTime")
                .and(T_EVENT + "." + EVENT_STAGE).as("stage")
                .and(T_EVENT + "." + EVENT_ADDRESS).as("address")
                .and(T_EVENT + "." + USER_NAME).as("creator")
                .and(ID).as("id")
                .andExclude(ID)
        );
        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> data = template.aggregate(aggregation, T_EVENT, HashMap.class).getMappedResults();
        List<HashMap> collect = data.stream().peek(item -> item.put("startTime", StringUtil.getTime(new Date((long)
                item.remove("startTime")))))
                .peek(item -> item.put("endTime", StringUtil.getTime(new Date((long) item.remove("endTime")))))
                .collect(Collectors.toList());
        return collect;
    }
}
