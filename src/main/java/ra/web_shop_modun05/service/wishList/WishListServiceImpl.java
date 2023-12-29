package ra.web_shop_modun05.service.wishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.entity.Product;
import ra.web_shop_modun05.model.entity.User;
import ra.web_shop_modun05.model.entity.WishLike;
import ra.web_shop_modun05.repository.ProductRepository;
import ra.web_shop_modun05.repository.WishLikeRepository;
import ra.web_shop_modun05.security.user_principal.UserPrincipal;

import java.util.List;
import java.util.Optional;

@Service
public class WishListServiceImpl implements WishListService {
    @Autowired
    private WishLikeRepository wishLikeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<WishLike> findAll() {
        return wishLikeRepository.findAll();
    }

    @Override
    public String addToWishList(Authentication authentication, Long id) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                WishLike wishLike = WishLike.builder()
                        .user(user)
                        .productW(product.get()).build();
                wishLikeRepository.save(wishLike);
                return "Đã thêm {" + product.get().getProduct_name() + "} vào danh mục yêu thích!";
            }
            throw new NotEmptyCustomer("Sản phẩm không tồn tại!");
        }
        throw new NotEmptyCustomer("Chưa đăng nhập!");
    }

    @Override
    public List<WishLike> getAllByIdUser(Authentication authentication) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            return wishLikeRepository.findAllByUser(user);
        }
        throw new NotEmptyCustomer("Chưa đăng nhập!");
    }

    @Override
    public String deleteById(Authentication authentication, Long id) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            Optional<WishLike> wishLike = wishLikeRepository.findById(id);
            if (wishLike.isPresent()) {
                if (wishLike.get().getUser().getId().equals(user.getId())){
                    wishLikeRepository.deleteById(id);
                    return "Xóa thành công!";
                }throw new NotEmptyCustomer("Không xóa được thư mục này !");
            }throw new NotEmptyCustomer("ID không tồn tại!");
        }
        throw new NotEmptyCustomer("Chưa đăng nhập!");
    }
}
