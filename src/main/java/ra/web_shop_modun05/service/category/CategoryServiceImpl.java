package ra.web_shop_modun05.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.CategoryException;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.CategoryReq;
import ra.web_shop_modun05.model.dto.response.CategoryRes;
import ra.web_shop_modun05.model.dto.response.ProductRes;
import ra.web_shop_modun05.model.entity.Category;
import ra.web_shop_modun05.model.entity.Product;
import ra.web_shop_modun05.repository.CategoryRepository;
import ra.web_shop_modun05.repository.OrderDetailRepository;
import ra.web_shop_modun05.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CategoryRes> findAll() {
        List<CategoryRes> categoryResList = new ArrayList<>();
        for (Category c : categoryRepository.findAll()) {
            if (c.getIs_delete()) {
                CategoryRes categoryRes = CategoryRes.builder()
                        .id(c.getId())
                        .category_name(c.getCategory_name())
                        .build();
                categoryResList.add(categoryRes);
            }
        }
        return categoryResList;
    }

    @Override
    public List<ProductRes> findById(Long id) throws CategoryException {
        Optional<Category> category = categoryRepository.findById(id);
        List<ProductRes> productResList = new ArrayList<>();
        if (category.isPresent()) {
            for (Product p : productRepository.findAllByCategory(category.get())) {
                ProductRes productRes = ProductRes.builder()
                        .id(p.getId())
                        .product_name(p.getProduct_name())
                        .description(p.getDescription())
                        .image(p.getImage())
                        .price_import(p.getPrice_import())
                        .price_export(p.getPrice_export())
                        .quantity(p.getQuantity())
                        .sold(p.getSold())
                        .brand(p.getBrand().getBrandName())
                        .category(p.getCategory().getCategory_name())
                        .status(p.isStatus()).build();
                productResList.add(productRes);
            }
            return productResList;
        }
        throw new CategoryException("Không tìm thấy loại sản phẩm!");
    }

    @Override
    public CategoryRes save(CategoryReq categoryReq) {
        Category c = Category.builder()
                .category_name(categoryReq.getCategory_name())
                .is_delete(true)
                .build();
        Category category = categoryRepository.save(c);
        return CategoryRes.builder()
                .id(category.getId())
                .category_name(category.getCategory_name())
                .build();
    }

    @Override
    public String delete(Long id) throws NotEmptyCustomer {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent() && category.get().getIs_delete()) {
            Category c = category.get();
            c.setIs_delete(false);
            categoryRepository.save(c);
            return "Xóa thành công!";
        }
        throw new NotEmptyCustomer("Category không tồn tại!");
    }
}
