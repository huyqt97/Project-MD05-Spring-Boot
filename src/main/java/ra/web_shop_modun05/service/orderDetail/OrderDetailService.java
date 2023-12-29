package ra.web_shop_modun05.service.orderDetail;

import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.response.OrderDetailRes;
import ra.web_shop_modun05.model.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailRes> orderDetailByIdOrder(Long id) throws NotEmptyCustomer;
}
