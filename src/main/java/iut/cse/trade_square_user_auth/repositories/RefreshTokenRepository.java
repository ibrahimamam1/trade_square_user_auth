package iut.cse.trade_square_user_auth.repositories;

import iut.cse.trade_square_user_auth.models.entities.RefreshToken;
import iut.cse.trade_square_user_auth.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("DELETE FROM RefreshToken t WHERE t.id = ?1")
    @Modifying
    void deleteByUserId(Long id);

    @Query("select t from RefreshToken t where t.userId=?1")
    Optional<RefreshToken> findByUserId(Long id);

    @Query("SELECT COUNT(t) > 0 FROM RefreshToken t WHERE t.userId = ?1 AND t.token = ?2")
    boolean existsByUserIdAndToken(Long id, String token);

    @Query("SELECT t from RefreshToken t where t.token = ?1")
    Optional<RefreshToken> findByToken(String token);
}
