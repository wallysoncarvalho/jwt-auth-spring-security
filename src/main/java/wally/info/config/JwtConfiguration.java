package wally.info.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Getter
@Component
// @ConfigurationProperties(prefix = "security")
public class JwtConfiguration {
  private final byte[] SECRET;
  private final long EXPIRATION_TIME;

  public JwtConfiguration(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.expiration-time}") long expiration_time) {
    this.SECRET = Base64.getDecoder().decode(secret);
    this.EXPIRATION_TIME = expiration_time;
  }
}
