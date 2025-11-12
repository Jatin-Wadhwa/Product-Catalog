package products.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "category name is required")
    private String name;
    @NotBlank(message = "Image Link required")
    private String main_image_url;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)//CascadeType.ALL Changes made to a Category (like saving or deleting) will cascade to its associated products automatically.
    @JsonIgnore
    private List<ProductModel> products;


}
