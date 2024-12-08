package be.helb_prigogine.player_manager.services;

import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;

public interface IPlayerService {
    PlayerDTO createPlayer(CreatePlayerDTO playerDTO);
}
