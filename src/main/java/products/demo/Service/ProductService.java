package products.demo.Service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import products.demo.DTO.ProductDto;
import products.demo.Model.ProductModel;
import products.demo.Repo.productRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final productRepo repository;

    public ProductService(productRepo repository) {
        this.repository = repository;
    }

    public List<ProductDto> getAllProducts() {
        return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProductDto getProductById(Integer productId) {
        return repository.findById(productId).map(this::convertToDto).orElse(null);
    }

    public ProductDto addProduct(@Valid ProductDto product) {
        ProductModel entity = convertToEntity(product);
        entity.setStock_quantity(
                product.getStockQuantity() != null ? String.valueOf(product.getStockQuantity()) : "0"
        );
        entity.set_Active(true);
        entity.set_Deleted(true);
        entity.setCreatedAt(LocalDateTime.now().toString());
        entity.setUpdatedAt(LocalDateTime.now().toString());
        ProductModel saved = repository.save(entity);
        return convertToDto(saved);
    }

    private ProductDto convertToDto(ProductModel entity) {
        return new ProductDto(
                entity.getProductName(),
                entity.getProductDesc(),
                entity.getProductCategory(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getSku(),
                entity.getStock_quantity() != null && !entity.getStock_quantity().isBlank()
                        ? Integer.parseInt(entity.getStock_quantity())
                        : 0,
                entity.getMain_image_url()
        );
    }

    private ProductModel convertToEntity(ProductDto dto) {
        ProductModel entity = new ProductModel();
        entity.setProductName(dto.getProductName());
        entity.setProductDesc(dto.getProductDesc());
        entity.setProductCategory(dto.getProductCategory());
        entity.setPrice(dto.getPrice());
        entity.setCurrency(dto.getCurrency());
        entity.setSku(dto.getSku());
        entity.setStock_quantity(String.valueOf(dto.getStockQuantity()));
        entity.setMain_image_url(dto.getMainImageUrl());
        return entity;
    }

    public ProductDto updateProduct(Integer productId, @Valid ProductDto productDto) {
        ProductModel existing = repository.findById(productId).orElse(null);

        if (existing == null) return null;

        existing.setProductName(productDto.getProductName());
            existing.setProductDesc(productDto.getProductDesc());
            existing.setProductCategory(productDto.getProductCategory());
            existing.setPrice(productDto.getPrice());
            existing.setCurrency(productDto.getCurrency());
            existing.setSku(productDto.getSku());
            existing.setStock_quantity(String.valueOf(productDto.getStockQuantity()));
            existing.setMain_image_url(productDto.getMainImageUrl());
            existing.setUpdatedAt(LocalDateTime.now().toString());

            ProductModel updatedProduct = repository.save(existing);
            return convertToDto(existing);
    }

    public boolean deleteProduct(Integer id) {
        return repository.findById(id).map(product ->{product.set_Deleted(true);
        repository.save(product);
        return true;
        }).orElse(false);
    }
}
