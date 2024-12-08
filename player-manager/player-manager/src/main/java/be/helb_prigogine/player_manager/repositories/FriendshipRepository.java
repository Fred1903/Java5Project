package be.helb_prigogine.player_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.helb_prigogine.player_manager.entities.Friendship;




@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    //The method names should be with the same variable names then in entities --- > if in Frienship : idPlayer then it has to be 
    // findByIdPlayer and not findByPlayerId
    Optional<Friendship> findByIdPlayerAndIdFriend(Long idPlayer, Long idFriend); 
    List<Friendship> findByIdPlayer(Long idPlayer);
    void deleteByIdPlayerAndIdFriend(Long idPlayer, Long idFriend);
}
