package com.bdilab.dataflow.flink.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * @author wh
 * @version 1.0
 * @date 2021/10/17
 * 清理失效链接
 */
@Component
public class IdleConnectionEvictor extends Thread {

    @Autowired
    private HttpClientConnectionManager httpClientConnectionManager;

    private volatile boolean shutdown;

    public IdleConnectionEvictor() {
        super();
        super.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    httpClientConnectionManager.closeExpiredConnections();

                    httpClientConnectionManager.closeIdleConnections(30L, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    //关闭清理无效连接的线程
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}