package org.example.apitestingproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secretBase64;   // app.jwt.secret-base64
    private String issuer;         // app.jwt.issuer

    public String getSecretBase64() { return secretBase64; }
    public void setSecretBase64(String s) { this.secretBase64 = s; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
}
