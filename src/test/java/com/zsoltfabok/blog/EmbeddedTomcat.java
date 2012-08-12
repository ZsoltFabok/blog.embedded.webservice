package com.zsoltfabok.blog;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class EmbeddedTomcat {
    private Tomcat tomcat;

    public void start() {
        try {
            String tempDirLocation = System.getProperty("java.io.tmpdir");
            String baseDir = FilenameUtils.concat(tempDirLocation, "tomcat");
            tomcat = new Tomcat();
            tomcat.setPort(8090);
            tomcat.setBaseDir(baseDir);
            tomcat.getHost().setAppBase(baseDir);
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    public void deploy(String appName) {
        File warFile = createWarFile(appName);
        try {
            FileUtils.deleteDirectory(new File(appName));
            FileUtils.copyFileToDirectory(warFile, new File(tomcat.getHost().getAppBase()));
            tomcat.addWebapp(tomcat.getHost(), "/" + appName, warFile.getAbsolutePath());
            FileUtils.deleteQuietly(warFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getApplicationUrl(String appName) {
        return String.format("http://%s:%d/%s", tomcat.getHost().getName(),
                tomcat.getConnector().getLocalPort(), appName);
    }

    public static File createWarFile(String appName) {
        JarArchive archive = new JarArchive();
        archive.addDirectory(new File("src/main/webapp"));
        archive.addDirectory(new File("target/classes"));
        return archive.toFile(appName + ".war");
    }
}
