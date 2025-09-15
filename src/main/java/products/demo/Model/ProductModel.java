package products.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String productCategory;
    private String price;
    private String currency;
    private String sku;
    private String stock_quantity;
    private String main_image_url;
    private boolean is_Active;
    private boolean is_Deleted;
    private String updatedAt;
    private String createdAt;
}
