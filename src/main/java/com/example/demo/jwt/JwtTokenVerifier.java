package com.example.demo.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier  extends OncePerRequestFilter {
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            // jesli token jest bledny lub pusty powrot
            //przy strings uzyc biblioteki od google

            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        String tokenFromClient = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {

            //usuwa z poczatku tokena od klienta bearer
            String secureKey = "securesecuresecuresecuresecuresecuresecuresecuresecuresecure";
            // ten sam secureKey co w klasie jwtusernameand pass...
//           ponizej w setsigningKey ten sam sposob na dekodowanie tokena co w jwtusernameand....

            Jws<Claims> claimsJws = Jwts.parser()
//                    .setSigningKey(Keys.hmacShaKeyFor(secureKey.getBytes()))
                    .setSigningKey(secretKey) //secretKey z application.properties
                    .parseClaimsJws(tokenFromClient);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            var authorities = (List<Map<String, String>>) body.get("authorities");  // authorities jak w claimie w jwt user and pass.....

            Set<SimpleGrantedAuthority> simpleGrantedAuthoritiesFromClientResponse = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthoritiesFromClientResponse
            );


            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Jwt exception, Token %s cannot be trusted ",tokenFromClient));
        }


        //zwraca api dla klienta jesli zapytanie bylo poprawne i jwt byl poprawny
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }


}
