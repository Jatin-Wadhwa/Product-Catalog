package products.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import products.demo.DTO.ProductDto;
import products.demo.Service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service=service;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>>  getAllProducts(){
        List<ProductDto> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer productId){
        ProductDto product = service.getProductById(productId);
        if(product!=null){
            return ResponseEntity.ok(product);
        }
        else {
            return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Product not found", "id", productId)
            );
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto product){
        ProductDto newProduct= service.addProduct(product);
        return ResponseEntity.status(201).body(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable("id") Integer productId, @Valid @RequestBody ProductDto productDto){
        ProductDto updated=service.updateProduct(productId,productDto);
        if(updated!=null){
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.status(404).body(
                    Map.of("error", "Product not found", "id", productId)
            );
        }
    }


    @DeleteMapping("/${id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id){
        boolean deleted=service.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok(
                    java.util.Map.of("message", "Product marked as deleted", "id", id)
            );
        } else {
            return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Product not found", "id", id)
            );
        }
    }


}
