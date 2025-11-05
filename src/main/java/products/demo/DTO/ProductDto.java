package products.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import jdk.jfr.Percentage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    @NotBlank(message = "Product name is required")
    private String productName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String productDesc;

    private boolean dashboard;

    @Min(value = 0,message = "Discount cannot be negative")
    @Max(value = 100,message="Discount cannot be greater than 100")
//    @Percentage
    private Double discount;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

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

    @JsonProperty("_Active")
    private boolean is_Active;
    @JsonProperty("_Deleted")
    private boolean is_Deleted;
}
