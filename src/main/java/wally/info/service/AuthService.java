package wally.info.service;

import org.springframework.stereotype.Service;
import wally.info.entity.User;
import wally.info.repository.UserRepository;
import wally.info.util.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userRepository = userRepository;
  }

  public User getUserDetails(HttpServletRequest http) {

    var userToken = jwtTokenProvider.resolveToken(http);
    var username = jwtTokenProvider.getUsername(userToken);

    var userOptn = userRepository.findUserByUsername(username);

    return userOptn.orElse(null);
  }
}
