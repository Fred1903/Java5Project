package be.helb_prigogine.player_manager.services;

import java.util.List;

import be.helb_prigogine.player_manager.dto.GetFriendDTO;

public interface IFriendService {
    void addFriend(Long idPlayer, Long idFriend);
    void removeFriend(Long idPlayer, Long idFriend);
    List<GetFriendDTO> getFriends(Long idPlayer);
}
