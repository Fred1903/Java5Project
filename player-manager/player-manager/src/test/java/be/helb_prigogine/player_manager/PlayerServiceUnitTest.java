package be.helb_prigogine.player_manager;

import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.*;
import be.helb_prigogine.player_manager.entities.Player;
import be.helb_prigogine.player_manager.services.FriendService;
import be.helb_prigogine.player_manager.services.PlayerService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PlayerServiceUnitTest {

    @Mock
    private IPlayerDAO playerDAO;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private PlayerService playerService;

    public PlayerServiceUnitTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlayer_shouldReturnPlayerDTO() {
        CreatePlayerDTO createPlayerDTO = new CreatePlayerDTO();
        createPlayerDTO.setName("Player1");
        createPlayerDTO.setPseudonym("PlayerOne");
        createPlayerDTO.setEmail("player1@example.com");

        Player mockPlayer = new Player();
        mockPlayer.setId(1L);
        mockPlayer.setName("Player1");

        when(playerDAO.findPlayerByEmail(createPlayerDTO.getEmail())).thenReturn(Optional.empty());
        when(playerDAO.findPlayerByPseudonym(createPlayerDTO.getPseudonym())).thenReturn(Optional.empty());
        when(playerDAO.savePlayer(any(Player.class))).thenReturn(mockPlayer);
        when(modelMapper.map(any(CreatePlayerDTO.class), eq(Player.class))).thenReturn(mockPlayer);
        when(modelMapper.map(mockPlayer, PlayerDTO.class)).thenReturn(new PlayerDTO());

        PlayerDTO result = playerService.createPlayer(createPlayerDTO);

        assertNotNull(result);
        verify(playerDAO).savePlayer(any(Player.class));
    }

    @Test
    void deletePlayer_shouldReturnTrue() {
        Long playerId = 1L;
        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(new Player()));

        boolean result = playerService.deletePlayer(playerId);

        assertTrue(result);
        verify(playerDAO).deletePlayerById(playerId);
    }

    @Test
    void getPlayerInformations_shouldReturnPlayerDTO() {
        Long playerId = 1L;
        Player mockPlayer = new Player();
        mockPlayer.setId(playerId);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(mockPlayer));
        when(modelMapper.map(mockPlayer, PlayerDTO.class)).thenReturn(new PlayerDTO());
        when(friendService.getFriends(playerId)).thenReturn(List.of(new GetFriendDTO()));

        Optional<PlayerDTO> result = playerService.getPlayerInformations(playerId);

        assertTrue(result.isPresent());
    }

    @Test
    void updatePlayer_shouldUpdateFields() {
        Long playerId = 1L;
        UpdatePlayerDTO updatePlayerDTO = new UpdatePlayerDTO();
        updatePlayerDTO.setEmail("updated@example.com");

        Player mockPlayer = new Player();
        mockPlayer.setId(playerId);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(mockPlayer));
        when(playerDAO.savePlayer(mockPlayer)).thenReturn(mockPlayer);
        when(modelMapper.map(mockPlayer, PlayerDTO.class)).thenReturn(new PlayerDTO());

        PlayerDTO result = playerService.updatePlayer(playerId, updatePlayerDTO);

        assertNotNull(result);
        verify(playerDAO).savePlayer(mockPlayer);
    }

    @Test
    void updatePlayerStatistics_shouldUpdateStats() {
        Long playerId = 1L;
        UpdatePlayerStatisticsDTO statsDTO = new UpdatePlayerStatisticsDTO();
        statsDTO.setPoints(150);

        Player mockPlayer = new Player();
        mockPlayer.setId(playerId);
        mockPlayer.setTotalPoints(50); 
        mockPlayer.setLevel(0);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(mockPlayer));
        when(playerDAO.savePlayer(mockPlayer)).thenReturn(mockPlayer);
        when(modelMapper.map(mockPlayer, PlayerDTO.class)).thenReturn(new PlayerDTO());

        PlayerDTO result = playerService.updatePlayerStatistics(playerId, statsDTO);

        assertNotNull(result);
        assertEquals(100, mockPlayer.getTotalPoints()); // Overflow points after leveling up
        assertEquals(1, mockPlayer.getLevel()); // Level increased by 1
    }

}
