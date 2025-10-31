package products.demo.Security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import products.demo.Model.User;
import products.demo.Repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo repo;

    public CustomUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User user=repo.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" +userName));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_",""))
                .build();
    }

}
