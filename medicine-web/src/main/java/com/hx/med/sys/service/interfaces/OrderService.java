package com.hx.med.sys.service.interfaces;

import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.OrderForm;

import java.util.Map;

/**
 * Created by huaxiao on 2016/12/16.
 * @author huaxiao
 * @since 1.0
 */
public interface OrderService {

    Map generateOrder(OrderForm orderForm) throws BusinessException;

    /**
     * 根据日期查询订单信息
     * @param orderForm
     * @return
     * @throws BusinessException
     */
    Map queryOrderByDate(OrderForm orderForm) throws BusinessException;

}
