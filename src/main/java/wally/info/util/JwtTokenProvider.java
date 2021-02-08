package wally.info.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import wally.info.config.SecurityConfigurations;
import wally.info.entity.User;
import wally.info.entity.UserRole;
import wally.info.exception.InvalidTokenException;
import wally.info.service.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
  private SecurityConfigurations securityConfig;
  private UserDetailsServiceImpl userDetailsServiceImpl;

  public JwtTokenProvider(
      SecurityConfigurations securityConfig, UserDetailsServiceImpl userDetailsServiceImpl) {
    this.securityConfig = securityConfig;
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }

  public String createToken(String username, Set<String> roles) {
    var claims = Jwts.claims().setSubject(username);

    claims.put("roles", roles);

    var now = new Date();
    var expiration = new Date(now.getTime() + securityConfig.getExpirationTime());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(getKeyFromSecret())
        .compact();
  }

  public Authentication getAuthentication(String token) {
    var userDetails = userDetailsServiceImpl.loadUserByUsername(getSubject(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getSubject(String token) {
    var parsed =
        Jwts.parserBuilder().setSigningKey(getKeyFromSecret()).build().parseClaimsJws(token);

    return parsed.getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");

    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getKeyFromSecret()).build().parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      throw new SignatureException("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      throw new MalformedJwtException("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      throw new ExpiredJwtException(null, null, "Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      throw new UnsupportedJwtException("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("JWT claims string is empty.");
    }
  }

  private Key getKeyFromSecret() {
    var decodedSecret = Base64.getDecoder().decode(securityConfig.getSecret());
    return Keys.hmacShaKeyFor(decodedSecret);
  }
}
