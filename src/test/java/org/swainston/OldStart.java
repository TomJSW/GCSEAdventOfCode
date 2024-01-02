package org.swainston;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * Separate startup class for people that want to run the examples directly. Use parameter
 * -Dcom.sun.management.jmxremote to startup JMX (and e.g. connect with jconsole).
 */
public class OldStart {

  /**
   * Main function for the Wicket Application host
   */
  public static void main(String[] args) {
    // Assigned system property - can be retrieved using System.getProperty("wicket.configuration")
    System.setProperty("wicket.configuration", "development");

    Server server = new Server();

    HttpConfiguration httpConfiguration = new HttpConfiguration();
    httpConfiguration.setSecureScheme("https");
    httpConfiguration.setSecurePort(8443);
    httpConfiguration.setOutputBufferSize(32768);

    ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(httpConfiguration));
    // Sets the port for the server to run on
    connector.setPort(8080);
    connector.setIdleTimeout(1000 * 60 * 60);

    server.addConnector(connector);

    WebAppContext webAppContext = new WebAppContext();
    webAppContext.setServer(server);
    webAppContext.setContextPath("/");
    webAppContext.setWar("src/main/webapp");

    server.setHandler(webAppContext);

    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
    server.addEventListener(mBeanContainer);
    server.addBean(mBeanContainer);

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }
  }
}
