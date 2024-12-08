package be.helb_prigogine.player_manager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;

import be.helb_prigogine.player_manager.dto.CreatePlayerDTO;
import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.services.PlayerService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // POST /players : Create a new player
    @PostMapping //On ne met pas <PlayerDTO> car alors on ne pourra pas renvoyer un String en cas d'erreur dans le catch
    public ResponseEntity<?> createPlayer(@Valid @RequestBody CreatePlayerDTO createPlayerDTO,BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) { //Errors like @Email not valid or blank fields...
                // If there are validation errors, return them as a bad request
                String errorMessages = bindingResult.getAllErrors()
                        .stream()
                        .map(error -> error.getDefaultMessage())
                        .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                        .orElse("Validation failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
            }

            // We can just make playerService.createPlayer but then we wont return the newPlayer in the HTTP response
            PlayerDTO newPlayer = playerService.createPlayer(createPlayerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPlayer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error with the player creation : "+e.getMessage());
        }
    }
}

