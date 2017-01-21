package com.hx.med.sys.service.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hx.med.common.util.PageUtil;
import com.hx.med.sys.dao.DrugDao;
import com.hx.med.sys.dao.OrderDao;
import com.hx.med.sys.dao.UserDao;
import com.hx.med.sys.entity.Order;
import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.service.interfaces.DrugService;
import com.hx.med.sys.service.interfaces.OrderService;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.OrderForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
@Service
public class OrderServiceImpl implements OrderService{

    /**
     * logger.
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    OrderDao orderDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private DrugService drugService;



    @Override
    public Map generateOrder(OrderForm orderForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            orderDao.insert(orderForm);
            String sellDetails = orderForm.getSellDetails();
            NewDrugForm[] newDrugForms = generateDrugFormArray(sellDetails);
            if(newDrugForms.length>0){
                drugService.updateDrugStock(newDrugForms);
            }
            resultMap.put("opt_flag", "success");
        }catch (Exception e){
            resultMap.put("opt_flag","false");
            throw new BusinessException("generateOrder error :"+e.getMessage());
        }
        return resultMap;
    }

    private NewDrugForm[] generateDrugFormArray(String sellDetails) {
        //[{"drugNo":"aaa","sellNum":"11","costAmount":"27.61","sellAmount":"137.50"}]
        if(!StringUtils.isBlank(sellDetails)){
            List<HashMap> list = JSON.parseArray(sellDetails, HashMap.class);
            NewDrugForm[] drugArray = new NewDrugForm[list.size()];
            for(int i=0;i<list.size();i++){
                NewDrugForm drugForm = new NewDrugForm();
                drugForm.setDrugNoAdd((String) list.get(i).get("drugNo"));
                drugForm.setNumber(Integer.parseInt((String) list.get(i).get("sellNum")));
                drugArray[i] = drugForm;
            }
            return drugArray;
        }else{
            return new NewDrugForm[0];
        }
    }

    @Override
    public Map queryOrderByDate(OrderForm orderForm) throws BusinessException {
        Map resultMap = new HashMap();
        try{
            int start = (orderForm.getCurrentPage()-1)*orderForm.getPageSize();
            orderForm.setStart(start);
            List<Order> orders = orderDao.pageQueryByDate(orderForm);
            for (Order order : orders) {
                User user  = userDao.getUserById(order.getOpUser());
                if(user != null){
                    order.setOpUserName(user.getName());
                }
            }
            Integer recordNum = orderDao.pageCountQueryByDate(orderForm);
            //统计查询
            Order totalOrder = orderDao.totalQueryByDate(orderForm);
            if(totalOrder != null){
                User user  = userDao.getUserById(totalOrder.getOpUser());
                if(user != null){
                    totalOrder.setOpUserName(user.getName());
                }
            }

            PageUtil pageUtil = new PageUtil(10, recordNum, orderForm.getCurrentPage());
            resultMap.put("orders", orders);
            resultMap.put("totalOrder", totalOrder);
            resultMap.put("recordNum", recordNum);
            resultMap.put("currentPage", orderForm.getCurrentPage());
            resultMap.put("pageCount", pageUtil.getPageCount());
            resultMap.put("opt_flag", "success");
        }catch (Exception e){
            logger.debug("queryOrderByDate error :{}",e.getMessage());
            resultMap.put("opt_flag","false");
            e.printStackTrace();
            throw new BusinessException("queryOrderByDate error :"+e.getMessage());
        }
        return resultMap;
    }
}
