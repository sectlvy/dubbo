package com.lkl.dcloud.extension;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.jetty.JettyContainer;

public class JettyApiContainer implements Container {

    public static final String JETTY_PORT = "dubbo.jetty.port2";
    public static final String JETTY_WEB_XML = "dubbo.jetty.webxml";
    public static final String JETTY_DIRECTORY = "dubbo.jetty.directory";
    public static final int DEFAULT_JETTY_PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(JettyContainer.class);
    public static final String CONTEXT = "/";

    private static final String DEFAULT_WEBAPP_PATH = "src/main/resources";
    SelectChannelConnector connector;

    public void start() {
        String serverPort = ConfigUtils.getProperty(JETTY_PORT);
        int port;
        if (serverPort == null || serverPort.length() == 0) {
            port = DEFAULT_JETTY_PORT;
        } else {
            port = Integer.parseInt(serverPort);
        }
        connector = new SelectChannelConnector();
        connector.setPort(port);

        Server server = new Server();
        server.addConnector(connector);
        
        
        WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, "/");
        String webXml = ConfigUtils.getProperty(JETTY_WEB_XML)==null?"src\\main\\webapp\\WEB-INF\\web.xml": ConfigUtils.getProperty(JETTY_WEB_XML);
        
        webContext.setDescriptor(webXml);
        // 设置webapp的位置

        server.setHandler(webContext);
        
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start jetty server on " + NetUtils.getLocalHost() + ":" + port + ", cause: " + e.getMessage(), e);
        }
    }

    public void stop() {
        try {
            if (connector != null) {
                connector.close();
                connector = null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

}