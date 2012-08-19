package com.zsoltfabok.blog;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class EmbeddedJetty {
    private Server jetty;

    public void start(String appName) {
        try {
            jetty = new Server(8090);
            WebAppContext context = new WebAppContext();
            context.setContextPath("/" + appName);
            context.setWar("src/main/webapp");
            context.setServer(jetty);
            jetty.addHandler(context);
            jetty.start();
            jetty.setStopAtShutdown(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            jetty.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getApplicationUrl(String appName) {
        return String.format("http://%s:%d/%s",
                "localhost",
                jetty.getConnectors()[0].getPort(), appName);
    }
}
