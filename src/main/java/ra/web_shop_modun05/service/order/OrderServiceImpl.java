package ra.web_shop_modun05.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.OrderReq;
import ra.web_shop_modun05.model.dto.response.OrderRes;
import ra.web_shop_modun05.model.entity.*;
import ra.web_shop_modun05.repository.*;
import ra.web_shop_modun05.security.user_principal.UserPrincipal;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public OrderRes addToOrder(Authentication authentication, OrderReq orderReq) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (cartItemRepository.existsByUser(user)) {
            Optional<Address> address = addressRepository.findById(orderReq.getAddressId());
            if (address.isPresent()) {
                Set<OrderDetail> orderDetails = new HashSet<>();
                Orders orders = new Orders();
                orders.setStatusOrder(StatusOrder.WAITING);
                orders.setCreate_at(new Date());
                orders.setNote(orderReq.getNote());
                orders.setAddress(addressRepository.findById(orderReq.getAddressId()).orElse(null));
                orders.setUser(user);

                Orders savedOrder = orderRepository.save(orders);

                double sumPrice = 0;

                for (CartItem c : cartItemRepository.getAllByUserId(user.getId())) {
                    Product product = c.getProduct();
                    if (product == null) {
                        throw new NotEmptyCustomer("Sản phẩm không tồn tại!");
                    }

                    sumPrice += c.getQuantity() * product.getPrice_export();

                    OrderDetail orderDetail = OrderDetail.builder()
                            .order_quantity(c.getQuantity())
                            .product_name(product.getProduct_name())
                            .unit_price(c.getQuantity() * product.getPrice_export())
                            .order(savedOrder)
                            .product(product)
                            .build();

                    orderDetails.add(orderDetail);
                    orderDetailRepository.save(orderDetail);
                    cartItemRepository.delete(c);
                }

                savedOrder.setTotal_price(sumPrice);
                savedOrder.setOrderDetails(orderDetails);
                Orders o = orderRepository.save(savedOrder);
                return OrderRes.builder()
                        .id(o.getId())
                        .receiver(o.getAddress().getReceiver())
                        .phone(o.getAddress().getPhone())
                        .full_address(o.getAddress().getFull_address())
                        .total_price(o.getTotal_price())
                        .note(o.getNote())
                        .create_at(o.getCreate_at())
                        .statusOrder(o.getStatusOrder())
                        .build();
            }
            throw new NotEmptyCustomer("Địa chỉ trống!");
        }
        throw new NotEmptyCustomer("giỏ hàng trống!");

    }

    @Override
    public List<OrderRes> getAllHistoryOrderByIdUser(Authentication authentication) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            List<OrderRes> orderRes = new ArrayList<>();
            boolean check = false;
            for (Orders o : orderRepository.getAllOrderByIdUser(user.getId(), StatusOrder.WAITING)) {
                OrderRes or = OrderRes.builder()
                        .id(o.getId())
                        .receiver(o.getAddress().getReceiver())
                        .phone(o.getAddress().getPhone())
                        .full_address(o.getAddress().getFull_address())
                        .total_price(o.getTotal_price())
                        .note(o.getNote())
                        .create_at(o.getCreate_at())
                        .statusOrder(o.getStatusOrder())
                        .build();
                orderRes.add(or);
                check = true;
            }
            if (!check) throw new NotEmptyCustomer("Lịch sử trống!");
            return orderRes;
        }
        throw new NotEmptyCustomer("Người dùng không tồn tại!");
    }

    @Override
    public OrderRes findById(Long id) throws NotEmptyCustomer {
        Optional<Orders> orders = orderRepository.findById(id);
        if (orders.isPresent()) {
            return OrderRes.builder()
                    .id(orders.get().getId())
                    .receiver(orders.get().getAddress().getReceiver())
                    .phone(orders.get().getAddress().getPhone())
                    .full_address(orders.get().getAddress().getFull_address())
                    .total_price(orders.get().getTotal_price())
                    .note(orders.get().getNote())
                    .create_at(orders.get().getCreate_at())
                    .statusOrder(orders.get().getStatusOrder())
                    .build();
        }
        throw new NotEmptyCustomer("Đơn hàng không tồn tại!");
    }

    @Override
    public List<OrderRes> getAllByStatus(String status, Authentication authentication) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            if (statusOrder(status) != null) {
                List<OrderRes> orderRes = new ArrayList<>();
                boolean check = false;
                for (Orders o : orderRepository.getAllOrderByIdUser(user.getId(), statusOrder(status))) {
                    OrderRes or = OrderRes.builder()
                            .id(o.getId())
                            .receiver(o.getAddress().getReceiver())
                            .phone(o.getAddress().getPhone())
                            .full_address(o.getAddress().getFull_address())
                            .total_price(o.getTotal_price())
                            .note(o.getNote())
                            .create_at(o.getCreate_at())
                            .statusOrder(o.getStatusOrder())
                            .build();
                    orderRes.add(or);
                    check = true;
                }
                if (!check) throw new NotEmptyCustomer("Không có đơn hàng nào trống!");
                return orderRes;
            }
            throw new NotEmptyCustomer("Không xác định trạng thái!");
        }
        throw new NotEmptyCustomer("Người dùng không tồn tại!");
    }

    public StatusOrder statusOrder(String status) {
        return switch (status) {
            case "waiting" -> StatusOrder.WAITING;
            case "confirm" -> StatusOrder.CONFIRM;
            case "success" -> StatusOrder.SUCCESS;
            case "cancel" -> StatusOrder.CANCEL;
            default -> null;
        };
    }

    @Override
    @Transactional
    public String cancelOrder(Authentication authentication, Long id) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        Optional<Orders> orders = orderRepository.findById(id);
        if (orders.isPresent()) {
            orderDetailRepository.deleteAllByOrderId(id);
//            deleteOrderDetail(id);
            if (user != null && user.getId().equals(orders.get().getUser().getId())) {
                orderRepository.delete(orders.get());
                return "Hủy đơn thành công!";
            }
            return "Không được phép xóa!";
        }
        throw new NotEmptyCustomer("Order Không tồn tại!");
    }

    public void deleteOrderDetail(Long id) {
        for (OrderDetail od:orderDetailRepository.getAllByIdOrder(id)) {
            if(od.getOrder().getId().equals(id)){
                orderDetailRepository.delete(od);
            }
        }
    }

    @Override
    public List<OrderRes> getAll() {
        List<OrderRes> orderRes = new ArrayList<>();
        for (Orders o : orderRepository.findAll()) {
            OrderRes or = OrderRes.builder()
                    .id(o.getId())
                    .receiver(o.getAddress().getReceiver())
                    .phone(o.getAddress().getPhone())
                    .full_address(o.getAddress().getFull_address())
                    .total_price(o.getTotal_price())
                    .note(o.getNote())
                    .create_at(o.getCreate_at())
                    .statusOrder(o.getStatusOrder())
                    .build();
            orderRes.add(or);
        }
        return orderRes;
    }

    @Override
    public List<OrderRes> getAllOrderByStatus(String status) {
        List<OrderRes> orderRes = new ArrayList<>();
        for (Orders o : orderRepository.getAllByStatus(statusOrder(status))) {
            OrderRes or = OrderRes.builder()
                    .id(o.getId())
                    .receiver(o.getAddress().getReceiver())
                    .phone(o.getAddress().getPhone())
                    .full_address(o.getAddress().getFull_address())
                    .total_price(o.getTotal_price())
                    .note(o.getNote())
                    .create_at(o.getCreate_at())
                    .statusOrder(o.getStatusOrder())
                    .build();
            orderRes.add(or);
        }
        return orderRes;
    }

    @Override
    @Transactional
    public OrderRes confirm(Long id) throws NotEmptyCustomer {
        Optional<Orders> ordersOptional = orderRepository.findById(id);

        if (ordersOptional.isPresent() && ordersOptional.get().getStatusOrder().equals(StatusOrder.WAITING)) {
            Orders order = ordersOptional.get();
            order.setStatusOrder(StatusOrder.CONFIRM);
            order.setCreate_at(new Date());
            Product product = findByProduct(order.getOrderDetails());

            if (product != null) {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    product.setQuantity(product.getQuantity() - orderDetail.getOrder_quantity());
                    product.setSold(product.getSold() + orderDetail.getOrder_quantity());
                }
                productRepository.save(product);

                orderRepository.save(order);

                return OrderRes.builder()
                        .id(order.getId())
                        .receiver(order.getAddress().getReceiver())
                        .phone(order.getAddress().getPhone())
                        .full_address(order.getAddress().getFull_address())
                        .total_price(order.getTotal_price())
                        .note(order.getNote())
                        .create_at(order.getCreate_at())
                        .statusOrder(order.getStatusOrder())
                        .build();
            }

            throw new NotEmptyCustomer("Không thành công!");
        }

        throw new NotEmptyCustomer("Đơn hàng không tồn tại hoặc không ở trạng thái chờ xác nhận!");
    }

    public Product findByProduct(Set<OrderDetail> orderDetail) {
        for (OrderDetail od : orderDetail) {
            return od.getProduct();
        }
        return null;
    }

    @Override
    public String cancel(Long id) throws NotEmptyCustomer {
        Optional<Orders> orders = orderRepository.findById(id);
        if (orders.isPresent()) {
            Orders o = orders.get();
            o.setStatusOrder(StatusOrder.CANCEL);
            orderRepository.save(o);
            return "đã huỷ đơn hàng!";
        }
        throw new NotEmptyCustomer("đơn hàng không tồn tại!");
    }
}
