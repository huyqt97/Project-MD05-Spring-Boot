package ra.web_shop_modun05.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemRes {
    private Long id;
    private int quantity;
    private String productName;
    private String image;
    private double price;
}
