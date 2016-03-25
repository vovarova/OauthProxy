package fine.project;

import org.eclipse.jetty.util.log.Log;

/**
 * Created by Vroman on 22.03.2016.
 */
public class App {
    public static void main(String[] args) throws Exception {
        Log.getLog().setDebugEnabled(true);
        ProxyServer proxyServer = new ProxyServer("https://google.com",7777);
        proxyServer.startServer();
    }
}
