package be.helb_prigogine.player_manager.services;

import java.util.Optional;

import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerStatisticsDTO;

public interface IPlayerService {
    PlayerDTO createPlayer(CreatePlayerDTO createPlayerDTO);
    boolean deletePlayer(Long id);
    Optional<PlayerDTO> getPlayerInformations(Long id);
    PlayerDTO updatePlayer(Long idPlayer, UpdatePlayerDTO updatePlayerDTO);
    PlayerDTO updatePlayerStatistics(Long idPlayer, UpdatePlayerStatisticsDTO updatePlayerStatisticsDTO);
}
