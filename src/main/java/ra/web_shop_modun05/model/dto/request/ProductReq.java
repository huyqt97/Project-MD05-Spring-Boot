package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductReq {
    @NotEmpty(message = "Không được để trống!")
        private String productName;
    @NotEmpty(message = "Không được để trống!")
    private String Des;
    private MultipartFile image;
    @Min(value = 0,message = "giá không hợp lệ!")
    private Double price_import;
    @NotNull(message = "Không được để trống!")
    private Long categoryId;
    @NotNull(message = "Không được để trống!")
    private Long brandId;
    @Min(value = 0,message = "số lượng phải lớn hơn 0")
    private int quantity;
}
