package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterReq {
    @Pattern(regexp = "^.{6,16}$",message = "Tài khoản phải từ 6 - 8 ký tự!")
    private String userName;
    @Pattern(regexp = "^.{8,12}$",message = "Mật khẩu phải có từ 8 - 12 ký tự!")
    private String passWord;
    @NotEmpty(message = "Không được để trống!")
    private String fullName;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "Email không hợp lệ!" )
    private String email;
}
