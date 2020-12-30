package com.luxx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanPropertyUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target);
    }

    public static <T> T copyObj(Class<?> doClass, Class<T> voClass) {
        T voObj = null;
        try {
            voObj = voClass.newInstance();
            BeanUtils.copyProperties(doClass, voObj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return voObj;
    }

    public static <T> List<T> copyList(List<?> doList, Class<T> voClass) {
        List<T> voList = new ArrayList<>();
        try {
            T voObj = null;
            for (Object doObj : doList) {
                voObj = voClass.newInstance();
                BeanUtils.copyProperties(doObj, voObj);
                voList.add(voObj);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return voList;
    }

    public static <T> T copyPojo(Object doClass, Class<T> voClass) {
        T voObj = null;
        try {
            voObj = voClass.newInstance();
            BeanUtils.copyProperties(doClass, voObj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return voObj;
    }

}
