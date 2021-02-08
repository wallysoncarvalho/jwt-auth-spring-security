package wally.info.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wally.info.repository.UserJpaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserJpaRepository userJpaRepository;

  public UserDetailsServiceImpl(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
    final var userOptional = this.userJpaRepository.findUserByUsername(subject);

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
    throw new UsernameNotFoundException("Username " + subject + " not found !");
  }
}
