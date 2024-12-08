package be.helb_prigogine.player_manager.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import be.helb_prigogine.player_manager.dao.IFriendshipDAO;
import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.GetFriendDTO;
import be.helb_prigogine.player_manager.entities.Friendship;
import be.helb_prigogine.player_manager.entities.Player;
import jakarta.transaction.Transactional;

@Service
public class FriendService implements IFriendService {

    private final IFriendshipDAO friendshipDAO;
    private final IPlayerDAO playerDAO;
    private final PlayerService playerService;
    private final ModelMapper modelMapper;


    public FriendService(IFriendshipDAO friendshipDAO, IPlayerDAO playerDAO, PlayerService playerService,ModelMapper modelMapper) {
        this.friendshipDAO = friendshipDAO;
        this.playerDAO=playerDAO;
        this.playerService=playerService;
        this.modelMapper=modelMapper;
    }


    @Override
    public void addFriend(Long idPlayer, Long idFriend) {
        if (!playerDAO.findPlayerById(idPlayer).isPresent()) {
            throw new RuntimeException("Player with ID " + idPlayer + " does not exist");
        }
        if (!playerDAO.findPlayerById(idFriend).isPresent()) {
            throw new RuntimeException("Player with ID " + idFriend + " does not exist");
        }
        Optional<Friendship> existingFriendship = friendshipDAO.findFriendByIds(idPlayer, idFriend);
        if (existingFriendship.isEmpty()) {
            //We are gonna duplicate the info in the DB because we want to have the infos in both directions
            //Instead of just having idPlayer=2, idFriend=9 we will have :idPlayer=2, idFriend=9 - idPlayer=9,idFriend=2
            Friendship friendship = new Friendship();
            friendship.setIdPlayer(idPlayer);
            friendship.setIdFriend(idFriend);
            friendshipDAO.saveFriend(friendship);

            Friendship friendship2 = new Friendship();
            friendship2.setIdPlayer(idFriend);
            friendship2.setIdFriend(idPlayer);
            friendshipDAO.saveFriend(friendship2);
        }else {
            throw new RuntimeException("These players are already friends");
        }
    }

    @Override
    public void removeFriend(Long idPlayer, Long idFriend) {
        playerService.checkIfPlayerExists(idPlayer);
        playerService.checkIfPlayerExists(idFriend);
        if(checkIfAlreadyFriends(idPlayer, idFriend)){
            friendshipDAO.deleteFriendByIds(idPlayer, idFriend);
        } else {
            throw new RuntimeException("These players are not friends");
        }
    }

    @Transactional
    public List<GetFriendDTO> getFriends(Long idPlayer) {
        //En gros on doit faire tout ca pck pas bonne pratique d utilser dto dans dao, donc on fait avec frienship mais ici ca doit etre dto
        playerService.checkIfPlayerExists(idPlayer); 
        List<Friendship> friendships = friendshipDAO.findFriendsByIdPlayer(idPlayer);
        List<GetFriendDTO> friendsDTO = new ArrayList<>();
    
        for (Friendship friendship : friendships) {
            Long friendId = friendship.getIdFriend();
            Optional<Player> friendOptionnal = playerDAO.findPlayerById(friendId);
    
            friendOptionnal.ifPresent(friend -> {
                GetFriendDTO friendDTO = modelMapper.map(friend, GetFriendDTO.class); // Use ModelMapper to map fields
                friendsDTO.add(friendDTO);
            });
        }
        return friendsDTO;
    }


    /*public void checkIfPlayerExists(Long idPlayer){   -----> should be just ther one time in PlayerService
        if(!playerDAO.findPlayerById(idPlayer).isPresent()){
            throw new RuntimeException("Player with ID " + idPlayer + " does not exist");
        }
    }*/  

    public boolean checkIfAlreadyFriends(Long idPlayer, Long idFriend){
        Optional<Friendship> existingFriendship = friendshipDAO.findFriendByIds(idPlayer, idFriend);
        if(existingFriendship.isEmpty())return false;
        return true;
    }
}
