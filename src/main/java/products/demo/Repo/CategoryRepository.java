package products.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import products.demo.Model.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
