package wally.info.controller;

import lombok.Getter;
import lombok.Setter;
import wally.info.entity.User;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterUserDto {
  @NotBlank(message = "Name must not be null or empty")
  @Size(max = 300, message = "Maximum size for the user's name is 300 characters")
  private String name;

  @NotBlank(message = "Username must not be null or empty")
  @Size(max = 300, message = "Maximum size for the username is 200 characters")
  private String username;

  @NotBlank(message = "Password required")
  @Size(max = 200, message = "Maximum size for the username is 200 characters")
  private String password;

  public static User toUser(RegisterUserDto userDto) {
    var user = new User();
    user.setName(userDto.getName());
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    return user;
  }
}
