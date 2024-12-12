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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private IPlayerDAO playerDAO;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private PlayerService playerService;

    public PlayerServiceTest() {
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

    @Test
    void createPlayer_shouldThrowExceptionWhenEmailExists() {
        CreatePlayerDTO createPlayerDTO = new CreatePlayerDTO();
        createPlayerDTO.setEmail("player1@example.com");
        createPlayerDTO.setPseudonym("PlayerOne");

        when(playerDAO.findPlayerByEmail(createPlayerDTO.getEmail())).thenReturn(Optional.of(new Player()));

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> playerService.createPlayer(createPlayerDTO)
        );

        assertEquals("There is already a player with this email", exception.getReason());
        verify(playerDAO, never()).savePlayer(any(Player.class));
    }

    @Test
    void createPlayer_shouldThrowExceptionWhenPseudonymExists() {
        CreatePlayerDTO createPlayerDTO = new CreatePlayerDTO();
        createPlayerDTO.setEmail("player2@example.com");
        createPlayerDTO.setPseudonym("PlayerOne");

        when(playerDAO.findPlayerByPseudonym(createPlayerDTO.getPseudonym())).thenReturn(Optional.of(new Player()));

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> playerService.createPlayer(createPlayerDTO)
        );

        assertEquals("There is already a player with this pseudonym", exception.getReason());
        verify(playerDAO, never()).savePlayer(any(Player.class));
    }

    @Test
    void deletePlayer_shouldReturnFalseWhenPlayerNotFound() {
        Long playerId = 1L;
        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.empty());

        boolean result = playerService.deletePlayer(playerId);

        assertFalse(result);
        verify(playerDAO, never()).deletePlayerById(playerId); // Ensure deletePlayerById is not called
        verify(playerDAO).findPlayerById(playerId);           // Verify the search is performed
}


    @Test
    void getPlayerInformations_shouldReturnEmptyWhenPlayerNotFound() {
        Long playerId = 1L;

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.empty());

        Optional<PlayerDTO> result = playerService.getPlayerInformations(playerId);

        assertTrue(result.isEmpty());
        verify(playerDAO).findPlayerById(playerId);
    }

    @Test
    void updatePlayerStatistics_shouldThrowExceptionWhenPlayerNotFound() {
        Long playerId = 1L;
        UpdatePlayerStatisticsDTO statsDTO = new UpdatePlayerStatisticsDTO();
        statsDTO.setPoints(50);
        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> playerService.updatePlayerStatistics(playerId, statsDTO)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Player not found", exception.getReason());

        verify(playerDAO, never()).savePlayer(any(Player.class));
        verify(playerDAO).findPlayerById(playerId); // Verify the search is performed
    }

}
