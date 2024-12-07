package be.helb_prigogine.player_manager.dao;

import org.springframework.stereotype.Repository;

import be.helb_prigogine.player_manager.entities.Player;
import be.helb_prigogine.player_manager.repositories.PlayerRepository;

@Repository
public class PlayerDAO implements IPlayerDAO {
    private PlayerRepository playerRepository;

    public PlayerDAO(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
    
}
