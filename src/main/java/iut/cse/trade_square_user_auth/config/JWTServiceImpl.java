package iut.cse.trade_square_user_auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import iut.cse.trade_square_user_auth.models.entities.RefreshToken;
import iut.cse.trade_square_user_auth.repositories.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class JWTServiceImpl implements JWTService {

    @Value("${provisorr.jwtSecret}")
    private String jwtSecret;

    @Value("${provisorr.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${provisorr.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    public JWTServiceImpl(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateToken(UserDetails userDetails) {
        Date currentTime = new Date(ZonedDateTime.now().toInstant().toEpochMilli());
        Date expirationDate = new Date(currentTime.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(currentTime)
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails){
        Date currentTime = new Date(ZonedDateTime.now().toInstant().toEpochMilli());
        Date expirationDate = new Date(currentTime.getTime() + jwtRefreshExpirationMs);
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(currentTime)
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUserName(String token){return extractClaim(token, Claims::getSubject);}

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !istokenExpired(token);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteRefreshToken(Long userId){
        try{
            refreshTokenRepository.deleteByUserId(userId);
        }catch (Exception e){
            throw new RuntimeException("Error deleting refresh token for user id : " + userId + " ,error : " + e.getMessage());
        }
    }

    @Override
    public void saveRefreshToken(Long userId, String refreshToken){
        try{
            Optional<RefreshToken> optional = refreshTokenRepository.findByUserId(userId);
            if(optional.isPresent()){
                RefreshToken token = optional.get();
                token.setToken(refreshToken);
                refreshTokenRepository.save(token);
            }else{
                refreshTokenRepository.save(new RefreshToken(refreshToken,userId));
            }
        }catch (Exception e){
                throw new RuntimeException("Error while saving refresh token for user id : " + userId + " ,error message: " + e.getMessage());
            }
        }


    @Override
    public boolean validateRefreshToken(Long userId, String refreshToken){
        try{
            return refreshTokenRepository.existsByUserIdAndToken(userId, refreshToken);
        }catch (Exception e){
            throw new RuntimeException("Error while validating refresh token for user id : " + userId+" ,error message: " + e.getMessage());
        }
    }

    @Override
    public boolean istokenExpired(String token){
        Date currentTime = new Date(ZonedDateTime.now().toInstant().toEpochMilli());
        return extractClaim(token, Claims::getExpiration).before(currentTime);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            log.warn("JWT token expired: {}", e.getMessage());
            return null;
        }
    }

    private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(key);
    }
}
