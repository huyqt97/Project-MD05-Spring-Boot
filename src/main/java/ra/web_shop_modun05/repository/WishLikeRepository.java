package ra.web_shop_modun05.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.web_shop_modun05.model.entity.User;
import ra.web_shop_modun05.model.entity.WishLike;

import java.util.List;

@Repository
public interface WishLikeRepository extends JpaRepository<WishLike,Long> {
    List<WishLike> findAllByUser(User user);

}
