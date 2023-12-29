package ra.web_shop_modun05.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.response.ProductRes;
import ra.web_shop_modun05.service.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ProductService productService;
    @GetMapping("/product")
    public ResponseEntity<List<ProductRes>> getAllProduct(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String filed,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit
    ) throws NotEmptyCustomer {
        return new ResponseEntity<>(productService.findAllShow(search, filed, sort, page, limit), HttpStatus.OK);
    }
}
