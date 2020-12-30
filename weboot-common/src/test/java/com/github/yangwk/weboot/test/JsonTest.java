package com.github.yangwk.weboot.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.yangwk.weboot.common.utils.JsonUtils;

public class JsonTest {

	public static class Bean {
		private String aud;
		private String exp;
		String noSetterAndGetter;

		public String getAud() {
			return aud;
		}

		public void setAud(String aud) {
			this.aud = aud;
		}

		public String getExp() {
			return exp;
		}

		public void setExp(String exp) {
			this.exp = exp;
		}
	}
	
	public static class Open {
		private String name;
		private String something;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSomething() {
			return something;
		}
		public void setSomething(String something) {
			this.something = something;
		}
		
	}
	

	@Test
	public void json() {
		String json = "{\r\n" +
				"  \"aud\": \"you\",\r\n" +
				"  \"exp\": 1300819380,\r\n" +
				"  \"user\": {\r\n" +
				"    \"name\": \"Joe\",\r\n" +
				"    \"address\": {\r\n" +
				"        \"street\": \"1234 Main Street\",\r\n" +
				"        \"city\": \"Anytown\",\r\n" +
				"        \"state\": \"CA\",\r\n" +
				"        \"postalCode\": 94401\r\n" +
				"    }\r\n" +
				"  }\r\n" +
				"}";

		System.out.println(json);
		
		try {
			Bean bean1 = JsonUtils.jsonToObject(json, new TypeReference<Bean>() {
			});
			Bean bean2 = JsonUtils.jsonToObject(json, Bean.class);
			System.out.println(ToStringBuilder.reflectionToString(bean1));
			System.out.println(ToStringBuilder.reflectionToString(bean2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void many() {
		String json = "{\r\n" + 
				"    \"aud\": \"you\",\r\n" + 
				"    \"exp\": 1300819380,\r\n" + 
				"    \"user\": [22, 33, 44],\r\n" + 
				"    \"other\": [{\r\n" + 
				"            \"name\": \"a\",\r\n" + 
				"            \"something\": \"aaaaaaa\",\r\n" + 
				"            \"ss\": \"6666\"\r\n" + 
				"        }, {\r\n" + 
				"            \"name\": \"b\",\r\n" + 
				"            \"something\": \"bbbbbbb\",\r\n" + 
				"            \"ss\": \"2222\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";

		System.out.println(json);
		JsonNode jsonNode = JsonUtils.readTree(json);
		System.out.println(jsonNode.get("aud").asText());
		JsonNode user = jsonNode.get("user");
		for(int r=0; r<user.size(); r++) {
			System.out.println(user.get(r).asInt());
		}
		JsonNode other = jsonNode.get("other");
		List<Open> valueList = JsonUtils.treeToValueList(other, Open.class);
		valueList.forEach(v -> {
			System.out.println(ToStringBuilder.reflectionToString(v));
		});
	}
	
	
	@Test
	public void copy() {
		Map<String, String> srcMap = new HashMap<>();
		srcMap.put("name", "hello");
		srcMap.put("money", "56.32");
		Map<String, String> targetMap = JsonUtils.jsonToObject( JsonUtils.objectToJson(srcMap) , new TypeReference<Map<String,String>>() {
		});
		System.out.println(targetMap);
	}

}
