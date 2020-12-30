package wally.info.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import wally.info.config.JwtConfiguration;
import wally.info.entity.UserRole;
import wally.info.exception.InvalidTokenException;
import wally.info.service.UserDetailsServiceImpl;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
  private JwtConfiguration securityConfig;
  private UserDetailsServiceImpl userDetailsServiceImpl;

  public JwtTokenProvider(
		JwtConfiguration securityConfig, UserDetailsServiceImpl userDetailsServiceImpl) {
    this.securityConfig = securityConfig;
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }

  public String createToken(String username, Set<UserRole> roles) {
    var claims = Jwts.claims().setSubject(username);

    claims.put(
        "auth",
        roles.stream()
            .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
            .collect(Collectors.toList()));

    var now = new Date();
    var expiration = new Date(now.getTime() + securityConfig.getEXPIRATION_TIME());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(getKeyFromSecret())
        .compact();
  }

  public Authentication getAuthentication(String token) {
    var userDetails = userDetailsServiceImpl.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
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
    } catch (JwtException | IllegalArgumentException e) {
      throw new InvalidTokenException("Invalid token");
    }
  }

  private Key getKeyFromSecret() {
    var signatureAlgorithm = SignatureAlgorithm.HS256;
    return new SecretKeySpec(securityConfig.getSECRET(), signatureAlgorithm.getJcaName());
  }
}
