package be.helb_prigogine.player_manager.services;

import org.springframework.stereotype.Service;

import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.entities.Player;

@Service
public class PlayerService implements IPlayerService {
    private IPlayerDAO playerDAO;

    
    public PlayerService(IPlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }


    @Override
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player player=new Player();
        player.setPseudonym(playerDTO.getPseudonym());

        Player savedPlayer = playerDAO.savePlayer(player);

        // Convertir l'entité sauvegardée en PlayerDTO
        PlayerDTO savedPlayerDTO = new PlayerDTO();
        savedPlayerDTO.setPseudonym(savedPlayer.getPseudonym());

        return savedPlayerDTO;
    }
    
}
