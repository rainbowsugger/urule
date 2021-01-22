package com.rule.client.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 反序列化时忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化时忽略空值
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    private JsonUtils() {
    }

    /**
     * 序列化对象成JSON串
     * @param obj obj
     * @return JSON串
     */
    public static String toJSON(Object obj) {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, obj);
        } catch (IOException e) {
            log.warn("[ERROR] To JSON Failed", e);
        }
        return writer.toString();
    }

    public static byte[] toBytes(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (IOException e) {
            log.warn("[ERROR] To JSON Failed", e);
        }
        return null;
    }

    /**
     * 反序列化JSON串成对象
     * @param className className
     * @param json  json
     * @param <T> <T>
     * @return 对象
     * @throws IOException IOException
     */
    public static <T> T toObject(String json, Class<T> className) throws IOException {
        if (json == null || json.length() == 0) {
            return null;
        }
        InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return objectMapper.readValue(is, className);
    }

    public static <T> T toObject(byte[] json, Class<T> className) throws IOException {
        return objectMapper.readValue(json, className);
    }


    /**
     * 反序列化JSON串成对象
     * @param json json
     * @param typeRef typeRef
     * @param <T> <T>
     * @return 对象
     * @throws IOException IOException
     */
    public static <T> T toObject(String json, TypeReference<T> typeRef)
            throws IOException {
        InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return objectMapper.readValue(is, typeRef);
    }

    /**
     * 反序列化成Map
     * @param json json
     * @param keyClass keyClass
     * @param valueClass valueClass
     * @param <K> keyClass
     * @param <V> valueClass
     * @return Map<K, V>
     * @throws JsonProcessingException JsonProcessingException
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) throws JsonProcessingException {
        return objectMapper.readValue(json, TypeFactory.defaultInstance().constructMapType(HashMap.class, keyClass, valueClass));
    }

    /**
     * 反序列化成List
     * @param json json
     * @param elementClass elementClass
     * @param <E> elementClass
     * @return List
     * @throws JsonProcessingException JsonProcessingException
     */
    public static <E> List<E> toList(String json, Class<E> elementClass) throws JsonProcessingException {
        return objectMapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, elementClass));
    }

    public static ObjectMapper instance() {
        return objectMapper;
    }

    public static JsonNode readTree(String json) throws JsonProcessingException {
        return objectMapper.readTree(json);
    }
}