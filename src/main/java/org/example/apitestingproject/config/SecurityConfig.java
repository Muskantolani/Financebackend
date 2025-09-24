package org.example.apitestingproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitestingproject.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final ObjectMapper om = new ObjectMapper();

    public SecurityConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Role rules (must match roles put in JWT, e.g. ROLE_USER, ROLE_SUB_ADMIN, ROLE_SUPER_ADMIN)
                        .requestMatchers("/admin/**").hasAnyRole("SUPER_ADMIN","SUB_ADMIN")
                        .requestMatchers("/subadmin/**").hasAnyRole("SUPER_ADMIN", "SUB_ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(rest401())
                        .accessDeniedHandler(rest403())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:5173", "http://127.0.0.1:5173",
                "http://localhost:3000", "http://127.0.0.1:3000"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));        // important for preflight
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(false);             // OK for JWT in Authorization header

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }



    private AuthenticationEntryPoint rest401() {
        return (request, response, ex) ->
                writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Missing or invalid token");
    }

    private AccessDeniedHandler rest403() {
        return (request, response, ex) ->
                writeJson(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Insufficient permissions");
    }

    private void writeJson(HttpServletResponse res, int status, String error, String message) throws IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", Instant.now().toString());
        om.writeValue(res.getOutputStream(), body);
    }
}

