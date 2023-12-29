package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleName(String s);
}
