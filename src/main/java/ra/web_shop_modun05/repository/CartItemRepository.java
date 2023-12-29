package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.CartItem;
import ra.web_shop_modun05.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT c FROM CartItem c JOIN c.user u WHERE u.id = :idUser")
    List<CartItem> getAllByUserId(Long idUser);
    @Query("SELECT c FROM CartItem  c JOIN c.user u JOIN c.product p WHERE u.id = :idUser AND p.id = :idProduct")
    CartItem findByIdUserAndIdProduct(Long idUser, Long idProduct);
    @Query("SELECT COUNT(c) > 0 FROM CartItem c JOIN c.user u WHERE u.id = :idUser")
    boolean existsByIdUser(@Param("idUser") Long idUser);
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :idUser")
    void deleteAllByIdUser(@Param("idUser") Long idUser);
    boolean existsByUser(User user);
}
