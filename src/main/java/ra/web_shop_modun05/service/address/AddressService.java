package ra.web_shop_modun05.service.address;

import org.aspectj.weaver.ast.Not;
import org.springframework.security.core.Authentication;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.AddressReq;
import ra.web_shop_modun05.model.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> findAll(Authentication authentication);
    Address addAddress(AddressReq addressReq,Authentication authentication) throws NotEmptyCustomer;
    String deleteAddress(Authentication authentication, Long id) throws NotEmptyCustomer;
    Address findById(Authentication authentication, Long id)throws NotEmptyCustomer;
}
