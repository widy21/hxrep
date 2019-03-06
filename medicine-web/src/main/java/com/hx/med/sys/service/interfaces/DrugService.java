package com.hx.med.sys.service.interfaces;

import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.vo.NewBatchDrugForm;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.QryDrugForm;

import java.util.List;
import java.util.Map;

/**
 * Created by huaxiao on 2016/12/16.
 * @author huaxiao
 * @since 1.0
 */
public interface DrugService {

    Map saveNewDrug(NewDrugForm newDrugForm) throws BusinessException;

    Map queryDrugByNo(NewDrugForm newDrugForm) throws BusinessException;

    /**
     * 分页查询药品信息
     * @param qryDrugForm
     * @return
     * @throws BusinessException
     */
    Map queryDrugByCondition(QryDrugForm qryDrugForm) throws BusinessException;

    /**
     * 单个增加库存
     * @param newDrugForm
     * @return
     * @throws BusinessException
     */
    Map addDrugNum(NewDrugForm newDrugForm) throws BusinessException;

    Map updateDrug(NewDrugForm newDrugForm) throws BusinessException;

    /**
     * 更新药品库存
     * @param newDrugForm
     * @return
     * @throws BusinessException
     */
    Map updateDrugStock(NewDrugForm[] newDrugForm) throws BusinessException;

    /**
     * 新增出库信息
     * @param newDrugForms
     */
    Map addCheckOutDrugs(NewDrugForm[] newDrugForms) throws BusinessException;

    /**
     * 查询药品拼写信息
     * @return
     * @throws BusinessException
     */
    Map getDrugSpellInfo() throws BusinessException;

    /**
     * 批量增加库存
     * @param newBatchDrugForms
     * @return
     */
    Map batchAddDrugNum(NewBatchDrugForm[] newBatchDrugForms) throws BusinessException;

    /**
     * 药品入库信息分页查询
     * @param qryDrugForm
     * @return
     */
    Map queryCheckInDrugByCondition(QryDrugForm qryDrugForm) throws BusinessException;


}
