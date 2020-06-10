package com.usian.service;

import com.usian.pojo.TbUser;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/9 11:43
 */
public interface SSOService {
    Boolean checkUserInfo(String checkValue, int checkFlag);

    Integer userRegister(TbUser user);

    Map userLogin(String username, String password);

    TbUser getUserByToken(String token);

    Boolean logOut(String token);
}
