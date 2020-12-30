package wally.info.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "USER_JWT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
  @Id
  @Column(name = "ID", nullable = false, unique = true)
  private String id;

  @NotBlank
  @Column(name = "USERNAME", nullable = false, unique = true)
  private String username;

  @NotBlank
  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @Column(name = "NAME", nullable = false)
  private String name;

  @NotBlank
  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole.class)
  private Set<UserRole> roles;

}
