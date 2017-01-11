package com.hx.med.sys.service.impls;

import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.dao.DrugDao;
import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.vo.NewDrugForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

@Service
public class DrugServiceImpl implements DrugService {
    @Autowired
    DrugDao drugDao;

    @Override
    public Map saveNewDrug(NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            drugDao.insert(newDrugForm);
            resultMap.put("update_flag", "success");
        }catch (Exception e){
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
            throw new BusinessException("queryDrugByNo error :"+e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map queryDrugByPage(NewDrugForm newDrugForm) throws BusinessException {
        return null;
    }

    @Override
    public Map addDrugNum(NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            drugDao.updateDrugNum(newDrugForm);
            resultMap.put("update_flag", "success");
        }catch (Exception e){
            resultMap.put("update_flag","false");
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
            throw new BusinessException("updateDrugStock error :"+e.getMessage());
        }
        return resultMap;
    }
}
