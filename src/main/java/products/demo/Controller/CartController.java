package products.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import products.demo.DTO.ApiResponse;
import products.demo.DTO.CartRequestDTO;
import products.demo.Model.CartModel;
import products.demo.Service.CartService;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService service;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartModel>>> getUserCart(@PathVariable Integer userId){
        List<CartModel> cart=service.getUserCart(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Cart fetched successfully",cart));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<CartModel>>> addMultipleCart(@RequestBody CartRequestDTO request){
        List<CartModel> addedItems=service.addMultipleToCart(request.getUserId(), request.getItems());
        return ResponseEntity.ok(new ApiResponse<>(true,"products added/updated in cart", addedItems));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteFromCart(@RequestParam Integer userId,@RequestParam Integer productId){
        service.removeFromCart(userId,productId);
        return ResponseEntity.ok(new ApiResponse<>(true,"removed from cart",null));
    }
}
