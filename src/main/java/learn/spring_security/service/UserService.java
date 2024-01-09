package learn.spring_security.service;

import learn.spring_security.model.User;
import learn.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public void save(User user) {
    userRepository.save(user);
  }
}
