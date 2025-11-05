package products.demo.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import products.demo.DTO.CategoryDTO;
import products.demo.DTO.ProductDto;
import products.demo.Model.Category;
import products.demo.Model.ProductModel;
import products.demo.Repo.CategoryRepository;
import products.demo.Repo.productRepo;
import products.demo.exception.ProductNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final productRepo repository;
    private final CategoryRepository categoryRepo;

    public ProductService(productRepo repository,CategoryRepository categoryRepo) {
        this.repository = repository;
        this.categoryRepo=categoryRepo;
    }

//    public List<ProductDto> getAllProducts() {
//        return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
//    }

    public ProductDto getProductById(Integer productId) {
        return repository.findById(productId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    public ProductDto addProduct(@Valid ProductDto dto) {
        ProductModel entity = convertToEntity(dto);
        entity.set_Active(true);
        entity.set_Deleted(false);
        entity.setCreatedAt(LocalDateTime.now().toString());
        entity.setUpdatedAt(LocalDateTime.now().toString());
        ProductModel saved = repository.save(entity);
        return convertToDto(saved);
    }

    public ProductDto updateProduct(Integer productId, @Valid ProductDto dto) {
        ProductModel existing = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        existing.setProductName(dto.getProductName());
        existing.setProductDesc(dto.getProductDesc());
        existing.setDiscount(dto.getDiscount());
        existing.setPrice(dto.getPrice());
        existing.setCurrency(dto.getCurrency());
        existing.setSku(dto.getSku());
        existing.setStock_quantity(String.valueOf(dto.getStockQuantity()));
        existing.setMain_image_url(dto.getMainImageUrl());
        existing.setUpdatedAt(LocalDateTime.now().toString());
        existing.set_Active(dto.is_Active());
        existing.set_Deleted(dto.is_Deleted());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ProductNotFoundException("Category not found with id: " + dto.getCategoryId()));
            existing.setCategory(category);
        }

        ProductModel updated = repository.save(existing);
        return convertToDto(updated);
    }

    public boolean deleteProduct(Integer id) {
        return repository.findById(id).map(product -> {
            product.set_Deleted(true);
            product.set_Active(false);
            repository.save(product);
            return true;
        }).orElse(false);
    }

    public List<ProductDto> getProducts(String name, Double price, String category,boolean dashboard, Double minDiscount, Double maxDiscount,
                                        String sortBy, String direction, int page, int pageSize) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        List<ProductModel> entities = repository.findAll(pageable).getContent();

        return entities.stream()
                .filter(p -> name == null || (p.getProductName() != null && p.getProductName().toLowerCase().contains(name.toLowerCase())))
                .filter(p -> price == null || (p.getPrice() != null && p.getPrice() >= price))
                .filter(p->!dashboard||Boolean.TRUE.equals(p.isDashboard()))
                .filter(p -> {
                    Double discountValue = p.getDiscount();

                    // If user applied a discount filter, exclude nulls
                    if ((minDiscount != null || maxDiscount != null) && discountValue == null) {
                        return false;
                    }

                    if (minDiscount != null && maxDiscount != null) {
                        // Between min and max
                        return discountValue >= minDiscount && discountValue <= maxDiscount;
                    } else if (minDiscount != null) {
                        // Greater than or equal to min
                        return discountValue >= minDiscount;
                    } else if (maxDiscount != null) {
                        // Less than or equal to max
                        return discountValue <= maxDiscount;
                    } else {
                        // No discount filter applied → allow all
                        return true;
                    }
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    private ProductDto convertToDto(ProductModel entity) {
        return new ProductDto(
                entity.getId(),
                entity.getProductName(),
                entity.getProductDesc(),
                entity.isDashboard(),
                entity.getDiscount(),
                entity.getCategory() != null ? entity.getCategory().getId() : null,
                entity.getPrice(),
                entity.getCurrency(),
                entity.getSku(),
                entity.getStock_quantity() != null && !entity.getStock_quantity().isBlank()
                        ? Integer.parseInt(entity.getStock_quantity())
                        : 0,
                entity.getMain_image_url(),
                entity.is_Active(),
                entity.is_Deleted()
        );
    }

    private ProductModel convertToEntity(ProductDto dto) {
        ProductModel entity = new ProductModel();
        entity.setProductName(dto.getProductName());
        entity.setProductDesc(dto.getProductDesc());
        entity.setDashboard(dto.isDashboard());
        entity.setDiscount(dto.getDiscount());
        entity.setPrice(dto.getPrice());
        entity.setCurrency(dto.getCurrency());
        entity.setSku(dto.getSku());
        entity.setStock_quantity(String.valueOf(dto.getStockQuantity()));
        entity.setMain_image_url(dto.getMainImageUrl());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ProductNotFoundException("Category not found with id: " + dto.getCategoryId()));
            entity.setCategory(category);
        }

        return entity;
    }
}
