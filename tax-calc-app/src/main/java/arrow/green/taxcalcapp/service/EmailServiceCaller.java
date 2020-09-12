package arrow.green.taxcalcapp.service;

import java.net.URI;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import arrow.green.taxcalcapp.config.EmailServiceConfig;
import arrow.green.taxcalcapp.util.WebUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         12/09/20
 **/

@Slf4j
@Service
public class EmailServiceCaller {
    
    @Autowired
    private WebClient client;
    
    @Autowired
    private EmailServiceConfig config;
    
    public String sendMail(URI uri, HttpServletRequest httpServletRequest, String request) {
        final String responseMsg = this.client.get().uri(uri)
                                              .headers(WebUtils.getHeadersConsumer(httpServletRequest))
                                              .retrieve()
                                              .onStatus(WebUtils.anyOtherThan2xx, response -> {
                                                  return response.bodyToMono(String.class).map(err -> {
                                                      JSONObject res = new JSONObject(err);
                                                      String msg = res.has("message") ? res.getString("message") : err;
                                                      log.error("Received error response while calling email service "
                                                                        + "Status code {}. Status Response {}",
                                                                response.statusCode(), msg);
                                                      return new RuntimeException(msg);
                                                  });
                                              }).bodyToMono(String.class).block();
        return responseMsg;
    }
    
    public URI getUri(String endpoint, Map<String, String> params) {
        return WebUtils
                .buildURI(config.getHost(), config.getScheme(), config.getPort(), config.getEndpoints().get(endpoint),
                          params);
    }
    
}


