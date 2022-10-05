package com.example.modulesystem.security;



import com.example.modulecore.exception.CustomRuntimeException;
import com.example.modulecore.response.code.SecurityResponseCode;
import com.example.modulesystem.redis.RedisService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisService redisService;

    @Value("${spring.jwt.secret-key}")
    private  String SECRET_KEY;
    @Value("${spring.jwt.blacklist.access-token}")
    private String blackListATPrefix;
    @Value("${spring.jwt.access-token-invalid-hour}")
    private String accessTokenInvalidHour;
    @Value("${spring.jwt.refresh-token-invalid-hour}")
    private String refreshTokenInvalidHour;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public boolean validateToken(HttpServletRequest request, String token) {
        String exception = "exception";
        try {
            String expiredAT = redisService.getValues(blackListATPrefix + token);
            if (expiredAT != null) {
                throw new CustomRuntimeException("토큰이 만료되었습니다.",SecurityResponseCode.F_EXPIRED_TOKEN);
            }
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            request.setAttribute(exception, "토큰의 형식을 확인하세요");
        } catch (ExpiredJwtException e) {
            request.setAttribute(exception, SecurityResponseCode.F_EXPIRED_TOKEN.name());
        } catch (IllegalArgumentException e) {
            request.setAttribute(exception, "JWT compact of handler are invalid");
        }
        return false;
    }



    public void logout(String userId, String accessToken) {
        long expiredAccessTokenTime = getExpiredTime(accessToken).getTime() - new Date().getTime();
        redisService.setValues(blackListATPrefix + accessToken, userId, Duration.ofMillis(expiredAccessTokenTime));
        redisService.deleteValues(userId); // Delete RefreshToken In Redis
    }


    public String createToken(String userId, Long tokenInvalidTime){
        Claims claims = Jwts.claims().setSubject(userId); // claims 생성 및 payload 설정
        claims.setId(UUID.randomUUID().toString());
        claims.put("https://itech.com/jwt_claims",true);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenInvalidTime)) // 토큰 유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }


    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public String createAccessToken(String userId) {

        long tokenInvalidTime = 1000L * 60 * 60 * Integer.parseInt(accessTokenInvalidHour);
        //Long tokenInvalidTime = 1000L * 60; // 1m
        return this.createToken(userId, tokenInvalidTime);
    }

    public String createRefreshToken(String userId) {
        long tokenInvalidTime = 1000L * 60 * 60 * Integer.parseInt(refreshTokenInvalidHour);
        String refreshToken = this.createToken(userId, tokenInvalidTime);
        redisService.setValues(userId, refreshToken, Duration.ofMillis(tokenInvalidTime));
        return refreshToken;
    }

    public void checkRefreshToken(String name, String refreshToken) {
        String redisRT = redisService.getValues(name);
        if (!refreshToken.equals(redisRT)) {
            throw new CustomRuntimeException("알 수 없는 토큰입니다..",SecurityResponseCode.F_TOKEN);
        }
    }
    private Date getExpiredTime(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }
}
