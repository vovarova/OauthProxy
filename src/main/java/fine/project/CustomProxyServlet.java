package fine.project;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public class CustomProxyServlet extends ProxyServlet {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEAREER = "BEARER";

    private String proxyPath;
    private Supplier<String> oauthTokenSupplier;
    private URL proxyPathURL;

    public CustomProxyServlet(String proxyPath, Supplier<String> oauthTokenSupplier) {
        this(proxyPath);
        this.oauthTokenSupplier = oauthTokenSupplier;
    }

    public CustomProxyServlet(String proxyPath) {
        this.proxyPath = proxyPath;
        try {
            proxyPathURL = new URL(proxyPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL " + proxyPath);
        }
    }

    @Override
    protected HttpClient newHttpClient() {
        return new HttpClient(new SslContextFactory());
    }


    @Override
    protected HttpClient getHttpClient() {
        return super.getHttpClient();
    }

    @Override
    public boolean validateDestination(String host, int port) {
        return true;
    }

    @Override
    public String getHostHeader() {
        return proxyPathURL.getHost();
    }

    @Override
    protected void copyRequestHeaders(HttpServletRequest clientRequest, Request proxyRequest) {
        super.copyRequestHeaders(clientRequest, proxyRequest);
        if (oauthTokenSupplier != null) {
            proxyRequest.getHeaders().add(AUTHORIZATION, BEAREER + " " + oauthTokenSupplier.get());
        }
    }

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest) {
        StringBuilder target = new StringBuilder();
        target.append(proxyPath).append(clientRequest.getServletPath());
        String query = clientRequest.getQueryString();
        if (query != null) {
            target.append("?").append(query);
        }
        return target.toString();
    }

}
