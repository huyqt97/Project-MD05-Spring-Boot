package ra.web_shop_modun05.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.Category;
import ra.web_shop_modun05.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
    @Query("SELECT p FROM Product p JOIN p.category ca WHERE ca.id = :idCategory")
    List<Product> getProductByCategoriesId(Long idCategory);
    @Query("SELECT p FROM Product p WHERE LOWER(p.product_name) LIKE %:search% OR LOWER(p.description) LIKE %:search%")
    Page<Product> findAllBySearch(@Param("search") String search, Pageable pageable);
    List<Product> findAllByCategory(Category category);
}
