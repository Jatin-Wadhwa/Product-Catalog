package products.demo.Service;

import org.springframework.stereotype.Service;
import products.demo.Model.ProductModel;
import products.demo.Repo.productRepo;

import java.util.List;

@Service
public class ProductService {
    private final productRepo repository;

    public ProductService(productRepo repository) {
        this.repository = repository;
    }

    public List<ProductModel> getAllProducts(){
        return repository.findAll();
    }
}
