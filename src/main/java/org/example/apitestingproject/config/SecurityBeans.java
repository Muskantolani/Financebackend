package org.example.apitestingproject.config;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityBeans {

    @Bean
    public SecretKey jwtSecretKey(JwtProperties props) {
        byte[] keyBytes = Decoders.BASE64.decode(props.getSecretBase64());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

