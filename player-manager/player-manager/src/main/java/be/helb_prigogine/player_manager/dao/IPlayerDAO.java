package be.helb_prigogine.player_manager.dao;

import java.util.Optional;

import be.helb_prigogine.player_manager.entities.Player;


//It is better to add Optional when we are note sure that the player exists because in this case we will not have to  manage it
public interface IPlayerDAO {
    Player savePlayer(Player player);
    void deletePlayerById(Long id);
    Optional<Player> findPlayerById(Long id);

    Optional<Player> findPlayerByEmail(String email);
    Optional<Player> findPlayerByPseudonym(String pseudonym);
}
