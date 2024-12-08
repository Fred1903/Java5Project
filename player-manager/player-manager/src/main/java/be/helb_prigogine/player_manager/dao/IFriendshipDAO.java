package be.helb_prigogine.player_manager.dao;

import java.util.List;
import java.util.Optional;

import be.helb_prigogine.player_manager.entities.Friendship;

public interface IFriendshipDAO {
    Friendship saveFriend(Friendship friend);
    boolean areFriends(Long idFriend1, Long idFriend2);  

    Optional<Friendship> findFriendByIds(Long idPlayer, Long idFriend);
    List<Friendship> findFriendsByIdPlayer(Long idPlayer);
    void deleteFriendByIds(Long idPlayer, Long idFriend);
}
