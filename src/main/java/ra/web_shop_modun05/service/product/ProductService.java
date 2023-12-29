package ra.web_shop_modun05.service.product;

import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.ProductReq;
import ra.web_shop_modun05.model.dto.request.ProductUpdateReq;
import ra.web_shop_modun05.model.dto.response.ProductRes;

import java.util.List;

public interface ProductService {
    List<ProductRes> findAllShow(String search,String filed,String sort,Integer page,Integer limit) throws NotEmptyCustomer;
    ProductRes findById(Long Id)throws NotEmptyCustomer;
    ProductRes addProduct(ProductReq productReq) throws NotEmptyCustomer;
    ProductRes updateProduct(ProductUpdateReq productUpdateReq) throws NotEmptyCustomer;
    String delete(Long id)throws NotEmptyCustomer;
}
