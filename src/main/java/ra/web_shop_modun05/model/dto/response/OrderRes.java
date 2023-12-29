package ra.web_shop_modun05.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ra.web_shop_modun05.model.entity.StatusOrder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderRes {
    private Long id;
    private String receiver; // tên người nhận
    private String phone;
    private String full_address;
    private double total_price;
    private String note;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date create_at;
    private StatusOrder statusOrder;
}
