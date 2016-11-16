// This file was auto-generated, but can be altered. It will not be overwritten.
package org.roylance.yadel;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class Main {
    public static void main(final String[] args) throws Exception {
        // handle migrations when we start
        final Server server = new Server(8080);

        String fileLocation = "webapp";
        if (!new File(fileLocation).exists()) {
            fileLocation = "src/main/webapp";
        }

        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        final ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/rest/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages","org.roylance.yadel");

        final ServletHolder staticServlet = context.addServlet(DefaultServlet.class,"/*");
        staticServlet.setInitParameter("resourceBase",fileLocation);
        staticServlet.setInitParameter("pathInfoOnly","true");

        try
        {
            server.start();
            server.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}
