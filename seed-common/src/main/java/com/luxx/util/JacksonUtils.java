package com.luxx.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class JacksonUtils {

    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

    static {
        JACKSON_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JACKSON_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JACKSON_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        JACKSON_MAPPER.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JACKSON_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static Map<String, Object> readValueAsMap(String value) {
        try {
            return JACKSON_MAPPER.readValue(value, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static JsonNode readTree(String jsonStr) {
        try {
            return JACKSON_MAPPER.readTree(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> readValueAsStringMap(String value) {
        try {
            return JACKSON_MAPPER.readValue(value, new TypeReference<HashMap<String, String>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueAsBean(JsonNode jsonNode, Class<T> clazz) {
        try {
            return JACKSON_MAPPER.readValue(jsonNode.traverse(), clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueAsBean(String value, Class<T> clazz) {
        try {
            return JACKSON_MAPPER.readValue(value, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static final String writeValueAsString(Object obj) {
        try {
            return JACKSON_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Map<String, Object> convertValue(Object obj) {
        return JACKSON_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> T readValue(JsonNode jsonNode, Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            JavaType javaType = JACKSON_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return JACKSON_MAPPER.readValue(jsonNode.traverse(), javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <type> type jsonToObjectList(String json,
                                               Class<?> collectionClass, Class<?>... elementClass) {
        type obj = null;
        JavaType javaType = JACKSON_MAPPER.getTypeFactory().constructParametricType(
                collectionClass, elementClass);
        try {
            obj = JACKSON_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }

    public static <type> type jsonToObjectHashMap(String json,
                                                  Class<?> keyClass, Class<?> valueClass) {
        type obj = null;
        JavaType javaType = JACKSON_MAPPER.getTypeFactory().constructParametricType(HashMap.class, keyClass, valueClass);
        try {
            obj = JACKSON_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }
}
