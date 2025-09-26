package products.demo.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    @NotBlank(message = "Product name is required")
    private String productName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String productDesc;

    @NotBlank(message = "Category is required")
    private CategoryDTO category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "SKU is required")
    private String sku;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String mainImageUrl;

    private boolean is_Active;
    private boolean is_Deleted;
}
