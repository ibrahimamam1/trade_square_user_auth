package iut.cse.trade_square_user_auth.controllers;

import iut.cse.trade_square_user_auth.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {
    final RatingService ratingService;

    @GetMapping("userAverageRating")
    public double getUserAverageRating(Long userId) {return ratingService.getUserAverageRating(userId);}
}
