package ra.web_shop_modun05.service.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.BrandReq;
import ra.web_shop_modun05.model.entity.Brand;
import ra.web_shop_modun05.model.entity.Product;
import ra.web_shop_modun05.repository.BrandRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findById(Long id) throws NotEmptyCustomer {
        Optional<Brand> brand =  brandRepository.findById(id);
        if (brand.isPresent()){
            return brand.get();
        }else {
            throw new NotEmptyCustomer("Brand Không tồn tại");
        }
    }

    @Override
    public Brand save(BrandReq brandReq) {
        Set<Product> set =new HashSet<>();
        Brand brand = Brand.builder()
                .brandName(brandReq.getName())
                .products(set)
                .is_delete(true).build();
        return brandRepository.save(brand);
    }
}
