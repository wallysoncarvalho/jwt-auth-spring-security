package wally.info.service;

import org.springframework.stereotype.Service;
import wally.info.entity.User;
import wally.info.repository.UserJpaRepository;
import wally.info.util.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {
  private final JwtTokenProvider jwtTokenProvider;
  private final UserJpaRepository userJpaRepository;

  public AuthService(JwtTokenProvider jwtTokenProvider, UserJpaRepository userJpaRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userJpaRepository = userJpaRepository;
  }

  public User getUserDetails(HttpServletRequest http) {

    var userToken = jwtTokenProvider.resolveToken(http);
    var username = jwtTokenProvider.getSubject(userToken);

    var userOptn = userJpaRepository.findUserByUsername(username);

    return userOptn.orElse(null);
  }
}
