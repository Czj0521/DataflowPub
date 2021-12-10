package com.bdilab.dataflow.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;


/**
 * Override configurator for getting http-session.
 *
 * @author wh
 * @date 2021/12/10
 */
@WebListener
@Component
public class RequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //将所有request请求都携带上httpSession
        ((HttpServletRequest) sre.getServletRequest()).getSession();
    }

    public RequestListener() {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent arg0) {
    }
}