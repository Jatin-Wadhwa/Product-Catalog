package products.demo.Service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import products.demo.DTO.CategoryDTO;
import products.demo.Model.Category;
import products.demo.Model.ProductModel;
import products.demo.Repo.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo){
        this.categoryRepo=categoryRepo;

    }


    public List<CategoryDTO> getCategories() {
        List<Category> categories = categoryRepo.findAll();

        return categories.stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO addCategory(@Valid CategoryDTO category){
        Category entity= convertToCategoryEntity(category);
        entity.setName(category.getName());
        entity.setMain_image_url(category.getCategoryImageUrl());
        Category saved=categoryRepo.save(entity);
        return convertToCategoryDTO(saved);
    }


    private CategoryDTO convertToCategoryDTO(Category entity) {
        List<String> productNames = entity.getProducts() != null
                ? entity.getProducts().stream()
                .map(ProductModel::getProductName)
                .filter(name -> name != null && !name.isBlank())
                .limit(5) // optional: send only first 5 for preview
                .collect(Collectors.toList())
                : List.of();

        return new CategoryDTO(
                entity.getId(),
                entity.getName(),
                entity.getMain_image_url(),
                productNames
        );
    }

    private Category convertToCategoryEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setMain_image_url(dto.getCategoryImageUrl());
        return entity;
    }
}
