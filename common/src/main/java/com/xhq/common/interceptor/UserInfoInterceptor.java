package com.xhq.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xhq.common.utils.HostHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的用户信息
        String userInfo = request.getHeader("user-info");
        // 2.判断是否为空
        if (StrUtil.isNotBlank(userInfo)) {
            // 不为空，保存到ThreadLocal
            System.out.println("请求头:："+userInfo);

            HostHolder.setUser(Long.valueOf(userInfo));
            System.out.println("请求头2:："+HostHolder.getUser());
        }
        // 3.放行
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        HostHolder.clear();
    }
}
