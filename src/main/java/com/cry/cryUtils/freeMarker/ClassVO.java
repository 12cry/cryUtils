package com.cry.cryUtils.freeMarker;

import java.util.List;

public class ClassVO {
	private String packageName;
	private String name;

	public ClassVO() {
	}
	
	public ClassVO(String packageName, String name) {
		super();
		this.packageName = packageName;
		this.name = name;
	}

	public ClassVO(String packageName, String name, List<PropertyVO> propertyVOList) {
		super();
		this.packageName = packageName;
		this.name = name;
		this.propertyVOList = propertyVOList;
	}
	
	
	private List<PropertyVO> propertyVOList;
	public List<PropertyVO> getPropertyVOList() {
		return propertyVOList;
	}
	public void setPropertyVOList(List<PropertyVO> propertyVOList) {
		this.propertyVOList = propertyVOList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
