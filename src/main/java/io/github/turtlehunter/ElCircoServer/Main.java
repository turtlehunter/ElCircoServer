package io.github.turtlehunter.ElCircoServer;

import io.github.turtlehunter.ElCircoServer.objects.Grupo;
import io.github.turtlehunter.ElCircoServer.objects.Usuario;
import org.apache.log4j.*;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends ServerResource {
    public static Server server;
    public static String serverVersion = "1.0";
    public static String clientVersion = "1.0";
    public static Logger logger;
    public static Database database;
    private static boolean run = true;
    private static SmackCcsClient firstConnection;
    private static SmackCcsClient secondConnection;
    public static SmackCcsClient activeConnection;

    public static void main(String[] args) throws Exception {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);

        FileAppender fa = new FileAppender();
        fa.setName("FileLogger");
        fa.setFile("mylog.log");
        fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fa.setThreshold(Level.DEBUG);
        fa.setAppend(true);
        fa.activateOptions();
        Logger.getRootLogger().addAppender(fa);
        logger = Logger.getRootLogger();
        database = new Database();

        System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");

        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 9080);

        // Then attach it to the local host
        component.getDefaultHost().attach("/api", HttpsServer.class);
        // Now, let's start the component!
        // Note that the HTTP server connector is also automatically started.
        component.start();

        firstConnection = new SmackCcsClient();
        secondConnection = new SmackCcsClient();
        activeConnection = firstConnection;
        boolean createdNewConnection = false;
        boolean using = true;
        boolean cleanedOld = false;
        activeConnection.connect(SmackCcsClient.YOUR_PROJECT_ID, SmackCcsClient.YOUR_API_KEY);

        while(run)
        {
            if(activeConnection.connectionDraining) {
                if(!createdNewConnection) {
                    if(using) {
                        activeConnection = secondConnection;
                        activeConnection.connect(SmackCcsClient.YOUR_PROJECT_ID, SmackCcsClient.YOUR_API_KEY);
                        using = false;
                        cleanedOld = false;
                        SmackCcsClient.toSend(activeConnection);
                    } else {
                        activeConnection = firstConnection;
                        activeConnection.connect(SmackCcsClient.YOUR_PROJECT_ID, SmackCcsClient.YOUR_API_KEY);
                        using = true;
                        cleanedOld = false;
                        SmackCcsClient.toSend(activeConnection);
                    }
                    createdNewConnection = true;
                } else {
                    createdNewConnection = false;
                }
            }
            if(!cleanedOld) {
                if(using) {
                    if(secondConnection.connection.isSocketClosed()) {
                        secondConnection = new SmackCcsClient();  //GC old one and create a new one
                        cleanedOld = true;
                    }
                } else {
                    if (firstConnection.connection.isSocketClosed()) {
                        secondConnection = new SmackCcsClient();  //GC old one and create a new one
                        cleanedOld = true;
                    }
                }
            }

        }

    }
}
