package com.example.userservice.security;

import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity{


    // 허용할 IP를 스태틱 변수로 설정
    public static final String ALLOWED_IP_ADDRESS = "172.17.0.1";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS);


    private final AuthenticationManager authenticationManager;
    
    
    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((auth) ->
                auth
                    // requestMatchers("/** ") 으로 할시 default setting은 AntPathRequestMatcher로 설정됨.
                    .requestMatchers(
                        // new AntPathRequestMatcher("/users/**"),
                        // new AntPathRequestMatcher("/h2-console/**"),
                        // new AntPathRequestMatcher("/heath_check/**"),
                        // new AntPathRequestMatcher("/user-service/**")
                        new AntPathRequestMatcher("/**")  
                        )
                        .access(this::hasIpAddress)     // 최신 버전의 hasIpAddress 설정법
                    // .permitAll()
                    // .anyRequest()
                    // .authenticated() 
            )
            .headers((headers) ->
                headers
                    .frameOptions((frame) -> frame.disable())
            )
            .addFilter(getAuthenticationFilter())
            .build();
    }
    
    // 새로운 authenticationManager 설정법
    // 원래는 WebSecurityConfigurerAdapter를 상속받아 그냥 사용할 수 있었지만 
    // 현재는 WebSecurityConfigurerAdapter 을 상속받아 사용하는것을 권하지 않기 때문에
    // 위에서 private final AuthenticationManager authenticationManager;을 해 줘야 한다.
    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager);

        return authenticationFilter;
    }

    // hasIpAddress 설정법
    private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(object.getRequest()));
    }

}
