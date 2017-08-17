package com.cry.cryUtils.freeMarker;

public class PropertyVO {
	private String type;
	private String name;
	private String comment;
	
	
	public PropertyVO() {
	}
	public PropertyVO(String type, String name, String comment) {
		this.type = type;
		this.name = name;
		this.comment = comment;
	}
	public PropertyVO(String type, String name) {
		this.type = type;
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
