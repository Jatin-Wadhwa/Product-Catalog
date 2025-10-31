package products.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import products.demo.Model.Role;
import products.demo.Model.User;
import products.demo.Repo.UserRepo;
import products.demo.Security.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepo repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, UserRepo repo, PasswordEncoder encoder, JwtUtil jwtUtil){
        this.authManager=authManager;
        this.repo=repo;
        this.encoder=encoder;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRole()==null) user.setRole(Role.ROLE_USER);
        repo.save(user);
        return ResponseEntity.ok(Map.of("message","User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> request){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.get("username"),request.get("password")));

        User user=repo.findByUserName(request.get("username")).orElseThrow();
        String token= jwtUtil.generateToken(user.getUserName(),user.getRole().name());
        return ResponseEntity.ok(Map.of("token",token));
    }
}
