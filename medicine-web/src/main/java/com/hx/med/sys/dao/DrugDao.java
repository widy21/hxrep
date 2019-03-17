package com.hx.med.sys.dao;

import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.entity.DrugSpell;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.vo.NewBatchDrugForm;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.QryDrugForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugDao {

    void insert(@Param("newDrugForm") NewDrugForm newDrugForm);

    void insertDrugSpell(@Param("newDrugForm") NewDrugForm newDrugForm);

    Drug query(@Param("drugNo") String drugNo);

    List<DrugSpell> queryAllDrugSpell();

    void updateDrugNum(@Param("drugForm") NewDrugForm drugForm);

    void updateDrug(@Param("drugForm") NewDrugForm drugForm);

    void updateDrugStock(@Param("drugForm")NewDrugForm newDrugForm);

    List<Drug> pageQueryByCondition(@Param("qryDrugForm")QryDrugForm qryDrugForm);

    Integer pageCountQueryByCondition(@Param("qryDrugForm")QryDrugForm qryDrugForm);

    Double queryTotalFeeByCondition(@Param("qryDrugForm")QryDrugForm qryDrugForm);

    void batchAddDrugNum(@Param("newBatchDrugForm")NewBatchDrugForm newBatchDrugForms);

    /**
     * 药品信息插入入库信息表
     * @param newBatchDrugForm
     */
    void checkInDrug(@Param("newBatchDrugForm")NewBatchDrugForm newBatchDrugForm);

    /**
     * 根据条件查询入库信息
     * @param qryDrugForm
     * @return
     */
    List<Drug> pageQueryCheckInDrugByCondition(@Param("qryDrugForm")QryDrugForm qryDrugForm);

    /**
     * 根据条件查询入库信息行数
     * @param qryDrugForm
     * @return
     */
    Integer pageCheckInDrugCountQueryByCondition(@Param("qryDrugForm")QryDrugForm qryDrugForm);

    /**
     * 新增出库信息
     * @param drugForm
     */
    void addCheckOutDrug(@Param("drugForm")NewDrugForm drugForm);
}
