package arrow.green.taxcalcapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author nakulgoyal
 *         12/09/20
 **/

@Configuration
public class WebConfig {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Bean
    public WebClient webClient() {
        ExchangeStrategies jacksonStrategy = ExchangeStrategies.builder().codecs(config -> {
            config.defaultCodecs()
                  .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
            config.defaultCodecs()
                  .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
            config.defaultCodecs().maxInMemorySize(5 * 1024 * 1024);
        }).build();
        return WebClient.builder().exchangeStrategies(jacksonStrategy).build();
    }
}


