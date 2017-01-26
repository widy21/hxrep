package com.hx.med.sys.dao;

import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.entity.DrugSpell;
import com.hx.med.sys.service.interfaces.DrugService;
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


}
