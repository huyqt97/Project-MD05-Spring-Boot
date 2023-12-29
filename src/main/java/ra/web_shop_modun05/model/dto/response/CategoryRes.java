package ra.web_shop_modun05.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRes {
    private Long id;
    private String category_name;
}
