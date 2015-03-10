package com.operation.fileprocess.mapper;

public class WordAnalyzerMapper {
	Long value;
	String key;
	public WordAnalyzerMapper(String key,Long value) {
		this.value = value;
		this.key = key;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
