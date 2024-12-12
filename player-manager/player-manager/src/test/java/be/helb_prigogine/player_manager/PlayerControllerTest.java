package be.helb_prigogine.player_manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.helb_prigogine.player_manager.controllers.PlayerController;
import be.helb_prigogine.player_manager.dto.*;

import be.helb_prigogine.player_manager.services.PlayerService;

class PlayerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreatePlayer_Success() throws Exception {
        CreatePlayerDTO createPlayerDTO = new CreatePlayerDTO();
        createPlayerDTO.setName("John");
        createPlayerDTO.setPseudonym("JohnDoe");
        createPlayerDTO.setEmail("john.doe@example.com");

        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(1L);
        playerDTO.setName("John");
        playerDTO.setPseudonym("JohnDoe");
        playerDTO.setEmail("john.doe@example.com");

        when(playerService.createPlayer(any(CreatePlayerDTO.class))).thenReturn(playerDTO);

        mockMvc.perform(post("/players/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPlayerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.pseudonym").value("JohnDoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testCreatePlayer_ValidationFailure() throws Exception {
        CreatePlayerDTO createPlayerDTO = new CreatePlayerDTO();
        createPlayerDTO.setName("");
        createPlayerDTO.setPseudonym("");
        createPlayerDTO.setEmail("invalid-email");

        mockMvc.perform(post("/players/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPlayerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pseudonym cannot be blank, Name cannot be blank, Email should be valid"));
    }

    @Test
    void testDeletePlayer_Success() throws Exception {
        when(playerService.deletePlayer(1L)).thenReturn(true);

        mockMvc.perform(delete("/players/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Player was deleted successfully"));
    }

    @Test
    void testDeletePlayer_NotFound() throws Exception {
        when(playerService.deletePlayer(1L)).thenReturn(false);

        mockMvc.perform(delete("/players/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Player with ID 1 not found"));
    }

    @Test
    void testGetPlayerInformation_Success() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(1L);
        playerDTO.setName("John");
        playerDTO.setPseudonym("JohnDoe");
        playerDTO.setEmail("john.doe@example.com");

        when(playerService.getPlayerInformations(1L)).thenReturn(Optional.of(playerDTO));

        mockMvc.perform(get("/players/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.pseudonym").value("JohnDoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetPlayerInformation_NotFound() throws Exception {
        when(playerService.getPlayerInformations(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/players/get/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Player with ID 1 not found"));
    }

    @Test
    void testUpdatePlayer_Success() throws Exception {
        UpdatePlayerDTO updatePlayerDTO = new UpdatePlayerDTO();
        updatePlayerDTO.setName("UpdatedName");
        updatePlayerDTO.setEmail("updated.email@example.com");
        updatePlayerDTO.setPseudonym("UpdatedPseudonym");

        PlayerDTO updatedPlayer = new PlayerDTO();
        updatedPlayer.setId(1L);
        updatedPlayer.setName("UpdatedName");
        updatedPlayer.setPseudonym("UpdatedPseudonym");
        updatedPlayer.setEmail("updated.email@example.com");

        when(playerService.updatePlayer(eq(1L), any(UpdatePlayerDTO.class))).thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePlayerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.email").value("updated.email@example.com"))
                .andExpect(jsonPath("$.pseudonym").value("UpdatedPseudonym"));
    }

    @Test
    void testUpdatePlayerStats_Success() throws Exception {
        UpdatePlayerStatisticsDTO updatePlayerStatisticsDTO = new UpdatePlayerStatisticsDTO();
        updatePlayerStatisticsDTO.setPoints(100);

        PlayerDTO updatedPlayer = new PlayerDTO();
        updatedPlayer.setId(1L);
        updatedPlayer.setTotalPoints(200);

        when(playerService.updatePlayerStatistics(eq(1L), any(UpdatePlayerStatisticsDTO.class))).thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/update/stats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePlayerStatisticsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalPoints").value(200));
    }
}

