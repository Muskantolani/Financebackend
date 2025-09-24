package org.example.apitestingproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    public JwtAuthFilter(JwtService jwt) { this.jwt = jwt; }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwt.parse(token);

                String username = claims.getSubject();
                List<SimpleGrantedAuthority> auths = jwt.extractRoles(claims)
                        .stream().map(SimpleGrantedAuthority::new).toList();

                // Pull uid claim (int or string) safely
                Integer uid = null;
                Object v = claims.get("uid");
                if (v instanceof Number n) {
                    uid = n.intValue();
                } else if (v instanceof String s) {
                    try { uid = Integer.parseInt(s); } catch (NumberFormatException ignored) {}
                }

                // Build Authentication with username + roles
                var auth = new UsernamePasswordAuthenticationToken(username, null, auths);

                // Expose uid in two convenient ways:
                if (uid != null) {
                    // 1) request attribute (easy to grab via @RequestAttribute)
                    req.setAttribute("uid", uid);
                    // 2) auth details (for reading from Authentication#getDetails)
                    auth.setDetails(uid);
                }

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // leave unauthenticated; SecurityConfig will return 401
            }
        }
        chain.doFilter(req, res);
    }
}
