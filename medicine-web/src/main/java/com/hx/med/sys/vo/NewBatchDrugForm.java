package com.hx.med.sys.vo;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
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

    public String getCheckIn_no() {
        return checkIn_no;
    }

    public void setCheckIn_no(String checkIn_no) {
        this.checkIn_no = checkIn_no;
    }

    public String getLegal_date() {
        return legal_date;
    }

    public void setLegal_date(String legal_date) {
        this.legal_date = legal_date;
    }

    public String getDrug_no() {
        return drug_no;
    }

    public void setDrug_no(String drug_no) {
        this.drug_no = drug_no;
    }

    public String getRegist_num() {
        return regist_num;
    }

    public void setRegist_num(String regist_num) {
        this.regist_num = regist_num;
    }
}
