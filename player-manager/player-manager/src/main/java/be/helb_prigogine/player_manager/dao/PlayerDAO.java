package be.helb_prigogine.player_manager.dao;

import java.util.Optional;

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
    public Optional<Player> findPlayerByEmail(String email) {
        return playerRepository.findByEmail(email);
    }

    @Override
    public Optional<Player> findPlayerByPseudonym(String pseudonym) {
        return playerRepository.findByPseudonym(pseudonym);
    }

    @Override
    public void deletePlayerById(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> findPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    /*@Override
    public boolean isPlayerExistingById(Long id) {
        return playerRepository.existsById(id);
    }*/ //Pas besoin car on peut faire findByID.isPresent
    
}
