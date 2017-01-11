package com.hx.med.sys.service.impls;

import com.hx.med.sys.entity.User;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.service.interfaces.UserService;
import com.hx.med.sys.dao.UserDao;
import com.hx.med.sys.vo.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huaxiao on 2016/12/20.
 * @author huaxiao
 * @since 1.6
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public Map update() throws BusinessException {
        userDao.update("aaaaaaaaaaaaa");
        Map resultMap = new HashMap();
        resultMap.put("update_flag","success");
        return resultMap;
    }

    @Override
    public Map checkLoginUser(LoginForm loginForm) throws BusinessException {
        Map ret =  new HashMap();
        User user = userDao.query(loginForm);
        if(user != null){
            ret.put("loginUser",user);
            ret.put("loginFlag","true");
        }else {
            ret.put("loginFlag","false");

        }
        return ret;
    }
}
