package com.eknows.model.bean.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zfh
 * @version 1.0
 * @date 2018/12/26 14:20
 */
public class User implements Serializable {

    private static final long serialVersionUID = 6872142563866152521L;
    private Integer id;
    private String name;
    private String password;
    private Integer role; // 默认是0, 普通用户，可登陆、注册、查看和修改个人信息； 1表示管理员，可登陆、查看和修改自己及其他人的信息
    private String address;
    private String ip;
    private Date lastLoginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
