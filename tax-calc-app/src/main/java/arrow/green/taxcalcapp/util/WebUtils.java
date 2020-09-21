package arrow.green.taxcalcapp.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         12/09/20
 **/

@Slf4j
public class WebUtils {
    public static URI buildURI(String host, String scheme, Integer port, String path, Map<String, String> params) {
        URIBuilder builder = new URIBuilder();
        builder.setHost(host);
        if (Objects.nonNull(port)) {
            builder.setPort(port);
        }
        builder.setScheme(scheme);
        builder.setPath(path);
        if (Objects.nonNull(params)) {
            params.forEach(builder::setParameter);
        }
        try {
            return builder.build();
        } catch (URISyntaxException e) {
            log.error("Unable to create URI fo given params: [host:{}, scheme: {}, port: {}]", host, scheme, port);
            return null;
        }
    }
    
    public static Predicate<HttpStatus> anyOtherThan2xx = status -> !status.is2xxSuccessful();
    
    public static Consumer<HttpHeaders> getHeadersConsumer(HttpServletRequest httpServletRequest) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        return httpHeaders -> {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String header = httpServletRequest.getHeader(key);
                httpHeaders.add(key, header);
            }
        };
    }
}


