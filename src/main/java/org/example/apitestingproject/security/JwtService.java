package org.example.apitestingproject.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.example.apitestingproject.config.JwtProperties;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class JwtService {
    private final SecretKey key;
    private final JwtProperties props;

    public JwtService(SecretKey key, JwtProperties props) {
        this.key = key; this.props = props;
    }

    public Claims parse(String token) {
        var payload = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        // optional: enforce expected issuer
        if (props.getIssuer() != null && !props.getIssuer().isBlank()) {
            if (!Objects.equals(props.getIssuer(), payload.getIssuer())) {
                throw new IllegalArgumentException("Wrong issuer");
            }
        }
        return payload;
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(Claims c) {
        Object v = c.get("roles");
        if (v instanceof List<?> list) return list.stream().map(String::valueOf).toList();
        return List.of();
    }
}
