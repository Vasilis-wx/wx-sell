package com.wx.control;

import com.wx.enums.ResultEnum;
import com.wx.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

/**
 * Created by wx
 * 2018/8/21
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatControl {

    @Autowired
    private WxMpService wxMpService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){

        String url = "http://xvmswj.natappfree.cc/sell/wechat/userInfo";
        String redictUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】result={}",redictUrl);
        return "redirect:"+redictUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl){
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            String openid = wxMpOAuth2AccessToken.getOpenId();
            return "redirect:"+returnUrl+"?openid="+openid;
        } catch (WxErrorException e) {
            log.error("【微信网络授权】{}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR,e.getError().getErrorMsg());
        }
    }
}
