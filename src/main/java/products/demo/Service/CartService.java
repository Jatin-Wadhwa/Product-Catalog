package products.demo.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import products.demo.DTO.CartRequestDTO;
import products.demo.Model.CartModel;
import products.demo.Model.ProductModel;
import products.demo.Model.User;
import products.demo.Repo.CartRepo;
import products.demo.Repo.UserRepo;
import products.demo.Repo.productRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private productRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public List<CartModel> getUserCart(Integer userId){
        User user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("user not found"));
        return cartRepo.findByUser(user);
    }

    @Transactional
    public List<CartModel> addMultipleToCart(Integer userId, List<CartRequestDTO.CartItemDTO> items){
        User user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("user not found"));

        List<CartModel> addedItems=new ArrayList<>();

        for(CartRequestDTO.CartItemDTO item:items){
            ProductModel product=productRepo.findById(item.getProductId())
                    .orElseThrow(()-> new RuntimeException("Product not found"+item.getProductId()));

            var existing= cartRepo.findByUserIdAndProductId(userId,item.getProductId());
            if(existing.isPresent()){
                CartModel existingItem=existing.get();
                existingItem.setQuantity(existingItem.getQuantity()+(item.getQuantity()!=null?item.getQuantity():1));
                cartRepo.save(existingItem);
                continue;
            }

            CartModel cartItem=new CartModel();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(item.getQuantity()!=null?item.getQuantity():1);
            addedItems.add(cartItem);
        }
        return cartRepo.saveAll(addedItems);
    }

    @Transactional
    public void removeFromCart(Integer userId,Integer productId){
        cartRepo.deleteByUserIdAndProductId(userId,productId);
    }
}
