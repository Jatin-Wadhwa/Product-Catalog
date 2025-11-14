package products.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CartRequestDTO {
    private Integer userId;
    private List<CartItemDTO> items;

    @Data
    public static class CartItemDTO{
        private Integer productId;
        private Integer quantity;
    }
}
