package products.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import products.demo.Config.ProductDetailsConverter;
import products.demo.Config.SubImagesConvertor;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String productName;
    private String productDesc;
    private boolean dashboard;
    private Double discount;
//    private String productCategory;
    private Double price;
    private String currency;
    private String sku;
    private String stock_quantity;
    private String main_image_url;
    private boolean is_Active;
    private boolean is_Deleted;
    private String updatedAt;
    private String createdAt;
//    private List<String> reviews;

    @Column(columnDefinition = "JSON")
    @Convert(converter = SubImagesConvertor.class)
    private List<String> subImages;

    @ManyToOne
    @JoinColumn(name="category_id")//Creates a foreign key in the products table pointing to categories.id
    private Category category;

    @Column(columnDefinition = "JSON")
    @Convert(converter = ProductDetailsConverter.class)
    private List<Map<String,String>> productDetails;
}
