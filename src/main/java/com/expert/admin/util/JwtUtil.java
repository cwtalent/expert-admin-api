package com.expert.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret:expert-admin-secret-key-2026-jhzlb-voting-system-secure-key-for-jwt-token-generation}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}") // 24小时
    private Long expiration;
    
    private SecretKey getSigningKey() {
        // 确保密钥至少32字节（256位）
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            // 如果密钥太短，进行填充
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
            return Keys.hmacShaKeyFor(paddedKey);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    public Boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
