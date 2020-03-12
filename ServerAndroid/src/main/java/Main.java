import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.MultipartConfigElement;

public class Main {
    public static void main(String []args) throws Exception{

        ServletUpload frontend=new ServletUpload();
        Server server=new Server(8080);
        ServletContextHandler context= new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        ServletHolder serv=new ServletHolder(frontend);

        context.addServlet(new ServletHolder(frontend),"/upload");
        server.start();

        java.util.logging.Logger.getGlobal().info("Server started");

        server.join();
    }
}
