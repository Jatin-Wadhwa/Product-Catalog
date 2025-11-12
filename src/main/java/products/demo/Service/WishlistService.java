package products.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import products.demo.Model.ProductModel;
import products.demo.Model.User;
import products.demo.Model.WishlistModel;
import products.demo.Repo.UserRepo;
import products.demo.Repo.WishlistRepo;
import products.demo.Repo.productRepo;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private productRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public List<WishlistModel> getUserWishlist(Integer userId){
        User user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("user not found"));
        return wishlistRepo.findByUser(user);
    }

    public WishlistModel addToWishlist(Integer userId, Integer productId){
        if(wishlistRepo.existsByUserIdAndProductId(userId,productId)){
            throw new RuntimeException("Product already exists in wishlist");
        }

        User user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("user not found"));

        ProductModel product=productRepo.findById(productId)
                .orElseThrow(()->new RuntimeException("Product not found"));

        WishlistModel wishlist=new WishlistModel();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        return wishlistRepo.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Integer userId,Integer productId){
        wishlistRepo.deleteByUserIdAndProductId(userId,productId);
    }
}
