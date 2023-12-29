package ra.web_shop_modun05.service.orderDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.response.OrderDetailRes;
import ra.web_shop_modun05.model.entity.OrderDetail;
import ra.web_shop_modun05.model.entity.Orders;
import ra.web_shop_modun05.repository.OrderDetailRepository;
import ra.web_shop_modun05.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDetailRes> orderDetailByIdOrder(Long id) throws NotEmptyCustomer {
        List<OrderDetailRes> orderDetails = new ArrayList<>();
        Optional<Orders> orders = orderRepository.findById(id);
        if (orders.isPresent()) {
            for (OrderDetail od : orderDetailRepository.getAllByIdOrder(id)) {
                OrderDetailRes o = OrderDetailRes.builder()
                        .id(od.getId())
                        .order_quantity(od.getOrder_quantity())
                        .product_name(od.getProduct_name())
                        .unit_price(od.getUnit_price())
                        .build();
                orderDetails.add(o);
            }
            return orderDetails;
        }
        throw new NotEmptyCustomer("Đơn hàng không tồn tại!");
    }
}
