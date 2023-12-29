package ra.web_shop_modun05.service.cartItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.CartItemReq;
import ra.web_shop_modun05.model.dto.request.CartItemUpdateReq;
import ra.web_shop_modun05.model.dto.response.CartItemRes;
import ra.web_shop_modun05.model.dto.response.OrderRes;
import ra.web_shop_modun05.model.entity.*;
import ra.web_shop_modun05.repository.CartItemRepository;
import ra.web_shop_modun05.repository.ProductRepository;
import ra.web_shop_modun05.security.user_principal.UserPrincipal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemSerImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartItemRes> getAllByUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<CartItemRes> item = new ArrayList<>();
        for (CartItem c : cartItemRepository.getAllByUserId(userPrincipal.getUser().getId())) {
            CartItemRes cartItemRes = CartItemRes.builder()
                    .id(c.getId())
                    .image(c.getProduct().getImage())
                    .price(c.getProduct().getPrice_export())
                    .productName(c.getProduct().getProduct_name())
                    .quantity(c.getQuantity())
                    .build();
            item.add(cartItemRes);
        }
        return item;
    }

    @Override
    public CartItemRes addToCart(Authentication authentication, CartItemReq cartItemReq) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            CartItem cartItem = cartItemRepository.findByIdUserAndIdProduct(user.getId(), cartItemReq.getProductId());
            if (cartItem == null) {
                Optional<Product> product = productRepository.findById(cartItemReq.getProductId());
                if (product.isPresent()) {
                    if (product.get().getQuantity() >= cartItemReq.getQuantity()) {
                        cartItem = CartItem.builder()
                                .quantity(cartItemReq.getQuantity())
                                .user(user)
                                .product(product.get()).build();
                        CartItem c = cartItemRepository.save(cartItem);
                        return CartItemRes.builder()
                                .id(c.getId())
                                .image(c.getProduct().getImage())
                                .price(c.getProduct().getPrice_export())
                                .productName(c.getProduct().getProduct_name())
                                .quantity(c.getQuantity())
                                .build();
                    }throw new NotEmptyCustomer("số lượng sản phẩm không đủ!");
                } else {
                    throw new NotEmptyCustomer("Sản phẩm không tồn tại!");
                }
            } else {
                Optional<Product> product = productRepository.findById(cartItemReq.getProductId());
                cartItem = CartItem.builder()
                        .quantity(cartItem.getQuantity() + cartItemReq.getQuantity()).build();
                if(product.isPresent() && product.get().getQuantity() >= cartItem.getQuantity()) {
                    CartItem c = cartItemRepository.save(cartItem);
                    return CartItemRes.builder()
                            .id(c.getId())
                            .image(c.getProduct().getImage())
                            .price(c.getProduct().getPrice_export())
                            .productName(c.getProduct().getProduct_name())
                            .quantity(c.getQuantity())
                            .build();
                }throw new NotEmptyCustomer("số lượng sản phẩm không đủ!");
            }
        }
        throw new NotEmptyCustomer("Người dùng không tồn tại!");
    }

    @Override
    public CartItemRes updateCart(Authentication authentication, CartItemUpdateReq cartItemUpdateReq) throws NotEmptyCustomer {
        Optional<CartItem> item = cartItemRepository.findById(cartItemUpdateReq.getIdCart());
        if(item.isPresent()){
            Product product = item.get().getProduct();
            if(product.getQuantity() >= cartItemUpdateReq.getQuantity()){
                CartItem cartItem = item.get();
                cartItem.setQuantity(cartItemUpdateReq.getQuantity());
                CartItem c = cartItemRepository.save(cartItem);
                return CartItemRes.builder()
                        .id(c.getId())
                        .image(c.getProduct().getImage())
                        .price(c.getProduct().getPrice_export())
                        .productName(c.getProduct().getProduct_name())
                        .quantity(c.getQuantity())
                        .build();
            }throw new NotEmptyCustomer("số lượng sản phẩm không đủ!");
        }throw new NotEmptyCustomer("cartItem không tồn tại!");
    }

    @Override
    public String deleteById(Authentication authentication, Long id) throws NotEmptyCustomer{
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        Optional<CartItem> item = cartItemRepository.findById(id);
        if (item.isPresent()) {
            if(item.get().getUser().getId().equals(user.getId())){
            cartItemRepository.deleteById(id);
            return "Xóa thành công " + item;
            }throw new NotEmptyCustomer("Không thẻ xóa!");
        }throw new NotEmptyCustomer("không tìm thấy cartItem!");
    }

    @Override
    public String deleteAll(Authentication authentication) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if(cartItemRepository.existsByIdUser(user.getId())){
            cartItemRepository.deleteAllByIdUser(user.getId());
            return "Xóa thành công!";
        }throw new NotEmptyCustomer("Giỏ hàng trống!");
    }

    @Override
    public List<CartItem> findAllByIdUser(Long id) throws NotEmptyCustomer {
        return cartItemRepository.getAllByUserId(id);
    }
}
