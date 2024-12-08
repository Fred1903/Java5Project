package be.helb_prigogine.player_manager.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;

import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.entities.Player;
import be.helb_prigogine.player_manager.repositories.PlayerRepository;

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

    @Override
    public PlayerDTO createPlayer(CreatePlayerDTO createPlayerDTO) {
        if (playerDAO.findByEmail(createPlayerDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "There is already a player with this email"
            );
        }
        if (playerDAO.findByPseudonym(createPlayerDTO.getPseudonym()).isPresent()) {
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
    
}

