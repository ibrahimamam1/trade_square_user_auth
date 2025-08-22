package iut.cse.trade_square_user_auth.repositories;

import iut.cse.trade_square_user_auth.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("select avg(r.rating) from Rating r where r.userId = ?1")
    float getAverageRating(Long user_id);
}
