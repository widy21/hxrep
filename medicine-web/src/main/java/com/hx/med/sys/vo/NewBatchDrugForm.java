package com.hx.med.sys.vo;

import lombok.Data;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
@Data
public class NewBatchDrugForm {
    /**
     * 药品编号
     */
    private String drug_no;
    /**
     * 进货数量
     */
    private String regist_num;
    /**
     * 批次
     */
    private String checkIn_no;
    /**
     * 有效期
     */
    private String legal_date;

    /**
     * 进货金额
     * @return
     */
    private Double checkIn_amount;
}
