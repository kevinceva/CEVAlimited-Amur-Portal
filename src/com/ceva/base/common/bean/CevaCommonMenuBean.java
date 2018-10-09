package com.ceva.base.common.bean;

public class CevaCommonMenuBean {

	private int id;
	private String menuName=null;
	private String parentMenu=null;
	private String menuaction=null;
	private String actionstatus=null;
	private String applName=null;
	private String title=null;
	private String hasChild = null;
	private String isChild = null;
	
	
	
	public String getIsChild() {
		return isChild;
	}
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	} 
	public String getHasChild() {
		return hasChild;
	}
	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public String getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}
	public String getMenuaction() {
		return menuaction;
	}
	public void setMenuaction(String menuaction) {
		this.menuaction = menuaction;
	}
	public String getActionstatus() {
		return actionstatus;
	}
	public void setActionstatus(String actionstatus) {
		this.actionstatus = actionstatus;
	}
	
	public String getApplName() {
		return applName;
	}
	public void setApplName(String applName) {
		this.applName = applName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
