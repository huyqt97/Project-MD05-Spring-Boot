package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.Orders;
import ra.web_shop_modun05.model.entity.StatusOrder;

import java.util.LinkedList;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    @Query("SELECT o FROM Orders o JOIN o.user u WHERE u.id = :idUser AND o.statusOrder = :statusOrder")
    List<Orders> getAllOrderByIdUser(@Param("idUser") Long idUser, @Param("statusOrder") StatusOrder statusOrder);
    @Query("SELECT o FROM Orders o WHERE o.statusOrder = :statusOrder")
    List<Orders> getAllByStatus( @Param("statusOrder") StatusOrder statusOrder);
}