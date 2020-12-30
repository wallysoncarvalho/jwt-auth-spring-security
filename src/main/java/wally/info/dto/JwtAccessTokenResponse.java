package wally.info.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtAccessTokenResponse {
  private String access_token;
//  private int expires_in;
//  private String refresh_token;
//  private int refresh_expires_in;
//  private String token_type;
}
