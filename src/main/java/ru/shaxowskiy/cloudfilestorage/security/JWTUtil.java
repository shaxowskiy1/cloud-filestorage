package ru.shaxowskiy.cloudfilestorage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.shaxowskiy.cloudfilestorage.dto.SignUpRequestDTO;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

@Component
public class JWTUtil {
    private final String issuer = "cloud_filestorage";
    @Value("${jwt_secret}")
    private String secret;
    private final String subject = "User details";

    public String generateToken(SignUpRequestDTO user){
        Date expirateDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("username", user.getUsername())
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirateDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT validateToken(String token){
        JWTVerifier verifyingToken = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();
        return verifyingToken.verify(token);
    }
}
