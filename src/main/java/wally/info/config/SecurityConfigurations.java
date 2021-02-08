package wally.info.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Getter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityConfigurations {
  private final String secret;
  private final long expirationTime;

  public SecurityConfigurations(
      @Value("${security.secret}") String secret,
      @Value("${security.expiration-time}") long expiration_time) {
    this.secret = secret;
    this.expirationTime = expiration_time;
  }
}
