package be.helb_prigogine.player_manager.dao;

import be.helb_prigogine.player_manager.entities.Player;

public interface IPlayerDAO {
    Player savePlayer(Player player);
    void deletePlayer(Long id);
}
