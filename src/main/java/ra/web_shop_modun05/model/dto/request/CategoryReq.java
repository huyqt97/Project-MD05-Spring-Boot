package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryReq {
    @NotEmpty(message = "Không được để trống!")
    private String category_name;
}
