package com.example.polls.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by nikolaBrodar on 19/08/17.
 */
//@Component
//public class JwtTokenProvider {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
//
//    @Value("${app.jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${app.jwtExpirationInMs}")
//    private int jwtExpirationInMs;
//
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    public String generateToken(Authentication authentication) {
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//        return generateToken(new HashMap<>(), userPrincipal.getId());
//    }
//
//    public String generateToken(Map<String, Object> extraClaims, Long userId) {
//        return buildToken(extraClaims, userId, jwtExpirationInMs);
//    }
//
//    public long getExpirationTime() {
//        return jwtExpirationInMs;
//    }
//
//    private String buildToken(
//            Map<String, Object> extraClaims,
//            Long userId,
//            long expiration
//    ) {
//        return Jwts
//                .builder()
//                .setClaims(extraClaims)
//                .setSubject(Long.toString(userId))
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
////        final String username = extractUsername(token);
////        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//        return  !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//
//
//    public Long getUserIdFromJWT(String token) {
////        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
////
////        Claims claims = Jwts.parser()
////                .verifyWith(key) // Convert secret to bytes
////                .build()
////                .parseSignedClaims(token)
////                .getPayload();
//
//        Claims claims = Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//
//        logger.debug("user id is: {}", Long.parseLong(claims.getSubject()));
//
////        Claims claims = Jwts
////                .parser()
////                .setSigningKey(getSignInKey())
////                .build()
////                .parseSignedClaims(token)
////                .getPayload();
//
//        return Long.parseLong(claims.getSubject());
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
////            SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
////
////            Jwts.parser()
////                    .verifyWith(key)
////                    .build()
////                    .parseSignedClaims(authToken);
////            Jwts
////                    .parser()
////                    .verifyWith(getSignInKey())
////                    .base64UrlDecodeWith()
////                    .parseClaimsJws(authToken)
////                    .getPayload();
//            return true;
//        } catch (SignatureException ex) {
//            logger.error("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty.");
//        }
//        return false;
//    }
//}
