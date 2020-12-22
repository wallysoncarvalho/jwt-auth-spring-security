package wally.info.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import wally.info.auth.JwtTokenProvider;
import wally.info.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public AuthController(
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password) {

    System.out.println("asdas asdasd");

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    var user = userRepository.findUserByUsername(username).get();
    return jwtTokenProvider.createToken(username, user.getRoles());
  }
}
