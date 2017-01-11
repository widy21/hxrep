package com.hx.med.sys.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class User {
    private Integer id;
    private String name;
    private Integer departId;
    private String userName;
    private String userPwd;
    private Date createTime;


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

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * get createTime.
     *
     * @return java.util.Date
     **/
    public Date getCreateTime() {
        if (this.createTime == null) {
            return null;
        }
        return (Date) createTime.clone();
    }

    /**
     * set createTime.
     *
     * @param createTime java.util.Date
     */
    public void setCreateTime(final Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        }else {
            this.createTime = (Date) createTime.clone();
        }
    }


}
