package com.hx.med.sys.vo;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
public class NewDrugForm {

    private String drugNoAdd;
    private String drugOriginalAdd;
    private String drugNameAdd;
    private String drugSpeAdd;
    private Double price;
    private Double drugSellPriceAdd;
    private Integer number;
    private Integer start;
    private Integer length;

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

    public Double getDrugSellPriceAdd() {
        return drugSellPriceAdd;
    }

    public void setDrugSellPriceAdd(Double drugSellPriceAdd) {
        this.drugSellPriceAdd = drugSellPriceAdd;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDrugNoAdd() {
        return drugNoAdd;
    }

    public void setDrugNoAdd(String drugNoAdd) {
        this.drugNoAdd = drugNoAdd;
    }

    public String getDrugOriginalAdd() {
        return drugOriginalAdd;
    }

    public void setDrugOriginalAdd(String drugOriginalAdd) {
        this.drugOriginalAdd = drugOriginalAdd;
    }

    public String getDrugNameAdd() {
        return drugNameAdd;
    }

    public void setDrugNameAdd(String drugNameAdd) {
        this.drugNameAdd = drugNameAdd;
    }

    public String getDrugSpeAdd() {
        return drugSpeAdd;
    }

    public void setDrugSpeAdd(String drugSpeAdd) {
        this.drugSpeAdd = drugSpeAdd;
    }
}
