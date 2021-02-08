package wally.info.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import wally.info.dto.JwtAccessTokenResponse;
import wally.info.entity.UserRole;
import wally.info.exception.UserExistsException;
import wally.info.repository.UserJpaRepository;
import wally.info.util.JwtTokenProvider;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserJpaRepository userJpaRepository;

  public AuthController(
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      PasswordEncoder passwordEncoder,
      UserJpaRepository userJpaRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.userJpaRepository = userJpaRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAccessTokenResponse> login(@RequestBody @Valid LoginDto loginDto) {

    var user =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

    var roles =
        user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

    var token = jwtTokenProvider.createToken(user.getName(), roles);
    return ResponseEntity.ok(JwtAccessTokenResponse.builder().access_token(token).build());
  }

  @Transactional
  @PostMapping("/register")
  public ResponseEntity<JwtAccessTokenResponse> register(
      @RequestBody @Valid RegisterUserDto userDto) {
    var userExists = userJpaRepository.existsByUsername(userDto.getUsername());

    if (userExists) {
      throw new UserExistsException(
          String.format("Username %s already in use.", userDto.getUsername()));
    }

    var user = RegisterUserDto.toUser(userDto);
    user.setId(UUID.randomUUID().toString());
    user.setRoles(Set.of(UserRole.ROLE_CLIENT));
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    this.userJpaRepository.save(user);

    var token =
        jwtTokenProvider.createToken(
            user.getUsername(),
            user.getRoles().stream().map(UserRole::getAuthority).collect(Collectors.toSet()));

    return ResponseEntity.ok(JwtAccessTokenResponse.builder().access_token(token).build());
  }
}
