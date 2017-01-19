package com.hx.med.sys.dao;

import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.entity.Order;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.OrderForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {

    void insert(@Param("orderForm") OrderForm orderForm);

    /**
     * 汇总查询明细
     * @param orderForm
     * @return
     */
    List<Order> pageQueryByDate(@Param("orderForm") OrderForm orderForm);
    /**
     * 汇总查询数量
     * @param orderForm
     * @return
     */
    Integer pageCountQueryByDate(@Param("orderForm") OrderForm orderForm);
    /**
     * 普通查询明细
     * @param orderForm
     * @return
     */
    List<Order> pageNormalQueryByDate(@Param("orderForm") OrderForm orderForm);
    /**
     * 普通查询数量
     * @param orderForm
     * @return
     */
    Integer pageNormalCountQueryByDate(@Param("orderForm") OrderForm orderForm);
}
