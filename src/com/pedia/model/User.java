package com.pedia.model;

import java.util.Date;

public class User {
    private Integer uid;

    private String account;

    private String password;

    private String username;

    private String iconaddr;

    private Integer role;

    private Integer level;

    private Integer exp;

    private Date lastlogintime;

    
    static final private int MAXEXP[]={0,5,10,50,200,500,1000,2000,5000,10000,20000,Integer.MAX_VALUE};
    
     
    
    public void AddExp(Integer e) {
		if (getRole()==2) return;
		exp += e;
		while (exp>=MAXEXP[level])
		{
			exp-=MAXEXP[level];
			level++;
		}
		System.out.println(level);
		System.out.println(exp);
	}
    
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getIconaddr() {
        return iconaddr;
    }

    public void setIconaddr(String iconaddr) {
        this.iconaddr = iconaddr == null ? null : iconaddr.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }
}