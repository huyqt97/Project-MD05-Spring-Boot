package ra.web_shop_modun05.service.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.AddressReq;
import ra.web_shop_modun05.model.entity.Address;
import ra.web_shop_modun05.model.entity.User;
import ra.web_shop_modun05.repository.AddressRepository;
import ra.web_shop_modun05.security.user_principal.UserPrincipal;

import java.util.List;
import java.util.Optional;

@Service
public class addressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> findAll(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        return addressRepository.findAllByUserId(user.getId());
    }

    @Override
    public Address addAddress(AddressReq addressReq, Authentication authentication) throws NotEmptyCustomer{
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user!= null){
            Address address = Address.builder()
                    .full_address(addressReq.getFull_address())
                    .receiver(addressReq.getReceiver())
                    .phone(addressReq.getPhone())
                    .user(user)
                    .build();
            return addressRepository.save(address);
        }
        throw new NotEmptyCustomer("ngươi dùng không tồn tại!");
    }

    @Override
    public String deleteAddress(Authentication authentication, Long id){
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        User user = userPrincipal.getUser();
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()){
            if(user.getId() == address.get().getUser().getId()){
                addressRepository.delete(address.get());
                return "xóa thành công!";
            }else {
                return "không xóa được!";
            }
        } else {
          return "Xóa không thành công!";
        }
    }

    @Override
    public Address findById(Authentication authentication, Long id) throws NotEmptyCustomer {
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()){
            return address.get();
        }
        throw new NotEmptyCustomer("không tìm thấy!");
    }
}
