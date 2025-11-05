package products.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import products.demo.DTO.ApiResponse;
import products.demo.DTO.CategoryDTO;
import products.demo.DTO.ProductDto;
import products.demo.Service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCategories(){

        List<CategoryDTO> categories=service.getCategories();
        return ResponseEntity.ok(new ApiResponse<>(true,"categories fetched successfully",categories));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> addCategories(@Valid @RequestBody CategoryDTO category){
        CategoryDTO newCategory=service.addCategory(category);
        return ResponseEntity.status(201).body(new ApiResponse<>(true,"category added successfully",newCategory));
    }

}
