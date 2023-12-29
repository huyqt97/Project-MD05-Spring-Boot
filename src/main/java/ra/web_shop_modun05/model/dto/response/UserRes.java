package ra.web_shop_modun05.model.dto.response;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRes {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private Boolean status;
    private Set<String> roles;
    private String token;
}
