package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.service.AccountService;
import com.tencent.wxcloudrun.utils.WeChatAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private WeChatAccessToken weChatAccessToken;
    @GetMapping("getToken")
    public String getAccessToken(){
        String token = weChatAccessToken.getToken();
        return token;
    }


}
