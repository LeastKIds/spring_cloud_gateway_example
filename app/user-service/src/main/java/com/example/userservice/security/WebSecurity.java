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
    public static final String ALLOWED_IP_ADDRESS = "192.168.1.19";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS);
    
    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // return http
        //     .csrf(AbstractHttpConfigurer::disable)
        //     .authorizeHttpRequests((auth) ->
        //         auth
        //             // requestMatchers("/** ") 으로 할시 default setting은 AntPathRequestMatcher로 설정됨.
        //             .requestMatchers(
        //                 // new AntPathRequestMatcher("/users/**"),
        //                 // new AntPathRequestMatcher("/h2-console/**"),
        //                 // new AntPathRequestMatcher("/heath_check/**"),
        //                 // new AntPathRequestMatcher("/user-service/**")
        //                 new AntPathRequestMatcher("/**")  
        //                 )
        //                 .access(this::hasIpAddress)     // 최신 버전의 hasIpAddress 설정법
        //             // .permitAll()
        //             // .anyRequest()
        //             // .authenticated() 
        //     )
        //     .headers((headers) ->
        //         headers
        //             .frameOptions((frame) -> frame.disable())
        //     )            
        //     .build();

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) ->
                    auth
                    // 최근에 바뀌었나? AntPathRequestMatcher를 확정 안 지어주면 에러가 나네
                        .requestMatchers(new AntPathRequestMatcher("/**"))
                            // .permiteAll()과 .access()는 같이 사용 못함.
                            // .permitAll()의 경우 모든 사용자 허용
                            // .access()의 경우 해당 사용자만 허용 이란 뜻이기에 서로 상충됨
                            .access(this::hasIpAddress)
                            // .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                // 현재로써 h2를 안 쓰기 때문에 굳이 안써도 될듯
                .headers((headers) ->
                    headers
                        .frameOptions((frame) -> frame.disable())
                )
                .build();
    }
    
    // // 새로운 authenticationManager 설정법
    // // 원래는 WebSecurityConfigurerAdapter를 상속받아 그냥 사용할 수 있었지만 
    // // 현재는 WebSecurityConfigurerAdapter 을 상속받아 사용하는것을 권하지 않기 때문에
    // // 위에서 private final AuthenticationManager authenticationManager;을 해 줘야 한다.
    // private AuthenticationFilter getAuthenticationFilter() throws Exception{
    //     AuthenticationFilter authenticationFilter = new AuthenticationFilter();
    //     authenticationFilter.setAuthenticationManager(authenticationManager);

    //     return authenticationFilter;
    // }

    // hasIpAddress 설정법
    private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(object.getRequest()));
    }


    
}
