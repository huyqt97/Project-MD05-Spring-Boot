package ra.web_shop_modun05.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.web_shop_modun05.exception.CategoryException;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.model.dto.request.BrandReq;
import ra.web_shop_modun05.model.dto.request.CategoryReq;
import ra.web_shop_modun05.model.dto.request.ProductReq;
import ra.web_shop_modun05.model.dto.request.ProductUpdateReq;
import ra.web_shop_modun05.model.dto.response.*;
import ra.web_shop_modun05.model.entity.Brand;
import ra.web_shop_modun05.model.entity.Role;
import ra.web_shop_modun05.model.entity.StatusOrder;
import ra.web_shop_modun05.service.brand.BrandService;
import ra.web_shop_modun05.service.category.CategoryService;
import ra.web_shop_modun05.service.order.OrderService;
import ra.web_shop_modun05.service.orderDetail.OrderDetailService;
import ra.web_shop_modun05.service.product.ProductService;
import ra.web_shop_modun05.service.role.RoleService;
import ra.web_shop_modun05.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProductService productService;
    @Autowired()
    BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    //    Lấy ra danh sách người dùng  (phân trang và sắp xếp) tìm kiếm người dùng theo tên
    @GetMapping("/user")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String filed,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit
    ) throws NotEmptyCustomer {
        return new ResponseEntity<>(userService.findAllShow(search, filed, sort, page, limit), HttpStatus.OK);
    }

    //    khóa tài khoản
    @PostMapping("/user/{id}")
    public ResponseEntity<String> editStatus(Authentication authentication, @PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(userService.editStatus(authentication, id), HttpStatus.OK);
    }

    //    danh sách role
    @GetMapping("/role")
    public ResponseEntity<List<Role>> getAllRole() {
        return new ResponseEntity<>(roleService.getAllRole(), HttpStatus.OK);
    }
//    danh sách brand
    @GetMapping("/brand")
    public ResponseEntity<List<Brand>> getAll(){
        return new ResponseEntity<>(brandService.findAll(), HttpStatus.OK);
    };
//    lấy nhãn hiệu theo id
    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> findBId(@PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(brandService.findById(id),HttpStatus.OK);
    }
//    thêm mới brand
    @PostMapping("/brand")
    public ResponseEntity<Brand> create(@RequestBody BrandReq brandReq){
        return new ResponseEntity<>(brandService.save(brandReq),HttpStatus.CREATED);
    }

    //   Lấy ra danh sách sản phẩm  (phân trang và sắp xếp) tìm kiếm người dùng theo tên
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

    //    lấy thông tin sản phẩm theo Id
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductRes> getById(@PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    //    Thêm mới sản phẩm

    @PostMapping("/product/addProduct")
    public ResponseEntity<ProductRes> addProduct(@Valid @ModelAttribute ProductReq productReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(productService.addProduct(productReq), HttpStatus.OK);
    }
    //    chỉnh sửa thông tin sản phẩm
    @PostMapping("/product/update")
    public ResponseEntity<ProductRes> updateProduct(@Valid @ModelAttribute ProductUpdateReq productUpdateReq) throws NotEmptyCustomer {
        return new ResponseEntity<>(productService.updateProduct(productUpdateReq), HttpStatus.OK);
    }

    //    Xóa sản phẩm
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(productService.delete(id), HttpStatus.OK);
    }

    //    Lấy về danh sách tất cả danh mục (sắp xếp và phân trang)
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryRes>> listResponseEntity() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    //    thêm mới danh mục
    @PostMapping("/categories")
    public ResponseEntity<CategoryRes> categoryResResponseEntity(@RequestBody CategoryReq categoryReq) {
        return new ResponseEntity<>(categoryService.save(categoryReq), HttpStatus.OK);
    }

    //    lấy về danh mục theo id
    @GetMapping("/categories/{id}")
    public ResponseEntity<List<ProductRes>> categoryResResponseEntity(@PathVariable Long id) throws CategoryException {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }
//    lấy về danh sách sản phẩm theo loại sản phẩm
    @GetMapping("/categories/")

    // xóa danh mục theo id
    @DeleteMapping("/categories/{id}")
    private ResponseEntity<String> deleteCategory(@PathVariable Long id) throws NotEmptyCustomer {
        return new ResponseEntity<>(categoryService.delete(id), HttpStatus.OK);
    }
//    danh sách sản phẩm theo category
//    @GetMapping("/categories/{id}/product")
//    private ResponseEntity<List<CategoryRes>> getAllById(@PathVariable Long id) throws NotEmptyCustomer{
//        return new ResponseEntity<>(,HttpStatus.OK);
//    }
//    lấy ra danh sách tất cả đơn hàng
    @GetMapping("/order")
    public ResponseEntity<List<OrderRes>> getAllOrder(){
        return new ResponseEntity<>(orderService.getAll(),HttpStatus.OK);
    }
//    Danh sách đơn hàng theo trạng thái
    @GetMapping("/order/getBy-status")
    public ResponseEntity<List<OrderRes>> getOrderByStatus(@RequestParam String status){
        return new ResponseEntity<>(orderService.getAllOrderByStatus(status),HttpStatus.OK);
    }
//    chi tiết đơn hàng
    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderDetailRes>> orderDetail(@PathVariable Long id) throws NotEmptyCustomer{
        return new ResponseEntity<>(orderDetailService.orderDetailByIdOrder(id),HttpStatus.OK);
    }
// duyệt đơn hàng
    @PutMapping("/orders/{id}/confirmOrder")
    public ResponseEntity<OrderRes> confirmOrder(@PathVariable Long id)throws NotEmptyCustomer{
        return new ResponseEntity<>(orderService.confirm(id),HttpStatus.OK);
    }
//   hủy đơn hàng
    @PutMapping("/orders/{id}/cancelOrder")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id)throws NotEmptyCustomer{
        return new ResponseEntity<>(orderService.cancel(id),HttpStatus.OK);
    }
}
