package ra.web_shop_modun05.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDetailRes {
    private Long id;
    private String product_name;
    private double unit_price;
    private int order_quantity;
}
