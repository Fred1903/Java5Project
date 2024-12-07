package be.helb_prigogine.player_manager.services;

import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.entities.Player;

public interface IPlayerService {
    PlayerDTO createPlayer(PlayerDTO player);
}
