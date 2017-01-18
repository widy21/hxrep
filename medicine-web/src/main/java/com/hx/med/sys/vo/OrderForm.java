package com.hx.med.sys.vo;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
public class OrderForm {

    private Integer id;
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
    //扣税金额
    private Double reduceTaxAmount;
    //销售详情
    private String sellDetails;

    private String qryStartDate;

    private String qryEndDate;

    private Integer currentPage;

    private Integer start;

    private static final Integer pageSize = 10;

    private Integer opUser;

    public static Integer getPageSize() {
        return pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Double getReduceTaxAmount() {
        return reduceTaxAmount;
    }

    public void setReduceTaxAmount(Double reduceTaxAmount) {
        this.reduceTaxAmount = reduceTaxAmount;
    }

    public Integer getOpUser() {
        return opUser;
    }

    public void setOpUser(Integer opUser) {
        this.opUser = opUser;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQryStartDate() {
        return qryStartDate;
    }

    public void setQryStartDate(String qryStartDate) {
        this.qryStartDate = qryStartDate;
    }

    public String getQryEndDate() {
        return qryEndDate;
    }

    public void setQryEndDate(String qryEndDate) {
        this.qryEndDate = qryEndDate;
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

    public String getSellDetails() {
        return sellDetails;
    }

    public void setSellDetails(String sellDetails) {
        this.sellDetails = sellDetails;
    }
}
