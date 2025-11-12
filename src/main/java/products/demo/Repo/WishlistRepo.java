package products.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import products.demo.Model.User;
import products.demo.Model.WishlistModel;

import java.util.List;

@Repository
public interface WishlistRepo extends JpaRepository<WishlistModel,Integer> {
    List<WishlistModel> findByUser(User user);
    boolean existsByUserIdAndProductId(Integer userId, Integer productId);
    void deleteByUserIdAndProductId(Integer userId, Integer productId);
}
