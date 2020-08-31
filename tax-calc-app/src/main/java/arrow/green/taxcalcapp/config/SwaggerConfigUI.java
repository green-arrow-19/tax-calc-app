package arrow.green.taxcalcapp.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Configuration
@EnableSwagger2
public class SwaggerConfigUI {
    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      //				.paths(PathSelectors.ant("/user/*"))
                                                      .apis(RequestHandlerSelectors
                                                                    .basePackage("arrow.green.taxcalcapp")).build()
                                                      .apiInfo(apiDetails());
    }
    
    public ApiInfo apiDetails() {
        return new ApiInfo("Tax Calc App", "Tac Calc App", "1.0", "Free to use",
                           new springfox.documentation.service.Contact("Nakul Goyal", "http://github.com/nakulGoyal19",
                                                                       "..."), "...", "...", Collections.emptyList());
    }
}


