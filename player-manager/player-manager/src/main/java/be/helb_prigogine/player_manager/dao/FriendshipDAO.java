package be.helb_prigogine.player_manager.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import be.helb_prigogine.player_manager.entities.Friendship;
import be.helb_prigogine.player_manager.repositories.FriendshipRepository;

@Repository
public class FriendshipDAO implements IFriendshipDAO{
    private FriendshipRepository friendshipRepository;

    public FriendshipDAO(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public Friendship saveFriend(Friendship friend) {
        return friendshipRepository.save(friend);
    }

     @Override
    public boolean areFriends(Long idFriend1, Long idFriend2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'areFriends'");
    }

    @Override       
    public Optional<Friendship> findFriendByIds(Long idPlayer, Long idFriend) {
        return friendshipRepository.findByIdPlayerAndIdFriend(idPlayer, idFriend);
    }

    @Override
    public List<Friendship> findFriendsByIdPlayer(Long idPlayer) {
        return friendshipRepository.findByIdPlayer(idPlayer);
    }

    @Override
    public void deleteFriendByIds(Long idPlayer, Long idFriend) {
        friendshipRepository.deleteByIdPlayerAndIdFriend(idPlayer, idFriend);
        friendshipRepository.deleteByIdPlayerAndIdFriend(idFriend, idPlayer);
    }
}
