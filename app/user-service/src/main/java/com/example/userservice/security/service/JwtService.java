package com.example.userservice.security.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.userservice.jpa.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {

    Environment env;
    
    public String generateToken(UserEntity userEntity) {
        return Jwts
                    .builder()
                    .subject(userEntity.getUserId())
                    .expiration(
                        new Date(
                            System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expriation_time"))
                            )
                        )
                    // .signWith(Jwts.SIG.HS512, env.getProperty("token.secret"))
                    .signWith(getSignInKey())
                    .compact();
    }

    private Key getSignInKey() {
      

        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("token.secret"));
        // return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
