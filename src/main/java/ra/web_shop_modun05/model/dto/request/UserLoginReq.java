package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginReq {
    @NotEmpty(message = "Không được để trống!")
    private String userName;
    @NotEmpty(message = "Không được để trống!")
    private String passWord;
}
