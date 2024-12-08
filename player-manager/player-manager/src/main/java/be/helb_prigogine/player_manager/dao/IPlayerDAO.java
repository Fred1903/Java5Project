package be.helb_prigogine.player_manager.dao;

import java.util.Optional;

import be.helb_prigogine.player_manager.entities.Player;



public interface IPlayerDAO {
    Player savePlayer(Player player);
    void deletePlayer(Long id);

    Optional<Player> findByEmail(String email);
    Optional<Player> findByPseudonym(String pseudonym);
}
