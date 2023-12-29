package ra.web_shop_modun05.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.Role;
import ra.web_shop_modun05.model.entity.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    @Query("SELECT u FROM User u WHERE NOT :role MEMBER OF u.roles AND u.fullName LIKE %:search%")
    Page<User> findAllByRole(String search, Role role, Pageable pageable);
}
