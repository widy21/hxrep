package com.hx.med.sys.api.v1.web;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.service.interfaces.UserService;
import com.hx.med.sys.vo.NewBatchDrugForm;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.QryDrugForm;
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
import java.util.List;
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
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/good_register", method = RequestMethod.GET)
    public Object goodRegister(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute("loginUser");
        Map<String, Object> model = new HashMap<String, Object>();
        if(user == null){
            return "login";
        }else {
//            List<User> allUsers = userService.getAllUsers();
//            model.put("allUsers", allUsers);
            model.put("user", user);
            return new ModelAndView("good_register",model);
        }
    }

    @RequestMapping(value = "/good_register_new", method = RequestMethod.GET)
    public Object goodRegisterNew(HttpServletRequest request) throws BusinessException {
        return new ModelAndView("good_register_new",null);
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
    @RequestMapping(value = "/batch_add_drugNum", method = RequestMethod.POST)
    public Object batchAddDrugNum(@RequestBody @FluentValid final NewBatchDrugForm[] newBatchDrugForms) throws BusinessException {
        Map resultMap = drugService.batchAddDrugNum(newBatchDrugForms);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/edit_drug", method = RequestMethod.POST)
    public Object editDrug(@RequestBody @FluentValid final NewDrugForm newDrugForm) throws BusinessException {
        Map resultMap = drugService.updateDrug(newDrugForm);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/get_drug_spell_info", method = RequestMethod.POST)
    public Object getDrugSpellInfo() throws BusinessException {
        Map resultMap = drugService.getDrugSpellInfo();
        return resultMap;
    }

    /**
     * 药品分页查询
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/drug_page_qry", method = RequestMethod.POST)
    public Object orderQry(@RequestBody @FluentValid final QryDrugForm qryDrugForm) throws BusinessException {
        Map resultMap = drugService.queryDrugByCondition(qryDrugForm);
        return resultMap;
    }

    @RequestMapping(value = "/show_drug_msg", method = RequestMethod.GET)
    public Object showDrugMsg(HttpServletRequest request) throws BusinessException {
        Map<String, Object> model = new HashMap<String, Object>();
        List<User> allUsers = userService.getAllUsers();
        model.put("allUsers", allUsers);
        return new ModelAndView("drug_msg",model);
    }

    @RequestMapping(value = "/show_menu", method = RequestMethod.GET)
    public Object showMenu(HttpServletRequest request) throws BusinessException {
        Map<String, Object> model = new HashMap<String, Object>();
//        List<User> allUsers = userService.getAllUsers();
//        model.put("allUsers", allUsers);
        return new ModelAndView("menu",model);
    }

}
