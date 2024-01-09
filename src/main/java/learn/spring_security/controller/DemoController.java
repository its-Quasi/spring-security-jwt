package learn.spring_security.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

  @GetMapping(value = "demo")
  public String welcomeLogged() {
    return "Welcome logged user";
  }

  @PostMapping(value = "demo")
  public String anotherProof(@RequestBody Map<String , Object> req) {
    return "PROOF ENDPOINT";
  }

  @GetMapping(value = "all")
  @PreAuthorize("hasAuthority('ADMIN')")
  public String onlyAdmin() {
    return "ADMIN ENDPOINT";
  }
}
