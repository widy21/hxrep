package com.hx.med.sys.api.v1.web;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.service.interfaces.OrderService;
import com.hx.med.sys.service.interfaces.UserService;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.OrderForm;
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
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/show_order_checkout", method = RequestMethod.GET)
    public Object showOrderCheckout(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute("loginUser");
        Map<String, Object> model = new HashMap<String, Object>();
        if(user == null){
            return "login";
        }else {
            List<User> allUsers = userService.getAllUsers();
            model.put("allUsers", allUsers);
            model.put("user", user);
            return new ModelAndView("order_day_check",model);
        }
    }

    @RequestMapping(value = "/show_order_msg", method = RequestMethod.GET)
    public Object showOrderMsg(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute("loginUser");
        Map<String, Object> model = new HashMap<String, Object>();
        if(user == null){
            return "login";
        }else {
            List<User> allUsers = userService.getAllUsers();
            model.put("allUsers", allUsers);
            model.put("user", user);
            return new ModelAndView("order_msg",model);
        }
    }

    /**
     * 订单入库
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/order_checkout", method = RequestMethod.POST)
    public Object orderCheckout(@RequestBody @FluentValid final OrderForm orderForm) throws BusinessException {
        Map resultMap = orderService.generateOrder(orderForm);
        return resultMap;
    }

    /**
     * 订单查询
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/order_qry", method = RequestMethod.GET)
    public Object orderQry(@RequestBody @FluentValid final OrderForm orderForm) throws BusinessException {
        Map resultMap = orderService.queryOrderByDate(orderForm);
        return resultMap;
    }

}
