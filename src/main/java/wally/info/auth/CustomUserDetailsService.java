package wally.info.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wally.info.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    final var userOptional = this.userRepository.findUserByUsername(s);

    if (userOptional.isPresent()) {
      var user = userOptional.get();

      return User.withUsername(user.getUsername())
          .password(user.getPassword())
          .authorities(user.getRoles())
          .accountExpired(false)
          .accountLocked(false)
          .credentialsExpired(false)
          .disabled(false)
          .build();
    }
    throw new UsernameNotFoundException("Username " + s + " not found !");
  }
}
