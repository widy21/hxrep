package com.hx.med.sys.vo;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
public class QryDrugForm {

    private String drugNo;
    private String drugName;
    private String drugSpe;
    private String drugOriginal;
    private Double purchasePrice;
    private Double drugSellPrice;
    private Integer number;
    private Integer length;
    private String qryStartDate;
    private String qryEndDate;
    private Integer start;
    private static final Integer pageSize = 10;
    private Integer currentPage;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public static Integer getPageSize() {
        return pageSize;
    }

    public String getDrugNo() {
        return drugNo;
    }

    public void setDrugNo(String drugNo) {
        this.drugNo = drugNo;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugSpe() {
        return drugSpe;
    }

    public void setDrugSpe(String drugSpe) {
        this.drugSpe = drugSpe;
    }

    public String getDrugOriginal() {
        return drugOriginal;
    }

    public void setDrugOriginal(String drugOriginal) {
        this.drugOriginal = drugOriginal;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getDrugSellPrice() {
        return drugSellPrice;
    }

    public void setDrugSellPrice(Double drugSellPrice) {
        this.drugSellPrice = drugSellPrice;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
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
}
