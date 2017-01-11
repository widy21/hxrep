package com.hx.med.sys.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class Order {

    private Integer id;
    private Integer opUser;
    //应收
    private Double receivableAmount;
    //实收
    private Double paidAmount;
    //成本
    private Double costAmount;
    //毛利
    private Double grossProfit;
    //税额
    private Double tax;
    //金额
    private Double amount;
    //销售详情
    private String sellDetails;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpUser() {
        return opUser;
    }

    public void setOpUser(Integer opUser) {
        this.opUser = opUser;
    }

    public Double getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(Double receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(Double costAmount) {
        this.costAmount = costAmount;
    }

    public Double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSellDetails() {
        return sellDetails;
    }

    public void setSellDetails(String sellDetails) {
        this.sellDetails = sellDetails;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
