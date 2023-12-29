package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemReq {
    @NotNull(message = "Không được để trống!")
    private Long productId;
    @Min(value = 0,message = "số lượng phải lớn hơn 0")
    private int quantity;
}
