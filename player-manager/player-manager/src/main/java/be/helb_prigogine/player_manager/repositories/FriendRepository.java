package be.helb_prigogine.player_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.helb_prigogine.player_manager.entities.Friend;




@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {
    
}
