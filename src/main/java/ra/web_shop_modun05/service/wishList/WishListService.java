package ra.web_shop_modun05.service.wishList;

import org.springframework.security.core.Authentication;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.entity.WishLike;

import java.util.List;

public interface WishListService {
    List<WishLike> findAll();

    String addToWishList(Authentication authentication, Long id)throws NotEmptyCustomer;
    List<WishLike> getAllByIdUser(Authentication authentication) throws NotEmptyCustomer;
    String deleteById(Authentication authentication,Long id) throws NotEmptyCustomer;
}
