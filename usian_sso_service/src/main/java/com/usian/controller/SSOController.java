package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/9 11:42
 */
@RestController
@RequestMapping("/service/sso")
public class SSOController {
    @Autowired
    private SSOService ssoService;

    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public Boolean checkUserInfo(@PathVariable String checkValue,@PathVariable int checkFlag){
        return ssoService.checkUserInfo(checkValue,checkFlag);
    }

    @RequestMapping("/userRegister")
    public Integer userRegister(@RequestBody TbUser user){
        return  ssoService.userRegister(user);
    }

    @RequestMapping("/userLogin")
    public Map userLogin(@RequestParam String username, @RequestParam String password){
        return ssoService.userLogin(username, password);
    }

    @RequestMapping("/getUserByToken/{token}")
    public  TbUser getUserByToken(@PathVariable String token){
        return  ssoService.getUserByToken(token);
    }

    @RequestMapping("/logOut")
    public  Boolean logOut(String token){
        return  ssoService.logOut(token);
    }
}
