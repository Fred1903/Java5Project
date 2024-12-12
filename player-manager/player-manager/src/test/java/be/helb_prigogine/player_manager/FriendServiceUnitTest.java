package be.helb_prigogine.player_manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import be.helb_prigogine.player_manager.dao.IFriendshipDAO;
import be.helb_prigogine.player_manager.dao.IPlayerDAO;
import be.helb_prigogine.player_manager.dto.GetFriendDTO;
import be.helb_prigogine.player_manager.entities.Friendship;
import be.helb_prigogine.player_manager.entities.Player;
import be.helb_prigogine.player_manager.services.FriendService;

class FriendServiceUnitTest {

    @Mock
    private IFriendshipDAO friendshipDAO;

    @Mock
    private IPlayerDAO playerDAO;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFriend_Success() {
        Long playerId = 1L;
        Long friendId = 2L;

        Player player = new Player();
        player.setId(playerId);
        Player friend = new Player();
        friend.setId(friendId);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(player));
        when(playerDAO.findPlayerById(friendId)).thenReturn(Optional.of(friend));
        when(friendshipDAO.findFriendByIds(playerId, friendId)).thenReturn(Optional.empty());

        friendService.addFriend(playerId, friendId);

        verify(friendshipDAO, times(2)).saveFriend(any(Friendship.class));//2 because biderectionnal
    }

    @Test
    void testAddFriend_Failure_PlayerNotFound() {
        Long playerId = 1L;
        Long friendId = 2L;

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(playerId, friendId);
        });

        assertEquals("Player with ID " + playerId + " does not exist", exception.getMessage());
    }

    @Test
    void testAddFriend_Failure_AlreadyFriends() {
        Long playerId = 1L;
        Long friendId = 2L;

        Player player = new Player();
        player.setId(playerId);
        Player friend = new Player();
        friend.setId(friendId);

        Friendship friendship = new Friendship();

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(player));
        when(playerDAO.findPlayerById(friendId)).thenReturn(Optional.of(friend));
        when(friendshipDAO.findFriendByIds(playerId, friendId)).thenReturn(Optional.of(friendship));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(playerId, friendId);
        });

        assertEquals("These players are already friends", exception.getMessage());
    }

    @Test
    void testRemoveFriend_Success() {
        Long playerId = 1L;
        Long friendId = 2L;

        Player player = new Player();
        player.setId(playerId);
        Player friend = new Player();
        friend.setId(friendId);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(player));
        when(playerDAO.findPlayerById(friendId)).thenReturn(Optional.of(friend));
        when(friendshipDAO.findFriendByIds(playerId, friendId)).thenReturn(Optional.of(new Friendship()));

        friendService.removeFriend(playerId, friendId);

        verify(friendshipDAO, times(1)).deleteFriendByIds(playerId, friendId);
    }

    @Test
    void testRemoveFriend_Failure_NotFriends() {
        Long playerId = 1L;
        Long friendId = 2L;

        Player player = new Player();
        player.setId(playerId);
        Player friend = new Player();
        friend.setId(friendId);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(player));
        when(playerDAO.findPlayerById(friendId)).thenReturn(Optional.of(friend));
        when(friendshipDAO.findFriendByIds(playerId, friendId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.removeFriend(playerId, friendId);
        });

        assertEquals("These players are not friends", exception.getMessage());
    }

    @Test
    void testGetFriends_Success() {
        Long playerId = 1L;

        Player player = new Player();
        player.setId(playerId);

        List<Friendship> friendships = new ArrayList<>();
        Friendship friendship = new Friendship();
        friendship.setIdPlayer(playerId);
        friendship.setIdFriend(2L);
        friendships.add(friendship);

        Player friend = new Player();
        friend.setId(2L);
        friend.setPseudonym("FriendPseudo");
        friend.setLevel(10);
        friend.setTotalPoints(500);

        GetFriendDTO friendDTO = new GetFriendDTO("FriendPseudo", 10, 500);

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.of(player));
        when(friendshipDAO.findFriendsByIdPlayer(playerId)).thenReturn(friendships);
        when(playerDAO.findPlayerById(2L)).thenReturn(Optional.of(friend));
        when(modelMapper.map(friend, GetFriendDTO.class)).thenReturn(friendDTO);

        List<GetFriendDTO> result = friendService.getFriends(playerId);

        assertEquals(1, result.size());
        assertEquals("FriendPseudo", result.get(0).getPseudonym());
        assertEquals(10, result.get(0).getLevel());
        assertEquals(500, result.get(0).getTotalPoints());
    }

    @Test
    void testGetFriends_Failure_PlayerNotFound() {
        Long playerId = 1L;

        when(playerDAO.findPlayerById(playerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.getFriends(playerId);
        });

        assertEquals("Player with ID " + playerId + " does not exist", exception.getMessage());
    }
}

