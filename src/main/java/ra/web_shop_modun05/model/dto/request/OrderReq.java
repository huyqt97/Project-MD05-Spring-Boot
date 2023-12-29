package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderReq {
    @NotEmpty(message = "Không được để trống!")
    private String note;
    @NotNull(message = "Không được để trống!")
    private Long addressId;
}
