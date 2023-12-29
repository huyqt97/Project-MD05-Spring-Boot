package ra.web_shop_modun05.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver; // tên người nhận

    private String phone;

    private String full_address;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
