package ra.web_shop_modun05.service.brand;

import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.BrandReq;
import ra.web_shop_modun05.model.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand findById(Long id) throws NotEmptyCustomer;
    Brand save(BrandReq brandReq);
}
