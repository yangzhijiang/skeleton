package me.veryyoung.skeleton.web;

import me.veryyoung.skeleton.security.FreeAccess;
import me.veryyoung.skeleton.security.LoginRequired;
import me.veryyoung.skeleton.utils.ContextUtils;
import netscape.security.Privilege;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by veryyoung on 2015/4/28.
 */
public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        FreeAccess freeAccess = method.getMethodAnnotation(FreeAccess.class);
        if (freeAccess != null) {
            return true;
        }


        boolean loginRequired = AnnotationUtils.findAnnotation(method.getBean().getClass(), LoginRequired.class) != null
                || method.getMethodAnnotation(LoginRequired.class) != null;
        if (loginRequired && !checkLogin(request)) {
            String url = request.getRequestURI();
            String queryString = request.getQueryString();
            if (StringUtils.isNotEmpty(queryString)) {
                url = url + "?" + queryString;
            }

            url = new String(Base64.encodeBase64(url.getBytes()));
            url = URLEncoder.encode(url);

            //如果是ajax请求响应头会有，x-requested-with
            if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                response.setHeader("sessionStatus", "timeout");//在响应头设置session状态
                return false;
            }

            response.setStatus(401);
            String redirectUrl = "/login?redirect=" + url;

            response.sendRedirect(redirectUrl);
            return false;
        }

        return true;
    }


    public boolean checkLogin(HttpServletRequest request) {
        return ContextUtils.getSessionUtils(request).getUser() != null;
    }

}
