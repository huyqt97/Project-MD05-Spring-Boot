package ra.web_shop_modun05.service.cartItem;

import org.springframework.security.core.Authentication;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.CartItemReq;
import ra.web_shop_modun05.model.dto.request.CartItemUpdateReq;
import ra.web_shop_modun05.model.dto.response.CartItemRes;
import ra.web_shop_modun05.model.entity.CartItem;

import java.util.List;

public interface CartItemService {
    List<CartItemRes> getAllByUser(Authentication authentication);
    CartItemRes addToCart(Authentication authentication, CartItemReq cartItemReq) throws NotEmptyCustomer;
    CartItemRes updateCart(Authentication authentication, CartItemUpdateReq cartItemUpdateReq) throws NotEmptyCustomer;
    String deleteById(Authentication authentication,Long id) throws NotEmptyCustomer;
    String deleteAll(Authentication authentication) throws NotEmptyCustomer;
    List<CartItem> findAllByIdUser(Long id) throws NotEmptyCustomer;
}
