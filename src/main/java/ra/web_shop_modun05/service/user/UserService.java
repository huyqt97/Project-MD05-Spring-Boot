package ra.web_shop_modun05.service.user;

import org.springframework.security.core.Authentication;
import ra.web_shop_modun05.exception.BlockUser;
import ra.web_shop_modun05.exception.LoginException;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.exception.RegisterException;
import ra.web_shop_modun05.model.dto.request.UserLoginReq;
import ra.web_shop_modun05.model.dto.request.UserRegisterReq;
import ra.web_shop_modun05.model.dto.request.UserUpdatePasswordReq;
import ra.web_shop_modun05.model.dto.request.UserUpdateReq;
import ra.web_shop_modun05.model.dto.response.UserRes;

import java.util.List;

public interface UserService {
    String register(UserRegisterReq userRegisterReq) throws RegisterException;
    List<UserRes> findAll();
    UserRes login(UserLoginReq userLoginReq) throws LoginException, BlockUser;
    UserRes profile(Authentication authentication) throws NotEmptyCustomer;
    UserRes updateProfile(Authentication authentication, UserUpdateReq userUpdateReq) throws NotEmptyCustomer;
    String updatePassWord(Authentication authentication, UserUpdatePasswordReq userUpdatePasswordReq) throws NotEmptyCustomer;
    List<UserRes> findAllShow(String search,String filed,String sort,Integer page,Integer limit)throws NotEmptyCustomer;
    String editStatus(Authentication authentication,Long id)throws NotEmptyCustomer;
}
