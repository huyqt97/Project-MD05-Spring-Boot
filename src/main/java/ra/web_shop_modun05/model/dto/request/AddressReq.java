package ra.web_shop_modun05.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressReq {
    @NotEmpty(message = "Không được để trống!")
    private String receiver;
    @Pattern(regexp = "^\\+?\\d{1,3}[- ]?\\(?(\\d{1,4})?\\)?[- ]?\\d{6,}$", message = "Số điện thoại không hợp lệ!")
    private String phone;
    @NotEmpty(message = "Không được để trống!")
    private String full_address;
}
