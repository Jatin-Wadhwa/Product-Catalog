package products.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import products.demo.Model.CartModel;
import products.demo.Model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<CartModel,Integer> {

    List<CartModel> findByUser(User user);

    boolean existsByUserIdAndProductId(Integer userId, Integer productId);

    Optional<CartModel> findByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserIdAndProductId(Integer userId,Integer productId);
}
