package learn.spring_security.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  private String username;
  private String password;
  private String firstname;
  private String lastname;
  private String country;
}
