package com.example.demo.jwt;

import com.google.common.net.HttpHeaders;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;

;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig{

    private String secureKey;
    private String tokenPrefix;
    private String tokenExpirationAfterDays;

    public JwtConfig() {
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(String tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }



    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
