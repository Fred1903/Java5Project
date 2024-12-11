package be.helb_prigogine.player_manager.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import org.modelmapper.ModelMapper;

import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerDTO;
import be.helb_prigogine.player_manager.entities.Player;
import jakarta.transaction.Transactional;

@Service
public class PlayerService implements IPlayerService {
    private final IPlayerDAO playerDAO;
    private final ModelMapper modelMapper;
    private static final int PLAYER_START_LEVEL = 0;
    private static final int PLAYER_START_TOTAL_POINTS = 0;

    
    public PlayerService(IPlayerDAO playerDAO, ModelMapper modelMapper) {
        this.playerDAO = playerDAO;
        this.modelMapper = modelMapper;
    }


    /*@Override
    public PlayerDTO createPlayer(CreatePlayerDTO createPlayerDTO) {
        // Create Player entity from CreatePlayerDTO
        Player player = new Player();
        player.setEmail(createPlayerDTO.getEmail());
        player.setName(createPlayerDTO.getName());
        player.setPseudonym(createPlayerDTO.getPseudonym());
        player.setLevel(PLAYER_START_LEVEL);
        player.setTotalPoints(PLAYER_START_TOTAL_POINTS);

        // Save to the database
        Player savedPlayer = playerDAO.savePlayer(player);

        // Convert saved Player entity to PlayerDTO
        PlayerDTO savedPlayerDTO = new PlayerDTO();
        savedPlayerDTO.setId(savedPlayer.getId());
        savedPlayerDTO.setEmail(savedPlayer.getEmail());
        savedPlayerDTO.setName(savedPlayer.getName());
        savedPlayerDTO.setPseudonym(savedPlayer.getPseudonym());
        savedPlayerDTO.setTotalPoints(savedPlayer.getTotalPoints());
        savedPlayerDTO.setLevel(savedPlayer.getLevel());

        return savedPlayerDTO;
    }*/

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
        // Mapper le DTO vers l'entité Player
        Player player = modelMapper.map(createPlayerDTO, Player.class);
        
        // Définir les valeurs par défaut après le mappage
        player.setLevel(PLAYER_START_LEVEL);
        player.setTotalPoints(PLAYER_START_TOTAL_POINTS);

        // Sauvegarder dans la base de données
        Player savedPlayer = playerDAO.savePlayer(player);

        // Mapper l'entité Player sauvegardée vers le DTO PlayerDTO
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
        return player.map(p -> modelMapper.map(p, PlayerDTO.class));
    }

    @Override
    @Transactional
    public PlayerDTO updatePlayer(Long idPlayer, UpdatePlayerDTO updatePlayerDTO) {
        checkIfPlayerExists(idPlayer);
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

    public void checkIfPlayerExists(Long idPlayer){
        if(!playerDAO.findPlayerById(idPlayer).isPresent()){
            throw new RuntimeException("Player with ID " + idPlayer + " does not exist");
        }
    }

    /*private boolean isPlayerExisting(Long id){
        if(playerDAO.findPlayerById(id).isPresent()){
            playerDAO.deletePlayerById(id);
            return true;
        }
        return false;
    }*/
    
}

