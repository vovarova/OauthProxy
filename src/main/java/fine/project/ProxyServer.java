package fine.project;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.function.Supplier;

public class ProxyServer {
    private String proxyPath;
    private int serverPort;
    private Supplier<String> oauthTokenSupplier;

    public ProxyServer(String proxyPath, int serverPort) {
        this.proxyPath = proxyPath;
        this.serverPort = serverPort;
    }

    public ProxyServer(String proxyPath, int serverPort, Supplier<String> oauthTokenSupplier) {
        this.proxyPath = proxyPath;
        this.serverPort = serverPort;
        this.oauthTokenSupplier = oauthTokenSupplier;
    }

    public void startServer() throws Exception {
        Server server = new Server(serverPort);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        CustomProxyServlet customProxyServlet = new CustomProxyServlet(proxyPath);

        ServletHolder servletHolder = new ServletHolder(customProxyServlet);
        context.addServlet(servletHolder,"/");
        server.start();
        server.join();
    }
}
