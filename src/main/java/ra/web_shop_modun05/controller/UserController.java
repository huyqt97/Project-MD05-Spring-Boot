package ra.web_shop_modun05.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.*;
import ra.web_shop_modun05.model.dto.response.CartItemRes;
import ra.web_shop_modun05.model.dto.response.OrderDetailRes;
import ra.web_shop_modun05.model.dto.response.OrderRes;
import ra.web_shop_modun05.model.dto.response.UserRes;
import ra.web_shop_modun05.model.entity.Address;
import ra.web_shop_modun05.model.entity.WishLike;
import ra.web_shop_modun05.service.address.AddressService;
import ra.web_shop_modun05.service.cartItem.CartItemService;
import ra.web_shop_modun05.service.order.OrderService;
import ra.web_shop_modun05.service.orderDetail.OrderDetailService;
import ra.web_shop_modun05.service.user.UserService;
import ra.web_shop_modun05.service.wishList.WishListService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WishListService wishListService;
    @Autowired
    private OrderDetailService orderDetailService;
    //    Thông tin tài khoản người dùng theo id
    @GetMapping("/profile")
    public ResponseEntity<UserRes> profile(Authentication authentication) throws NotEmptyCustomer {
        return new ResponseEntity<>(userService.profile(authentication), HttpStatus.OK);
    }

    //    Cập nhật thông tin người dùng
    @PostMapping("/profile")
    public ResponseEntity<UserRes> update(Authentication authentication, @Valid @RequestBody UserUpdateReq userUpdateReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(userService.updateProfile(authentication, userUpdateReq), HttpStatus.OK);
    }

    //    Thay đổi mật khẩu (payload : oldPass, newPass, confirmNewPass)
    @PostMapping("/profile/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication, @Valid @RequestBody UserUpdatePasswordReq userUpdatePasswordReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(userService.updatePassWord(authentication, userUpdatePasswordReq), HttpStatus.OK);
    }

    ;

    //    lấy tất cả địa chỉ theo id user
    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAllAddress(Authentication authentication) {
        return new ResponseEntity<>(addressService.findAll(authentication), HttpStatus.OK);
    }

    //    lấy địa chỉ  người dùng theo idAddress
    @GetMapping("/address/{id}")
    public ResponseEntity<Address> getById(Authentication authentication, @PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(addressService.findById(authentication, id), HttpStatus.OK);
    }

    //    thêm mới địa chỉ
    @PostMapping("/address")
    public ResponseEntity<Address> addAddress(Authentication authentication, @Valid @RequestBody AddressReq addressReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(addressService.addAddress(addressReq, authentication), HttpStatus.OK);
    }

    //    xóa địa chỉ
    // lỗi
    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(Authentication authentication, @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(addressService.deleteAddress(authentication, id), HttpStatus.OK);
    }

    //    danh sách sản phẩm trong giỏ hàng
    @GetMapping("/shopping-cart")
    public ResponseEntity<List<CartItemRes>> cart(Authentication authentication) {
        return new ResponseEntity<>(cartItemService.getAllByUser(authentication), HttpStatus.OK);
    }

    //    Thêm mới sản phẩm vào giỏ hàng (payload: productId and quantity)
    @PostMapping("/shopping-cart")
    public ResponseEntity<CartItemRes> addToCart(Authentication authentication, @Valid @RequestBody CartItemReq cartItemReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(cartItemService.addToCart(authentication, cartItemReq), HttpStatus.OK);
    }

    //    Thay đổi số lượng đặt hàng của 1 sản phẩm  (payload :quantity)
    @PostMapping("/shopping-cart/update")
    public ResponseEntity<CartItemRes> updateCart(Authentication authentication, @Valid @RequestBody CartItemUpdateReq cartItemUpdateReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(cartItemService.updateCart(authentication, cartItemUpdateReq), HttpStatus.OK);
    }

    //    Xóa 1 sản phẩm trong giỏ hàng
    @DeleteMapping("/shopping-cart/{id}")
    public ResponseEntity<String> deleteById(Authentication authentication, @PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(cartItemService.deleteById(authentication, id), HttpStatus.OK);
    }

    //    Xóa toàn bộ sản phẩm trong giỏ hàng
    @DeleteMapping("/shopping-cart")
    public ResponseEntity<String> deleteAllCart(Authentication authentication) throws NotEmptyCustomer {
        return new ResponseEntity<>(cartItemService.deleteAll(authentication), HttpStatus.LOOP_DETECTED);
    }

    //    đặt hàng
    @PostMapping("/shopping-cart/checkout")
    public ResponseEntity<OrderRes> checkOut(Authentication authentication, @RequestBody OrderReq orderReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(orderService.addToOrder(authentication, orderReq), HttpStatus.OK);
    }

    //    lấy ra danh sách lịch sử mua hàng
    @GetMapping("/history")
    public ResponseEntity<List<OrderRes>> getHistoryOrder(Authentication authentication) throws NotEmptyCustomer {
        return new ResponseEntity<>(orderService.getAllHistoryOrderByIdUser(authentication), HttpStatus.OK);
    }

    //    lấy ra  chi tiết đơn hàng theo số id
    @GetMapping("/history/{id}/get-order-detail")
    public ResponseEntity<List<OrderDetailRes>> getByIdOrder(@PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(orderDetailService.orderDetailByIdOrder(id), HttpStatus.OK);
    }

    //    lấy ra danh sách lịch sử đơn hàng theo trạng thái đơn hàng
    @GetMapping("/history/{orderStatus}")
    public ResponseEntity<List<OrderRes>> getAllByStatus(@RequestParam String orderStatus, Authentication authentication) throws NotEmptyCustomer {
        return new ResponseEntity<>(orderService.getAllByStatus(orderStatus, authentication), HttpStatus.OK);
    }

    //    Hủy đơn hàng đang trong trạng thái chờ xác nhận
    @DeleteMapping("/history/{id}/cancel")
    public ResponseEntity<String> cancelOrder(Authentication authentication, @PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(orderService.cancelOrder(authentication, id), HttpStatus.OK);
    }

    //    thêm mới 1 sản phẩm vào danh sách yêu thích
    @PostMapping("/wish-list/{id}")
    public ResponseEntity<String> addWishList(Authentication authentication, @PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(wishListService.addToWishList(authentication, id), HttpStatus.OK);
    }

    //    Lấy ra danh sách yêu thích
    @GetMapping("/wish-list")
    public ResponseEntity<List<WishLike>> getAllWishList(Authentication authentication) throws NotEmptyCustomer {
        return new ResponseEntity<>(wishListService.getAllByIdUser(authentication), HttpStatus.OK);
    }
    @GetMapping("/wish-lists")
    public ResponseEntity<List<WishLike>> findAll() throws NotEmptyCustomer {
        return new ResponseEntity<>(wishListService.findAll(), HttpStatus.OK);
    }

    ;

    //    xóa sản phẩm ra khỏi danh sách yêu thích
    @DeleteMapping("/wish-list/{id}")
    public ResponseEntity<String> deleteByIdWishList(Authentication authentication, @PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(wishListService.deleteById(authentication, id), HttpStatus.OK);
    }
}
