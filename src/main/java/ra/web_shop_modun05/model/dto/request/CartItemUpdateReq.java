package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemUpdateReq {
    @NotNull(message = "Không được để trống!")
    private Long idCart;
    @Min(value = 0,message = "số lượng phải lớn hơn 0")
    private int quantity;
}
