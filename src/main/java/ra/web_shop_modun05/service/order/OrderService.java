package ra.web_shop_modun05.service.order;

import org.springframework.security.core.Authentication;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.OrderReq;
import ra.web_shop_modun05.model.dto.response.OrderRes;
import ra.web_shop_modun05.model.entity.StatusOrder;

import java.util.List;

public interface OrderService {
    OrderRes addToOrder(Authentication authentication, OrderReq orderReq) throws NotEmptyCustomer;
    List<OrderRes> getAllHistoryOrderByIdUser(Authentication authentication) throws NotEmptyCustomer;
    List<OrderRes> getAllByStatus(String status,Authentication authentication)throws NotEmptyCustomer;
    OrderRes findById(Long id) throws NotEmptyCustomer;
    String cancelOrder(Authentication authentication, Long id)throws NotEmptyCustomer;
    List<OrderRes> getAll();
    List<OrderRes> getAllOrderByStatus(String status);
    OrderRes confirm(Long id) throws NotEmptyCustomer;
    String cancel(Long id)throws NotEmptyCustomer;
}
