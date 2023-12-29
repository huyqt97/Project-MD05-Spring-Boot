package ra.web_shop_modun05.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.ProductReq;
import ra.web_shop_modun05.model.dto.request.ProductUpdateReq;
import ra.web_shop_modun05.model.dto.response.ProductRes;
import ra.web_shop_modun05.model.entity.Brand;
import ra.web_shop_modun05.model.entity.Category;
import ra.web_shop_modun05.model.entity.Product;
import ra.web_shop_modun05.repository.BrandRepository;
import ra.web_shop_modun05.repository.CategoryRepository;
import ra.web_shop_modun05.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("${path-upload}")
    private String pathUpload;

    @Value("${server.port}")
    private Long port;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductRes> findAllShow(String search, String filed, String sort, Integer page, Integer limit) throws NotEmptyCustomer {
        Sort sort1 = Sort.by(filed);
        Page<Product> products = productRepository.findAllBySearch(search, PageRequest.of(page, limit).withSort(sort1));
        List<ProductRes> productResList = new ArrayList<>();
        for (Product p : products) {
            if(p.is_delete()) {
                ProductRes ps = ProductRes.builder()
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
                productResList.add(ps);
            }
        }
        return productResList;
    }

    @Override
    public ProductRes findById(Long id) throws NotEmptyCustomer {
        Optional<Product> p = productRepository.findById(id);
        if (p.isPresent() && p.get().is_delete()) {
            return ProductRes.builder()
                    .id(p.get().getId())
                    .product_name(p.get().getProduct_name())
                    .description(p.get().getDescription())
                    .image(p.get().getImage())
                    .price_import(p.get().getPrice_import())
                    .price_export(p.get().getPrice_export())
                    .quantity(p.get().getQuantity())
                    .sold(p.get().getSold())
                    .brand(p.get().getBrand().getBrandName())
                    .category(p.get().getCategory().getCategory_name())
                    .status(p.get().isStatus()).build();
        }
        throw new NotEmptyCustomer("Không tìm thấy sản phẩm!");
    }

    @Override
    public ProductRes addProduct(ProductReq productReq) throws NotEmptyCustomer {
        String fileName = productReq.getImage().getOriginalFilename();
        try {
            FileCopyUtils.copy(productReq.getImage().getBytes(), new File(pathUpload + fileName));
            Optional<Brand> brand = brandRepository.findById(productReq.getBrandId());
            if (brand.isPresent()) {
                Optional<Category> category = categoryRepository.findById(productReq.getCategoryId());
                if (category.isPresent()) {
                    Product product = Product.builder()
                            .description(productReq.getDes())
                            .product_name(productReq.getProductName())
                            .image("http://localhost:" + port + "/" + fileName)
                            .is_delete(true)
                            .price_import(productReq.getPrice_import())
                            .price_export(productReq.getPrice_import() * 1.5)
                            .quantity(productReq.getQuantity())
                            .status(true)
                            .brand(brand.get())
                            .category(category.get()).build();
                    Product p = productRepository.save(product);
                    return ProductRes.builder()
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
                }
                throw new NotEmptyCustomer("category không tồn tại!");
            }
            throw new NotEmptyCustomer("brand không tồn tại!");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ProductRes updateProduct(ProductUpdateReq productUpdateReq) throws NotEmptyCustomer {
        String fileName = productUpdateReq.getImage().getOriginalFilename();
        try {
            FileCopyUtils.copy(productUpdateReq.getImage().getBytes(), new File(pathUpload + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Optional<Brand> brand = brandRepository.findById(productUpdateReq.getBrandId());
        if (brand.isPresent()) {
            Optional<Category> category = categoryRepository.findById(productUpdateReq.getCategoryId());
            if (category.isPresent()) {
                Optional<Product> product = productRepository.findById(productUpdateReq.getId());
                if (product.isPresent() && product.get().is_delete()) {
                    Product product1 = product.get();
                    product1.setDescription(productUpdateReq.getDes());
                    product1.setImage("http://localhost:" + port + "/" + fileName);
                    product1.setPrice_import(productUpdateReq.getPrice_import());
                    product1.setPrice_export(productUpdateReq.getPrice_import() * 1.5);
                    product1.setProduct_name(productUpdateReq.getProductName());
                    product1.setQuantity(productUpdateReq.getQuantity());
                    product1.setBrand(brand.get());
                    product1.setCategory(category.get());
                    Product p = productRepository.save(product1);
                    return ProductRes.builder()
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
                }
                throw new NotEmptyCustomer("sản phẩm không tồn tại!");
            }
            throw new NotEmptyCustomer("category không tồn tại!");
        }
        throw new NotEmptyCustomer("brand không tồn tại!");
    }

    @Override
    public String delete(Long id) throws NotEmptyCustomer {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && product.get().is_delete()) {
            Product product1 = product.get();
            product1.set_delete(false);
            productRepository.save(product1);
            return "Xóa thành công!";
        }
        throw new NotEmptyCustomer("Sản phẩm không tồn tại!");
    }
}
