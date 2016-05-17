package com.lihang.accounting.entitys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by LiHang on 2016/3/9.
 */
public class Payout implements Serializable{
    //主键
    private int payoutId;
    //账本ID外键
    private int accountBookId;
    //账本名称
    private String accountBookName;
    //支出类别ID外键
    private int categoryId;
    //类别名称
    private String categoryName;
    //类别路径
    private String path;
    //消费金额
    private BigDecimal amount;
    //计算方式
    private String payoutType;
    //消费人ID外键
    private String payoutUserId;
    //备注
    private String comment;
    //消费日期
    private Date payoutDate = new Date();
    //添加日期
    private Date createDate = new Date();
    //状态：0失效，1启用，默认启用
    private int state = 1;

    public int getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(int payoutId) {
        this.payoutId = payoutId;
    }

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayoutType() {
        return payoutType;
    }

    public void setPayoutType(String payoutType) {
        this.payoutType = payoutType;
    }

    public String getPayoutUserId() {
        return payoutUserId;
    }

    public void setPayoutUserId(String payoutUserId) {
        this.payoutUserId = payoutUserId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPayoutDate() {
        return payoutDate;
    }

    public void setPayoutDate(Date payoutDate) {
        this.payoutDate = payoutDate;
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

    @Override
    public String toString() {
        return "Payout{" +
                "payoutId=" + payoutId +
                ", accountBookId=" + accountBookId +
                ", accountBookName='" + accountBookName + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", path='" + path + '\'' +
                ", amount=" + amount +
                ", payoutType='" + payoutType + '\'' +
                ", payoutUserId='" + payoutUserId + '\'' +
                ", comment='" + comment + '\'' +
                ", payoutDate=" + payoutDate +
                ", createDate=" + createDate +
                ", state=" + state +
                '}';
    }
}
