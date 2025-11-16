package pe.com.ecocleany.ecosmart.iam.infrastructure.adapters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret:very-secret-key-change-it}")
    private String secret;

    @Value("${security.jwt.ttl-seconds:3600}")
    private long ttlSeconds;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public TokenPair generateToken(User user, Set<RoleName> roles) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ttlSeconds);

        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .claim("email", user.getEmail())
                .claim("roles", roles.stream().map(RoleName::name).toList())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new TokenPair(token, expiresAt);
    }

    public UUID extractUserId(String jwt) {
        Claims claims = parseClaims(jwt);
        return UUID.fromString(claims.getSubject());
    }

    public Claims parseClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public Set<String> extractRoles(String jwt) {
        Claims claims = parseClaims(jwt);
        Object rawRoles = claims.get("roles");
        if (rawRoles instanceof Iterable<?> iterable) {
            return StreamSupport.stream(iterable.spliterator(), false)
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        throw new IllegalStateException("roles claim missing");
    }

    public record TokenPair(String token, Instant expiresAt) {
    }
}
