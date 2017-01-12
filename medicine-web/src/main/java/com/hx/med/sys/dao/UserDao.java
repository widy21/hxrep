package com.hx.med.sys.dao;

import com.hx.med.sys.entity.User;
import com.hx.med.sys.vo.LoginForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    void update(@Param("name") String name);

    User query(@Param("loginForm")LoginForm loginForm);

    List<User> getAllUsers();
}
