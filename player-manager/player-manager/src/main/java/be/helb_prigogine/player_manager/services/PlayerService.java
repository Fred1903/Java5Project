package be.helb_prigogine.player_manager.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.GetFriendDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerStatisticsDTO;
import be.helb_prigogine.player_manager.entities.Player;
import jakarta.transaction.Transactional;

@Service
public class PlayerService implements IPlayerService {
    private final IPlayerDAO playerDAO;
    private final ModelMapper modelMapper;
    private static final int PLAYER_START_LEVEL = 0;
    private static final int PLAYER_START_TOTAL_POINTS = 0;
    private final int AMOUNT_POINTS_TO_PASS_LEVEL = 100;
    private final FriendService friendService;
    
    public PlayerService(IPlayerDAO playerDAO, ModelMapper modelMapper,FriendService friendService) {
        this.playerDAO = playerDAO;
        this.modelMapper = modelMapper;
        this.friendService=friendService;
    }

    @Override  /// IF I HAVE TIME I SHOULD CHANGE THE CODE HERE BECAUSE HTTP MESSAGES SHOULD BE SENT FROM THE CONTROLLER AND NOT IN THE SERVICE
    public PlayerDTO createPlayer(CreatePlayerDTO createPlayerDTO) {
        if (playerDAO.findPlayerByEmail(createPlayerDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "There is already a player with this email"
            );
        }
        if (playerDAO.findPlayerByPseudonym(createPlayerDTO.getPseudonym()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "There is already a player with this pseudonym"
            );
        }
        Player player = modelMapper.map(createPlayerDTO, Player.class);
        player.setLevel(PLAYER_START_LEVEL);
        player.setTotalPoints(PLAYER_START_TOTAL_POINTS);
        Player savedPlayer = playerDAO.savePlayer(player);

        return modelMapper.map(savedPlayer, PlayerDTO.class);
    }


    @Override
    public boolean deletePlayer(Long id) {
        if(playerDAO.findPlayerById(id).isPresent()){
            playerDAO.deletePlayerById(id);
            return true;
        }
        return false;
    }


    @Override
    public Optional<PlayerDTO> getPlayerInformations(Long id) { //On doit encore ajouter sa liste d'amis !!!!!!!!!!!!!!!!!!!!!!
        Optional<Player> player=playerDAO.findPlayerById(id);
        //On map le optionnal pour en faire un player, si il existe alors on renvoie le dto de ce dernier, sinon on renvoie optional.empty et
        //on ne rentre pas dasns le modelMapper
        //return player.map(p -> modelMapper.map(p, PlayerDTO.class));

        return player.map(p -> {
        PlayerDTO playerDTO = modelMapper.map(p, PlayerDTO.class);

        // Fetch and add friends using IFriendService
        List<GetFriendDTO> friends = friendService.getFriends(id); // Assuming you have injected friendService
        playerDTO.setFriends(friends);

        return playerDTO;
    });
    }

    @Override
    @Transactional
    public PlayerDTO updatePlayer(Long idPlayer, UpdatePlayerDTO updatePlayerDTO) {
        friendService.checkIfPlayerExists(idPlayer); //we dont want checkIfPlayerExists here because then friendService will depend on 
        //playerService and we avoid to have dependency in both directions
        Player player = playerDAO.findPlayerById(idPlayer).get(); 
        if(updatePlayerDTO.getEmail() == null && updatePlayerDTO.getPseudonym() == null && updatePlayerDTO.getName()==null){
            throw new RuntimeException("Please provide an email, a name or a pseudonym to update");
        }
        if (updatePlayerDTO.getEmail() != null) {
            player.setEmail(updatePlayerDTO.getEmail());
        }
        if (updatePlayerDTO.getPseudonym() != null) {
            player.setPseudonym(updatePlayerDTO.getPseudonym());
        }
        if (updatePlayerDTO.getName() != null) {
            player.setPseudonym(updatePlayerDTO.getName());
        }
        playerDAO.savePlayer(player);

        return modelMapper.map(player, PlayerDTO.class);
    }

    /*public void checkIfPlayerExists(Long idPlayer){
        if(!playerDAO.findPlayerById(idPlayer).isPresent()){
            throw new RuntimeException("Player with ID " + idPlayer + " does not exist");
        }
    }*/


    @Override
    public PlayerDTO updatePlayerStatistics(Long idPlayer, UpdatePlayerStatisticsDTO updatePlayerStatisticsDTO) {
        Player player = playerDAO.findPlayerById(idPlayer).get(); 
        player.setTotalPoints(player.getTotalPoints()+updatePlayerStatisticsDTO.getPoints());
        if(player.getTotalPoints()>AMOUNT_POINTS_TO_PASS_LEVEL){
            int pointsToAdd = player.getTotalPoints()-AMOUNT_POINTS_TO_PASS_LEVEL;
            player.setTotalPoints(pointsToAdd);
            player.setLevel(player.getLevel()+1);
        }
        playerDAO.savePlayer(player);
        return modelMapper.map(player, PlayerDTO.class);
    }
}

