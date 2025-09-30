package iut.cse.trade_square_user_auth.repositories;

import iut.cse.trade_square_user_auth.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Query("select u from User u where u.username like %?1% OR u.username like %?2%")
    List<User> findByUserNames(String firstname, String lastname);

    @Query("select u from User u join Rating r on u.id = r.userId where avg(r.rating) > ?1 group by u")
    List<User> findByMinRating(float minRating);

    @Query("select u from User u where u.address = ?1")
    List<User> findByAddress(String address);

}
