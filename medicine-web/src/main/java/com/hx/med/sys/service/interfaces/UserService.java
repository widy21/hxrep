package com.hx.med.sys.service.interfaces;

import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.vo.LoginForm;

import java.util.Map;

/**
 * Created by huaxiao on 2016/12/16.
 * @author huaxiao
 * @since 1.0
 */
public interface UserService {

    Map update() throws BusinessException;

    Map checkLoginUser(LoginForm loginForm) throws BusinessException;
}
