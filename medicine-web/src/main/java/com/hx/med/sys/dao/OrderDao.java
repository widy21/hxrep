package com.hx.med.sys.dao;

import com.hx.med.sys.entity.Drug;
import com.hx.med.sys.vo.NewDrugForm;
import com.hx.med.sys.vo.OrderForm;
import org.apache.ibatis.annotations.Param;

public interface OrderDao {

    void insert(@Param("orderForm") OrderForm orderForm);

    Drug pageQueryByDatee(@Param("orderForm") OrderForm orderForm);

}
