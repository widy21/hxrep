package com.hx.med.sys.entity;

import java.util.Date;

public class Drug {
    private Integer id;
    private String drugNo;
    private String drugName;
    private String specification;
    private String origin;
    private Double purchasePrice;
    private Double sellingPrice;
    private Integer number;
    private Date createTime;

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugNo() {
        return drugNo;
    }

    public void setDrugNo(String drugNo) {
        this.drugNo = drugNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
