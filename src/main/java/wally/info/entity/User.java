package wally.info.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USER_JWT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User {
  @Id
  @Column(name = "ID", nullable = false, unique = true)
  private String id;

  @Column(name = "USERNAME", nullable = false, unique = true)
  private String username;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<UserRole> roles;

}
