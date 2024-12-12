package be.helb_prigogine.player_manager.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerDTO;
import be.helb_prigogine.player_manager.dto.UpdatePlayerStatisticsDTO;
import be.helb_prigogine.player_manager.services.PlayerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/players")
@Validated
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // POST /players : Create a new player
    @PostMapping("/create") //On ne met pas <PlayerDTO> car alors on ne pourra pas renvoyer un String en cas d'erreur dans le catch
    public ResponseEntity<?> createPlayer(@Valid @RequestBody CreatePlayerDTO createPlayerDTO,BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) { //Errors like @Email not valid or blank fields...
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErroMessage(bindingResult));
            }
            // We can just make playerService.createPlayer but then we wont return the newPlayer in the HTTP response
            PlayerDTO newPlayer = playerService.createPlayer(createPlayerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPlayer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error with the player creation : "+e.getMessage());
        }
    }

    // DELETE /players/{id} : Delete player by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        try {
            if (playerService.deletePlayer(id))
                return ResponseEntity.status(HttpStatus.OK).body("Player was deleted successfully");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player with ID " + id + " not found");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error with the player deletion : "+e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPlayerInformation(@PathVariable Long id) {
        try {
            Optional<PlayerDTO> playerDTO = playerService.getPlayerInformations(id);
            if(playerDTO.isPresent())
                return ResponseEntity.status(HttpStatus.OK).body(playerDTO);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player with ID " + id + " not found");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting the player : "+e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @Valid @RequestBody UpdatePlayerDTO updatePlayerDTO,BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErroMessage(bindingResult));
            }                                                             
            PlayerDTO updatedPlayer = playerService.updatePlayer(id, updatePlayerDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while updating the player : "+e.getMessage());
        }
    }

    @PutMapping("update/stats/{id}")
    public ResponseEntity<?> updatePlayerStats(@PathVariable Long id, @RequestBody UpdatePlayerStatisticsDTO updatePlayerStatisticsDTO) {
        System.out.println("HMMMMMMMMMMM");
        try {
            PlayerDTO updatedPlayer = playerService.updatePlayerStatistics(id, updatePlayerStatisticsDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPlayer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error while updating the player : "+e.getMessage());
        }
    }


    public String getErroMessage(BindingResult bindingResult){
            return bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("Validation failed");
    }
}

