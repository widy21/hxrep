package com.hx.med.sys.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
@Getter
@Setter
public class NewDrugForm {

    private String drugNoAdd;
    private String drugOriginalAdd;
    private String drugNameAdd;
    private String drugSpeAdd;
    private Double price;
    private Double drugSellPriceAdd;
    private Double checkOutGrossProfit;
    private Integer number;
    private Integer start;
    private Integer length;

}
