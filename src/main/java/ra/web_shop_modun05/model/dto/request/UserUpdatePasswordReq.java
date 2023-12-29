package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdatePasswordReq {
    @NotEmpty(message = "Không được để trống!")
    private String passWord;
    @Pattern(regexp = "^.{8,12}$",message = "Mật khẩu phải có từ 8 - 12 ký tự!")
    private String passWordNew;
}
