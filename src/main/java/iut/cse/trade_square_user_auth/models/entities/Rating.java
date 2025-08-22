package iut.cse.trade_square_user_auth.models.entities;

import iut.cse.trade_square_user_auth.models.enums.RatingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private float rating;
    @Enumerated(EnumType.STRING)
    private RatingType ratingType;
}
