package com.wx.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wx
 * 2018/8/21
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinControl {

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code){

        log.info("进入auto方法");
        log.info("code={}",code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx214031a92ee408b6&secret=dbb4bb1e7e210982d31ba6568c78347e" +
                "&code="+code+"&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String resutl = restTemplate.getForObject(url,String.class);
        log.info("result:{}",resutl);
    }
}
