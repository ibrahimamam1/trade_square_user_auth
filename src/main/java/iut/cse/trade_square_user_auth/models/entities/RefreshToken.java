package iut.cse.trade_square_user_auth.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends MasterEntity{
    private String token;
    private Long userId;
}
