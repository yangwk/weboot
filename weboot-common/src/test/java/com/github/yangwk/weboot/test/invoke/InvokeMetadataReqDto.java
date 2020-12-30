package com.github.yangwk.weboot.test.invoke;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

public class InvokeMetadataReqDto {
	private String bean;
	private String method;
	private List<InvokeMetadataReqParamsDto> params;
	
	public String getBean() {
		return bean;
	}
	public void setBean(String bean) {
		this.bean = bean;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<InvokeMetadataReqParamsDto> getParams() {
		return params;
	}
	public void setParams(List<InvokeMetadataReqParamsDto> params) {
		this.params = params;
	}
	
	public Object[] paramsToRuntimeType() {
		return CollectionUtils.isEmpty(params) ? ArrayUtils.EMPTY_OBJECT_ARRAY 
				: params.stream().map(InvokeMetadataReqParamsDto::toRuntimeType).toArray();
	}
}
