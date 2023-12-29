package ra.web_shop_modun05.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductRes {
    private Long id;
    private String product_name;
    private String description;
    private String image;
    private Double price_import;
    private Double price_export;
    private int quantity;
    private int sold;
    private String brand;
    private String category;
    private boolean status = true;
}
