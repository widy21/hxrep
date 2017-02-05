package com.hx.med.sys.service.impls;

import com.hx.med.common.util.PageUtil;
import com.hx.med.sys.entity.DrugSpell;
import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.dao.DrugDao;
import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.QryDrugForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

@Service
public class DrugServiceImpl implements DrugService {

    /**
     * logger.
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    DrugDao drugDao;

    @Override
    public Map saveNewDrug(NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            drugDao.insert(newDrugForm);
            drugDao.insertDrugSpell(newDrugForm);
            resultMap.put("update_flag", "success");
        }catch (Exception e){
            logger.debug("saveNewDrug error : [{}]", e.getMessage());
            resultMap.put("update_flag","false");
            throw new BusinessException("saveNewDrug error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map queryDrugByNo(NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            Drug drug = drugDao.query(newDrugForm.getDrugNoAdd());
            if(drug == null){
                resultMap.put("query_flag", "none");
            }else {
                resultMap.put("query_flag", "success");
                resultMap.put("drug", drug);
            }
        }catch (Exception e){
            resultMap.put("query_flag","false");
            logger.debug("queryDrugByNo error : [{}]", e.getMessage());
            throw new BusinessException("queryDrugByNo error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map queryDrugByCondition(QryDrugForm qryDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            int start = (qryDrugForm.getCurrentPage()-1)*qryDrugForm.getPageSize();
            qryDrugForm.setStart(start);
            List<Drug> drugs = drugDao.pageQueryByCondition(qryDrugForm);
            Integer recordNum = drugDao.pageCountQueryByCondition(qryDrugForm);
            Double totalFee = drugDao.queryTotalFeeByCondition(qryDrugForm);
            PageUtil pageUtil = new PageUtil(10, recordNum, qryDrugForm.getCurrentPage());
            resultMap.put("drugs", drugs);
            resultMap.put("recordNum", recordNum);
            resultMap.put("currentPage", qryDrugForm.getCurrentPage());
            resultMap.put("pageCount", pageUtil.getPageCount());
            resultMap.put("totalFee", totalFee);
        }catch (Exception e){
            resultMap.put("query_flag","false");
            logger.debug("queryDrugByCondition error : [{}]", e.getMessage());
            throw new BusinessException("queryDrugByCondition error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map addDrugNum(NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            drugDao.updateDrugNum(newDrugForm);
            resultMap.put("update_flag", "success");
        }catch (Exception e){
            resultMap.put("update_flag","false");
            logger.debug("addDrugNum error : [{}]", e.getMessage());
            throw new BusinessException("addDrugNum error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map updateDrug(NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            drugDao.updateDrug(newDrugForm);
            resultMap.put("update_flag", "success");
        }catch (Exception e){
            resultMap.put("update_flag","false");
            logger.debug("updateDrug error : [{}]", e.getMessage());
            throw new BusinessException("updateDrug error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map updateDrugStock(NewDrugForm[] newDrugForms) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            for (NewDrugForm drugForm : newDrugForms) {
                drugDao.updateDrugStock(drugForm);
            }
            resultMap.put("update_flag", "success");
        }catch (Exception e){
            resultMap.put("update_flag","false");
            logger.debug("updateDrugStock error : [{}]", e.getMessage());
            throw new BusinessException("updateDrugStock error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map getDrugSpellInfo() throws BusinessException {
        Map resultMap = new HashMap();
        try{
            List<DrugSpell> allDrugSpell = drugDao.queryAllDrugSpell();
            resultMap.put("allDrugSpell", allDrugSpell);
            resultMap.put("query_flag", "success");
        }catch (Exception e){
            resultMap.put("query_flag","false");
            logger.debug("getDrugSpellInfo error : [{}]", e.getMessage());
            throw new BusinessException("getDrugSpellInfo error :"+e.getMessage());
        }
        return resultMap;
    }
}
