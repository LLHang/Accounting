package com.lihang.accounting.entitys;

import java.util.Date;

/**
 * Created by LiHang on 2016/2/29.
 */
public class AccountBook {
    //主键
    private int accountBookId;
    //用户名称
    private String accountBookName;
    //添加的日期
    private Date createDate = new Date();
    //状态：0失效，1启用，默认启用
    private int state = 1;
    //是否默认账本：0否，1是

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    private int isDefault;

    public int getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(int accountBookId) {
        this.accountBookId = accountBookId;
    }

    public String getAccountBookName() {
        return accountBookName;
    }

    public void setAccountBookName(String accountBookName) {
        this.accountBookName = accountBookName;
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
