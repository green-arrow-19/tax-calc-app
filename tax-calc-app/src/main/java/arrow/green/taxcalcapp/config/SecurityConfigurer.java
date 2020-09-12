package arrow.green.taxcalcapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import arrow.green.taxcalcapp.service.CustomUserDetailsService;

/**
 * @author nakulgoyal
 *         29/08/20
 **/

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    JwtRequestFilter jwtRequestFilter;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
            .antMatchers("/", "/error", "/user/signup", "/user/signup/verified", "/user/signin", "/swagger-ui.html",
                         "/actuator/**", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/h2/**",
                         "/favicon.ico", "/").permitAll().anyRequest().authenticated().and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}


