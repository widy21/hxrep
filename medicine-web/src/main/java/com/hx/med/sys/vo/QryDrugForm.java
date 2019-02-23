package com.hx.med.sys.vo;

import lombok.*;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */

@Data
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
    private String checkInNo;
    private String legalDate;
    private Integer start;
    @Getter
    private static final Integer pageSize = 10;
    private Integer currentPage;


}
