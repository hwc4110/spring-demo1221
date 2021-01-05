package com.example.springdemo1221.tokenandsign;

import com.alibaba.fastjson.JSONObject;
import com.example.springdemo1221.entity.PersonEntity;
import com.example.springdemo1221.service.TokenSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Description: 开放的接口
 * @author huangweicheng
 * @date 2020/12/21
*/
@RestController
@RequestMapping("/token")
public class TokenSignController {

    @Autowired
    private TokenSignService tokenSignService;

    @RequestMapping(value = "openDemo",method = RequestMethod.GET)
    public List<PersonEntity> openDemo(int personId){
        return tokenSignService.getPersonList(personId);
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public JSONObject login(@NotNull String username, @NotNull String password){
        return tokenSignService.login(username,password);
    }
}
