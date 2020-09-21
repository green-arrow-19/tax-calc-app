package arrow.green.taxcalcapp.config;

import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         12/09/20
 **/

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "email.service")
public class EmailServiceConfig {
    
    private Boolean enabled;
    private String host;
    private String scheme;
    private Integer port;
    private Map<String, String> endpoints;
    
    @PostConstruct
    public void init() {
        log.info("Email Service props :- enabled:{}, scheme:{}, host:{}, port:{}, endpoints:{}", enabled, scheme, host,
                 port, endpoints);
    }
}
