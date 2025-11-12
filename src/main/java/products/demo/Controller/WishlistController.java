package products.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import products.demo.DTO.ApiResponse;
import products.demo.Model.WishlistModel;
import products.demo.Service.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService service;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<WishlistModel>>> getUserWishlist(@PathVariable Integer userId){
        List<WishlistModel> wishlist=service.getUserWishlist(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Wishlist fetched successfully",wishlist));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistModel>> addToWishlist(@RequestParam Integer userId, @RequestParam Integer productId){
        WishlistModel wishlistItem= service.addToWishlist(userId,productId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Product added to wishlist",wishlistItem));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removeFromWishlist(@RequestParam Integer userId, @RequestParam Integer productId) {
        service.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product Removed From Wishlist", null));
    }
}
