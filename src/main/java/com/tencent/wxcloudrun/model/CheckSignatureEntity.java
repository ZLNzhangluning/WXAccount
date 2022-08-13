package com.tencent.wxcloudrun.model;

import lombok.Data;

@Data
public class CheckSignatureEntity {

    //签名
    public String signature;
    //时间戳
    public String timestamp;
    //随机数
    public String nonce;
    //随机字符串
    public String echostr;

}
