package com.xhq.api.config;

import com.xhq.common.utils.HostHolder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {


    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                //获取登录用户
                Long userId= HostHolder.getUser();
                System.out.println("defaultconfig"+userId);
                if(userId==null){
                    return ;
                }
                template.header("user-info",userId.toString());
                System.out.println("defaultconfig2"+HostHolder.getUser());
            }
        };
    }
//
//    @Bean
//    public ItemClientFallback itemClientFallback(){
//        return new ItemClientFallback();
//    }
}
