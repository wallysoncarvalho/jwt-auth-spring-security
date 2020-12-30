package wally.info.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import wally.info.dto.JwtAccessTokenResponse;
import wally.info.entity.User;
import wally.info.entity.UserRole;
import wally.info.exception.UserExistsException;
import wally.info.repository.UserRepository;
import wally.info.util.JwtTokenProvider;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

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
  public ResponseEntity<JwtAccessTokenResponse> login(
      @RequestParam String username, @RequestParam String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    var user = userRepository.findUserByUsername(username);
    var token = jwtTokenProvider.createToken(username, user.get().getRoles());
    return ResponseEntity.ok(JwtAccessTokenResponse.builder().access_token(token).build());
  }

  @Transactional
  @PostMapping("/register")
  public ResponseEntity<JwtAccessTokenResponse> register(@Valid @RequestBody User user) {
    var userExists = userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());

    if (userExists) {
      throw new UserExistsException(
          String.format(
              "Username %s or email %s already in use.", user.getUsername(), user.getEmail()));
    }

    var id = UUID.randomUUID().toString();
    var password = passwordEncoder.encode(user.getPassword());
    user.setId(id);
    user.setRoles(Set.of(UserRole.ROLE_CLIENT));
    user.setPassword(password);
    this.userRepository.save(user);

    var token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

    return ResponseEntity.ok(JwtAccessTokenResponse.builder().access_token(token).build());
  }
}
