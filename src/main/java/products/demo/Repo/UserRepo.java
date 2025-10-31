package products.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import products.demo.Model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String userName);
}
