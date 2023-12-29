package ra.web_shop_modun05.service.category;

import ra.web_shop_modun05.exception.CategoryException;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.CategoryReq;
import ra.web_shop_modun05.model.dto.response.CategoryRes;
import ra.web_shop_modun05.model.dto.response.ProductRes;
import ra.web_shop_modun05.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryRes> findAll();
    List<ProductRes> findById(Long id) throws CategoryException;
    CategoryRes save(CategoryReq categoryReq);
    String delete(Long id) throws NotEmptyCustomer;
}
