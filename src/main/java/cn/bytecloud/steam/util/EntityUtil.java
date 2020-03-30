package cn.bytecloud.steam.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
    /**
     * entity转dto
     *
     * @param entity   字段多的实体
     * @param dtoClass 字段少的实体的class
     * @return
     * @throws IllegalAccessException
     * @author zhanlang
     */
    public static Object entityToDto(Object entity, Class dtoClass) throws IllegalAccessException,
            InstantiationException {
        Object dto = dtoClass.newInstance();
        Class<?> entityClass = entity.getClass();
        //暴力反射获取所有字段(包括私有)
        Field[] entityClassFields = entityClass.getDeclaredFields();
        Field[] dtoClassFields = dtoClass.getDeclaredFields();
        //赋值
        for (Field dtoField : dtoClassFields) {
            String dtoFieldName = dtoField.getName();
            Class<?> dtoFieldType = dtoField.getType();
            if (dtoFieldName.equals("serialVersionUID")) {
                continue;
            }
            //类型和名称相同就进行赋值
            for (Field entityField : entityClassFields) {
                if (dtoFieldName.equals(entityField.getName()) && dtoFieldType == entityField.getType()) {
                    //暴力访问
                    dtoField.setAccessible(true);
                    entityField.setAccessible(true);
                    dtoField.set(dto, entityField.get(entity));
                }
            }
        }
        return dto;
    }


    /**
     * List<entity> 转成List<dto>
     *
     * @param entityList
     * @param dtoClass
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List entityListToDtoList(List entityList, Class dtoClass) throws IllegalAccessException,
            InstantiationException {
        List dtoList = new ArrayList();
        for (Object entity : entityList) {
            Object dto = entityToDto(entity, dtoClass);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
