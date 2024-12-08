package be.helb_prigogine.player_manager.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.helb_prigogine.player_manager.dto.GetFriendDTO;
import be.helb_prigogine.player_manager.services.FriendService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    public FriendController(FriendService friendService){
        this.friendService=friendService;
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam Long idPlayer, @RequestParam Long idFriend) {
        try {
            friendService.addFriend(idPlayer, idFriend);
            return ResponseEntity.status(HttpStatus.CREATED).body("Friendship created successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeFriend(@RequestParam Long idPlayer, @RequestParam Long idFriend) {
        try {
            friendService.removeFriend(idPlayer, idFriend);
            return ResponseEntity.status(HttpStatus.OK).body("Friendship removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getFriends(@RequestParam Long idPlayer) {
        try {
            List<GetFriendDTO> friends = friendService.getFriends(idPlayer);
            return ResponseEntity.status(HttpStatus.OK).body(friends);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
