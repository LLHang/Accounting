package com.lihang.accounting.entitys;

import java.util.Date;

/**
 * Created by LiHang on 2016/2/29.
 */
public class User {
    //主键
    private int userId;
    //用户名称
    private String userName;
    //添加的日期
    private Date createDate = new Date();
    //状态：0失效，1启用，默认启用
    private int state = 1;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
