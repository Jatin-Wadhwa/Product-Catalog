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
        return repository.findById(productId).map(this::convertToDto)
                .orElseThrow(()-> new ProductNotFoundException("Product with id"+productId+"not found"));
    }

    public ProductDto addProduct(@Valid ProductDto product) {
        ProductModel entity = convertToEntity(product);
        entity.setStock_quantity(
                product.getStockQuantity() != null ? String.valueOf(product.getStockQuantity()) : "0"
        );
        entity.set_Active(true);
        entity.set_Deleted(false);
        entity.setCreatedAt(LocalDateTime.now().toString());
        entity.setUpdatedAt(LocalDateTime.now().toString());
        ProductModel saved = repository.save(entity);
        return convertToDto(saved);
    }

    private CategoryDTO convertToDTO(Category category){
        if(category==null) return null;
        return new CategoryDTO(category.getId(), category.getName());
    }

    private ProductDto convertToDto(ProductModel entity) {
        return new ProductDto(
                entity.getId(),
                entity.getProductName(),
                entity.getProductDesc(),
                convertToDTO(entity.getCategory()),
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

    private Category convertToEntity(CategoryDTO dto){
        if(dto==null) return null;
        Category category=new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    private ProductModel convertToEntity(ProductDto dto) {
        ProductModel entity = new ProductModel();
        entity.setProductName(dto.getProductName());
        entity.setProductDesc(dto.getProductDesc());
        entity.setCategory(convertToEntity(dto.getCategory()));
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
//            existing.setCategory(productDto.getCategory());
            existing.setPrice(productDto.getPrice());
            existing.setCurrency(productDto.getCurrency());
            existing.setSku(productDto.getSku());
            existing.setStock_quantity(String.valueOf(productDto.getStockQuantity()));
            existing.setMain_image_url(productDto.getMainImageUrl());
            existing.setUpdatedAt(LocalDateTime.now().toString());

            if(productDto.getCategory()!=null && productDto.getCategory().getId()!=null){
                Category categoryModel= categoryRepo.findById(productDto.getCategory().getId())
                        .orElseThrow(()-> new ProductNotFoundException("Category not found with id: " + productDto.getCategory().getId()));
                existing.setCategory(categoryModel);
            }

            ProductModel updatedProduct = repository.save(existing);
            return convertToDto(existing);
    }

    public boolean deleteProduct(Integer id) {
        return repository.findById(id).map(product ->{product.set_Deleted(true);
            product.set_Active(false);
        repository.save(product);
        return true;
        }).orElse(false);
    }

    public List<ProductDto> getProducts(String name, Double price, String category,
                                        String sortBy,String direction, int page, int pageSize) {
        Sort sort=direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(page,pageSize,sort);

        System.out.println(pageable+"pageable");

        List<ProductModel> entities = repository.findAll(pageable).getContent();
        return entities
                .stream()
                .filter(p->(name==null || (p.getProductName()!=null &&  p.getProductName().toLowerCase().contains(name.toLowerCase()))))
                .filter(p->(price==null || (p.getPrice()!=null && p.getPrice()>=price)))
                .map(this::convertToDto).collect(Collectors.toList());
    }
}
