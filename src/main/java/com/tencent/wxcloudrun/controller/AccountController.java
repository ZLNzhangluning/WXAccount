package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.model.CheckSignatureEntity;
import com.tencent.wxcloudrun.service.AccountService;
import com.tencent.wxcloudrun.utils.WeChatAccessToken;
import com.tencent.wxcloudrun.utils.WeixinCheckoutUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weChat")
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

    /**
     * 公众号开发校验签名
     */
    @GetMapping("checkSignature")
    public String checkSignature(String signature,String timestamp,String nonce,String echostr){
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signature != null && WeixinCheckoutUtil.checkSignature(signature, timestamp, nonce,echostr)) {
            return echostr;
        }
        return null;
    }


}
