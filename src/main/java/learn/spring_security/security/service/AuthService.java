package learn.spring_security.security.service;

import learn.spring_security.enums.Role;
import learn.spring_security.model.User;
import learn.spring_security.repository.UserRepository;
import learn.spring_security.security.jwt.AuthResponse;
import learn.spring_security.security.jwt.LoginRequest;
import learn.spring_security.security.jwt.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;
  public AuthResponse login(LoginRequest req) {
    String username = req.getUsername();
    String password = req.getPassword();
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    UserDetails user = userRepository.findByUsername(username).orElseThrow();
    String token = jwtService.getToken(user);
    return AuthResponse.builder().token(token).build();
  }

  public AuthResponse register(RegisterRequest req) {
    String encodedPassword = encoder.encode(req.getPassword());

    User newUser = User.builder()
      .username(req.getUsername())
      .firstName(req.getFirstname())
      .lastName(req.getLastname())
      .country(req.getCountry())
      .password( encodedPassword )
      .role(Role.ADMIN)
      .build();

      userRepository.save(newUser);

      // Retornamos token en caso de validacion por email o si dejamos al user logeado
      return AuthResponse.builder()
        .token(jwtService.getToken(newUser))
        .build();
  }
}
