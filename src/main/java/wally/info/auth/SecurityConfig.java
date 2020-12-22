package wally.info.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Getter
@Component
public class SecurityConfig {
  private final byte[] SECRET;
  private final long EXPIRATION_TIME;

  public SecurityConfig(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.expiration-time}") long expiration_time) {
    this.SECRET = Base64.getDecoder().decode(secret);
    this.EXPIRATION_TIME = expiration_time;
  }


}
