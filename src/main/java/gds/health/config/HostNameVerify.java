package gds.health.config;

import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import javax.servlet.ServletException;

/** 自动信任服务端类，用于不做hostname验证
 * @author sunxingba
 * @version 1.0 $
 */
@Configuration
public class HostNameVerify {

    public HostNameVerify() {
        super();
        try {
            disableHostNameVerify();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void disableHostNameVerify() throws ServletException {
        try {
            SSLContext sc;
            // 获取 SSL 上下文
            sc = SSLContext.getInstance("SSL");
            // 创建一个空的HostnameVerifier
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            // 不就行证书链验证的信任管理器
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[]
                                                   certs, String authType) {
                }
                public void checkServerTrusted(java.security.cert.X509Certificate[]
                                                   certs, String authType) {
                }
            } };
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sc.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
