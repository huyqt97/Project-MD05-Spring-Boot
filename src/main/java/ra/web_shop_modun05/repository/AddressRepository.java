package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.Address;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query("SELECT ad FROM Address ad JOIN ad.user u where u.id = :idUser")
    List<Address> findAllByUserId(Long idUser);
}
