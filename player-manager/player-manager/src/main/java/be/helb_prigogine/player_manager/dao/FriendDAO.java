package be.helb_prigogine.player_manager.dao;

import org.springframework.stereotype.Repository;

import be.helb_prigogine.player_manager.entities.Friend;
import be.helb_prigogine.player_manager.repositories.FriendRepository;

@Repository
public class FriendDAO implements IFriendDAO{
    private FriendRepository friendRepository;

    public FriendDAO(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public Friend saveFriend(Friend friend) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveFriend'");
    }
}
