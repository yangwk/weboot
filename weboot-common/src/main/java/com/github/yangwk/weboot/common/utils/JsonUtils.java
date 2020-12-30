package com.github.yangwk.weboot.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SuppressWarnings("deprecation")
public class JsonUtils {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		OBJECT_MAPPER.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static String objectToJson(Object value) {
		if (value == null) {
			return null;
		}
		try {
			byte[] content = OBJECT_MAPPER.writeValueAsBytes(value);
			return new String(content, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("json error", e);
		}
	}

	public static <T> T jsonToObject(String json, Class<T> valueType) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException("json error", e);
		}
	}

	public static <T> T jsonToObject(String json, TypeReference<T> valueTypeRef) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, valueTypeRef);
		} catch (Exception e) {
			throw new RuntimeException("json error", e);
		}
	}

	public static JsonNode readTree(String json) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readTree(json);
		} catch (Exception e) {
			throw new RuntimeException("json error", e);
		}
	}

	public static <T> T treeToValue(TreeNode node, Class<T> valueType) {
		if (node == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.treeToValue(node, valueType);
		} catch (Exception e) {
			throw new RuntimeException("json error", e);
		}
	}

	public static <T> List<T> treeToValueList(TreeNode node, Class<T> valueType) {
		if (node == null) {
			return null;
		}
		try {
			List<T> valueList = new ArrayList<>();
			for (int r = 0; r < node.size(); r++) {
				T value = treeToValue(node.get(r), valueType);
				valueList.add(value);
			}
			return valueList;
		} catch (Exception e) {
			throw new RuntimeException("json error", e);
		}
	}

	protected ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

}
