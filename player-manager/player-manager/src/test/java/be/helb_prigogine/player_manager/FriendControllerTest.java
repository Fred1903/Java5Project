package be.helb_prigogine.player_manager;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import be.helb_prigogine.player_manager.controllers.FriendController;
import be.helb_prigogine.player_manager.dto.GetFriendDTO;
import be.helb_prigogine.player_manager.services.FriendService;

class FriendControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private FriendController friendController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(friendController).build();
    }

    @Test
    void testAddFriend_Success() throws Exception {
        doNothing().when(friendService).addFriend(1L, 2L);

        mockMvc.perform(post("/friends/add")
                .param("idPlayer", "1")
                .param("idFriend", "2"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Friendship created successfully"));
    }


    @Test
    void testAddFriend_Failure() throws Exception {
        doThrow(new RuntimeException("Player not found")).when(friendService).addFriend(1L, 2L);

        mockMvc.perform(post("/friends/add")
                .param("idPlayer", "1")
                .param("idFriend", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Player not found"));
    }

    @Test
    void testRemoveFriend_Success() throws Exception {
        doNothing().when(friendService).removeFriend(1L, 2L);

        mockMvc.perform(delete("/friends/delete")
                .param("idPlayer", "1")
                .param("idFriend", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Friendship removed successfully"));
    }

    @Test
    void testRemoveFriend_Failure() throws Exception {
        doThrow(new RuntimeException("Not friends")).when(friendService).removeFriend(1L, 2L);

        mockMvc.perform(delete("/friends/delete")
                .param("idPlayer", "1")
                .param("idFriend", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not friends"));
    }

    @Test
    void testGetFriends_Success() throws Exception {
        GetFriendDTO friend1 = new GetFriendDTO("Friend1", 10, 100);
        GetFriendDTO friend2 = new GetFriendDTO("Friend2", 20, 200);

        when(friendService.getFriends(1L)).thenReturn(Arrays.asList(friend1, friend2));

        mockMvc.perform(get("/friends/list")
                .param("idPlayer", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pseudonym").value("Friend1"))
                .andExpect(jsonPath("$[0].level").value(10))
                .andExpect(jsonPath("$[0].totalPoints").value(100))
                .andExpect(jsonPath("$[1].pseudonym").value("Friend2"))
                .andExpect(jsonPath("$[1].level").value(20))
                .andExpect(jsonPath("$[1].totalPoints").value(200));
    }

    @Test
    void testGetFriends_Failure() throws Exception {
        when(friendService.getFriends(1L)).thenThrow(new RuntimeException("Player not found"));

        mockMvc.perform(get("/friends/list")
                .param("idPlayer", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Player not found"));
    }
}
