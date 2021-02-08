package wally.info.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
@SecondaryTable(
        name = "user_relevance",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id"))
public class User {
  @Id
  @Size(max = 36, min = 36, message = "Id should be a UUID of size 36")
  @NotBlank(message = "Id must no be null or empty")
  private String id;

  @NotBlank(message = "Name must not be null or empty")
  @Size(max = 300, message = "Maximum size for the user's name is 300 characters")
  private String name;

  @NotBlank(message = "Username must not be null or empty")
  @Size(max = 300, message = "Maximum size for the username is 300 characters")
  private String username;

  @Column(table = "user_relevance")
  private Integer relevance;

  @NotBlank(message = "Password must not be null or empty")
  @Size(max = 250, message = "Maximum size for the password is 250 characters")
  private String password;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole.class)
  private Set<UserRole> roles;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getRelevance() {
    return relevance;
  }

  public void setRelevance(Integer relevance) {
    this.relevance = relevance;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<UserRole> roles) {
    this.roles = roles;
  }
}
