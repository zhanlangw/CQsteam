package cn.bytecloud.steam;

import cn.bytecloud.steam.banner.entity.Banner;
import cn.bytecloud.steam.category.dao.CategoryRepository;
import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.project.dao.ProjectRepository;
import cn.bytecloud.steam.project.entity.Material;
import cn.bytecloud.steam.project.entity.Member;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.constant.ModelConstant.PROJECT_MEMBERS;
import static cn.bytecloud.steam.constant.ModelConstant.PROJECT_MEMBER_ID_CARD;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SteamApplicationTests {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;


    @Test
    public void findAll() {
        for (Project project : projectRepository.findAll()) {
            project.setCreatorId("system");
            projectRepository.save(project);
        }

//        List<Project> collect = projectRepository.findAll().stream().filter(project -> project.getMaterials().size() > 0).collect(Collectors.toList());
//        long time = Objects.requireNonNull(StringUtil.getTime("2019/07/01 12:00:00")).getTime();
//        for (Project project : collect) {
//            long count = 0l;
//            for (Material material : project.getMaterials()) {
//              count += material.getDocPath().size() + material.getPptPath().size();
//            }
//            List<Long> submitTime = project.getDocSubmitTime();
//            for (int i = 0;i<count;i++) {
//                submitTime.add(time);
//            }
//            projectRepository.save(project);
//        }
    }

    @Test
    public void project() {
        List<HashMap> data = new ArrayList();
        List<Category> list = template.findAll(Category.class);
        for (Category category : list) {
            List<String> idcards = projectService.findByCategoryId(category.getId()).stream().map
                    (Project::getMembers).flatMap(Collection::stream).map(Member::getIdCard).collect(Collectors
                    .toList());
            HashMap map = new HashMap();
            Set<String> set = new HashSet();
            for (String idcard : idcards) {
                if (!set.contains(idcard)) {
                    List<String> demo = new ArrayList<>(idcards);
                    demo.remove(idcard);
                    if (demo.contains(idcard)) {
                        set.add(idcard);
                    }
                }
            }
            map.put(category.getName(), set);
            data.add(map);
        }

        for (HashMap map : data) {
            map.forEach((key, value) -> {
                System.out.println("//------------------------------------------------------------");
                System.out.println("//" + key);
                System.out.println("//------------------------------------------------------------");
                System.out.println();
                System.out.println();
                HashSet set = (HashSet) value;
                set.forEach(idCard -> {
                    Query query = new Query();
                    query.addCriteria(Criteria.where(PROJECT_MEMBERS + "." + PROJECT_MEMBER_ID_CARD).is(idCard));
                    List<Project> projects = template.find(query, Project.class);
                    System.out.println(JSONObject.toJSONString(projects));
                    System.out.println();
                    System.out.println();
                    System.out.println();
                });
            });
        }

//        List<AggregationOperation> aggList = new ArrayList<>();
//
//        aggList.add(LookupOperation.newLookup()
//                .from(T_CATEGORY)
//                .localField(PROJECT_CATEGORY_ID)
//                .foreignField("_id")
//                .as("category")
//        );
//        aggList.add(Aggregation.unwind("category"));
//
//        Map map = new HashMap();
//        map.put(PROJECT_NUMBER, "$" + PROJECT_NUMBER);
//        map.put("id", "$" + ID);
//        map.put(PROJECT_MEMBERS, "$" + PROJECT_MEMBERS);
//        map.put(PROJECT_MATERIALS, "$" + PROJECT_MATERIALS);
//        map.put(CREATE_TIME, "$" + CREATE_TIME);
//
//        aggList.add(Aggregation.group("category." + CATEGORY_NAME).push(map).as("project"));
//        Aggregation aggregation = Aggregation.newAggregation(aggList);
//        List<HashMap> data = template.aggregate(aggregation, T_PROJECT, HashMap.class).getMappedResults();
//        System.out.println(data);
    }

    @Test
    public void query() {
        DBObject dbObject = new BasicDBObject();

        DBObject fieldObject = new BasicDBObject();

        fieldObject.put("_id", true);

        Query query = new BasicQuery(dbObject, fieldObject);
        List<Banner> banners = template.find(query, Banner.class);
        System.out.println(banners);
    }

    @Test
    public void contextLoads() {
//        AA aa = new AA();
//        aa.setId(UUIDUtil.getUUID());
//        List list = new ArrayList<>();
//        list.add('1');
//        list.add('2');
//        list.add('3');
//
//        aa.setIds(list);
//        template.save(aa);
        Query query = new Query();
        List list = new ArrayList<>();
        list.add('1');
        query.addCriteria(Criteria.where("ids").in(list));
        List<AA> aas = template.find(query, AA.class);
        System.out.println();
    }

    @Test
    public void saveData() {

        List<Project> list = template.findAll(Project.class);


//        for (int i = 0; i < 10000; i++) {
//            String uuid = UUIDUtil.getUUID();
//            Ref3 ref3 = new Ref3(uuid, uuid, uuid, uuid, uuid, uuid, uuid, uuid, uuid, uuid);
//            template.save(ref3);
//        }
//
//        List<Ref3> list3 = template.findAll(Ref3.class);
//        Random random = new Random();
//        for (int i = 0; i < 10000; i++) {
//            String uuid = UUIDUtil.getUUID();
//            Ref2 ref2 = new Ref2(uuid, list3.get(random.nextInt(list3.size())).getId(), uuid, uuid, uuid, uuid, uuid,
//                    uuid, uuid, uuid, uuid);
//            template.save(ref2);
//        }
//
//        List<Ref2> list2 = template.findAll(Ref2.class);
//        for (int i = 0; i < 10000; i++) {
//            String uuid = UUIDUtil.getUUID();
//            Ref1 ref1 = new Ref1(uuid, list2.get(random.nextInt(list2.size())).getId(), uuid, uuid, uuid, uuid, uuid,
//                    uuid, uuid, uuid, uuid);
//            template.save(ref1);
//        }
//
//        List<Ref1> list1 = template.findAll(Ref1.class);
//        for (int i = 0; i < 100000; i++) {
//            String uuid = UUIDUtil.getUUID();
//            Data data = new Data(uuid, list1.get(random.nextInt(list1.size())).getId(), uuid, uuid, uuid, uuid, uuid,
//                    uuid, uuid, uuid, uuid);
//            template.save(data);
//        }
//        long time = System.currentTimeMillis();
//        long count = template.count(new Query(), Data.class);
//        System.out.println(count);
//        System.out.println(System.currentTimeMillis()-time);
    }

    @Test
    public void aggregation() {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(LookupOperation.newLookup()
                .from("ref1")
                .localField("ref1Id")
                .foreignField("_id")
                .as("ref1"));
        list.add(Aggregation.unwind("ref1"));

        list.add(LookupOperation.newLookup()
                .from("ref2")
                .localField("ref1.ref2Id")
                .foreignField("_id")
                .as("ref2"));
        list.add(Aggregation.unwind("ref2"));

        list.add(LookupOperation.newLookup()
                .from("ref3")
                .localField("ref2.ref3Id")
                .foreignField("_id")
                .as("ref3"));
        list.add(Aggregation.unwind("ref3"));


        CountOperation count = Aggregation.count().as("count");
        list.add(count);
        Aggregation countAggregation = Aggregation.newAggregation(list);
        long countTime = System.currentTimeMillis();
        List<HashMap> countData = template.aggregate(countAggregation, "data", HashMap.class).getMappedResults();
        System.out.println(System.currentTimeMillis() - countTime);
        System.out.println(countData);
        list.remove(count);


        list.add(Aggregation.skip(0));
        list.add(Aggregation.limit(20));
//        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC,"_id")));

        Aggregation dataAggregation = Aggregation.newAggregation(list);
        long time = System.currentTimeMillis();
        List<HashMap> data = template.aggregate(dataAggregation, "data", HashMap.class).getMappedResults();
        System.out.println(System.currentTimeMillis() - time);
        System.out.println();

    }

    @Test
    public void test3() {
//        for (int i = 0; i < 10; i++) {
//            Person person = new Person();
//            person.setAge(i + "");
//            person.setName(i + "");
//            template.save(person);
//        }q

//        Query query = new Query();


        Update update = new Update();
        update.set("name",
                "xxxxxfafweefsafease");
        update.set("age",
                "1");
        Query query = new Query();
        query.addCriteria(Criteria.where("age").is("xxxxxfafweeeeeeeeeeeeeeeeeeeeeeefsafease"));
        template.updateFirst(query, update, Person.class);

        query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "_id"));
        List<Person> list = template.find(query, Person.class);
        list.forEach(person -> System.out.println(person.getAge()));
    }
}

@lombok.Data
@Document(collection = "aaa")
class AA {
    @Id
    private String id;

    @Field
    private List<String> ids;
}

@lombok.Data
@Document(collection = "person")
class Person {
    @Id
    private ObjectId id;

    @Field
    private String name;

    @Field
    private String age;
}

@Document(collection = "data")
@lombok.Data
class Data {

    @Id
    private String id;
    @Field
    @Indexed
    private String ref1Id;
    @Field
    @Indexed
    private String data1;
    @Field
    @Indexed
    private String data2;
    @Field
    @Indexed
    private String data3;
    @Field
    @Indexed
    private String data4;
    @Field
    @Indexed
    private String data5;
    @Field
    private String data6;
    @Field
    private String data7;
    @Field
    @Indexed
    private String data8;
    @Field
    @Indexed
    private String data9;
    public Data(String id, String ref1Id, String data1, String data2, String data3, String data4, String data5,
                String data6, String data7, String data8, String data9) {
        this.id = id;
        this.ref1Id = ref1Id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
    }
}

@lombok.Data
@Document(collection = "ref1")
class Ref1 {
    @Id
    @Indexed
    private String id;
    @Field
    @Indexed
    private String ref2Id;
    @Field
    @Indexed
    private String data1;
    @Field
    @Indexed
    private String data2;
    @Field
    @Indexed
    private String data3;
    @Indexed
    @Field
    private String data4;
    @Indexed
    @Field
    private String data5;
    @Indexed
    @Field
    private String data6;
    @Field
    @Indexed
    private String data7;
    @Indexed
    @Field
    private String data8;
    @Indexed
    @Field
    private String data9;
    public Ref1(String id, String ref2Id, String data1, String data2, String data3, String data4, String data5,
                String data6, String data7, String data8, String data9) {
        this.id = id;
        this.ref2Id = ref2Id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
    }
}

@lombok.Data
@Document(collection = "ref2")
class Ref2 {
    @Id
    private String id;
    @Field
    private String ref3Id;
    @Field
    private String data1;
    @Field
    private String data2;
    @Field
    private String data3;
    @Field
    private String data4;
    @Field
    private String data5;
    @Field
    private String data6;
    @Field
    private String data7;
    @Field
    private String data8;
    @Field
    private String data9;
    public Ref2(String id, String ref3Id, String data1, String data2, String data3, String data4, String data5,
                String data6, String data7, String data8, String data9) {
        this.id = id;
        this.ref3Id = ref3Id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
    }
}

@lombok.Data
@Document(collection = "ref3")
class Ref3 {
    @Id
    private String id;
    @Field
    private String data1;
    @Field
    private String data2;
    @Field
    private String data3;
    @Field
    private String data4;
    @Field
    private String data5;
    @Field
    private String data6;
    @Field
    private String data7;
    @Field
    private String data8;
    @Field
    private String data9;
    public Ref3(String id, String data1, String data2, String data3, String data4, String data5, String data6, String
            data7, String data8, String data9) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
    }
}