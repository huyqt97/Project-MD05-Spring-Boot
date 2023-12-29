package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
