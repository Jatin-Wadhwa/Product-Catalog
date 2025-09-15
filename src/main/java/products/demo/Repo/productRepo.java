package products.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import products.demo.Model.ProductModel;

public interface productRepo extends JpaRepository<ProductModel,Integer> {
}
