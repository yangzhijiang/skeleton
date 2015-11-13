package me.veryyoung.skeleton.web;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 * <p/>
 * Created by veryyoung on 2015/3/12.
 */

@WebListener
public class ServletListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContextEvent.getServletContext())
                .getAutowireCapableBeanFactory()
                .autowireBean(this);

        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute("appName", "Skeleton");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
