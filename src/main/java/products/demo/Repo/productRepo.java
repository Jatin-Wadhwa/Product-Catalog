package products.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import products.demo.Model.ProductModel;

import java.util.List;

public interface productRepo extends JpaRepository<ProductModel,Integer> {
    List<ProductModel> findTop10ByProductNameContainingIgnoreCase(String name);
}
