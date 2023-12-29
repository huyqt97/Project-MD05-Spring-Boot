package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query("SELECT od FROM OrderDetail od JOIN od.order o WHERE o.id = :idOrder")
    List<OrderDetail> getAllByIdOrder(@Param("idOrder") Long idOrder);

    void deleteAllByOrderId(Long id);
}
