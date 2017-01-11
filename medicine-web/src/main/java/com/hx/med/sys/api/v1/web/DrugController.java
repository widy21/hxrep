package com.hx.med.sys.api.v1.web;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.vo.NewDrugForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/med")
public class DrugController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DrugService drugService;

    @RequestMapping(value = "/good_register", method = RequestMethod.GET)
    public Object goodRegister(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute("loginUser");
        Map<String, User> model = new HashMap<String, User>();
        if(user == null){
            return "login";
        }else {
            model.put("user", user);
            return new ModelAndView("good_register",model);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/save_new_drug", method = RequestMethod.POST)
    public Object saveNewDrug(@RequestBody @FluentValid final NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = drugService.saveNewDrug(newDrugForm);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/query_drug_byno", method = RequestMethod.POST)
    public Object queryDrugByNo(@RequestBody @FluentValid final NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = drugService.queryDrugByNo(newDrugForm);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/add_drugNum", method = RequestMethod.POST)
    public Object addDrugNum(@RequestBody @FluentValid final NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = drugService.addDrugNum(newDrugForm);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/edit_drug", method = RequestMethod.POST)
    public Object editDrug(@RequestBody @FluentValid final NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = drugService.updateDrug(newDrugForm);
        return resultMap;
    }

}
