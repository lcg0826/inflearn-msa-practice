package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity{

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectPostProcessor<Object> objectPostProcessor;

    private Environment env;

    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, ObjectPostProcessor<Object> objectPostProcessor) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectPostProcessor = objectPostProcessor;
    }
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        // AuthenticationFileter 추가 전
        http.csrf((csrf) -> csrf.disable());
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/users/**", "/**").permitAll());
//        http.headers((headers) -> headers.disable());
//        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));
//        return http.build();

        // AuthenticationFileter 추가 후
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/users/**", "/**")
                )
                .authorizeRequests().requestMatchers("/**")
                .access("hasIpAddress('"+"192.168.0.5"+"')")
                .and()
                .addFilter(getAuthenticationFilter())
        ;

//        http.authorizeRequests().requestMatchers("/**")
//                //                .hasIpAddress(env.getProperty("gateway.ip"))
//                .access("hasIpAddress('"+"172.30.1.8"+"')")
//                .and()
//                .addFilter(getAuthenticationFilter());
        return http.build();

    }

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return auth.build();
//        return null;
    }
    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
        return authenticationFilter;
    }

}
