package com.sumith.jfs.PaAnaBot.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOrigin("http://localhost:3000");
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/chat/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/report/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login.disable())
                .httpBasic(basic -> basic.disable());
        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
