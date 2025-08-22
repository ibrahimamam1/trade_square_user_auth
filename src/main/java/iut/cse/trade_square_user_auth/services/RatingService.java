package iut.cse.trade_square_user_auth.services;

import iut.cse.trade_square_user_auth.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public float getUserAverageRating(Long userId) {
        return ratingRepository.getAverageRating(userId);
    }
}
