package com.anjoyo.meituan.domain;

public class User {
      private String username;
      private String password;
      private int type;  //1 员工  2 客户
      
	public User() {
	}
    
	public User(String uName, String pwd, int t) {
		username = uName;
		password = pwd;
		type = t;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
      
}
