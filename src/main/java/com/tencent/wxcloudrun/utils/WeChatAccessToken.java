package com.tencent.wxcloudrun.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.config.WxChatCache;
import com.tencent.wxcloudrun.constant.WxChatConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeChatAccessToken {

    @Value("${wx.config.appid}")
    private String appid;
    @Value("${wx.config.secret}")
    private String secret;

    //rpc
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取 accessToken （token有效期为2小时, 设置缓存, 过期并重新获取 accessToken）
     * <p>
     * 微信请求正确返回 {\"access_token\":\"37_B7bSN7N0VoqOVhf5rOk7NOHY6aMoxvE15VxNIcnD3f2kXvZkc0HOU-9rhfGZyWAoYkVfLrPzxTMhdcf86kgQeabfWSV-DH0hUYD8YMBF9vcbASzwRlEE3zJbKW2PuHJIl5Nu4BLouY4rUSFwTCBbAHAIRQ\",\"expires_in\":7200}
     * 微信请求错误返回 {"errcode":40013,"errmsg":"invalid appid"}
     * </p>
     * <P>
     *     流程：
     *     1、 判断token是否过期
     *     2、 获取 url 并拼接参数 APPID + APPSECRET
     *     3、 发起请求
     *     4、 判断请求是否成功
     *     5、 缓存accessToken + accessToken过期时间到jvm 内存中
     * </P>
     * @author wangsong
     * @date 2019年6月19日 下午5:55:11
     * @return
     */
    public String getToken() {
        if (WxChatCache.AccessToken.expiration <= System.currentTimeMillis()) {
            String url = WxChatConstant.Url.ACCESS_TOKEN_URL
                    .replace("APPID", appid)
                    .replace("APPSECRET",secret);
            //http
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            JSONObject jsonObject = JSON.parseObject(forEntity.getBody());
            Object errcode = jsonObject.get("errcode");
            if (errcode != null && "40013".equals(errcode.toString())) {
//                throw new ErrorException(RType.WX_GET_ACCESS_TOKEN_ERROR);
            }
            // 设置为1小时59分钟有效期，防止空指针异常
            WxChatCache.AccessToken.token = jsonObject.get("access_token").toString();
            WxChatCache.AccessToken.expiration = ((Integer.parseInt(jsonObject.get("expires_in").toString()) - 1) * 1000) + System.currentTimeMillis();
        }
        return WxChatCache.AccessToken.token;
    }
}
