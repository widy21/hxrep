package com.hx.med.sys.dao;

import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.entity.Order;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.OrderForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {

    void insert(@Param("orderForm") OrderForm orderForm);

    List<Order> pageQueryByDate(@Param("orderForm") OrderForm orderForm);
}
