package be.helb_prigogine.player_manager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.helb_prigogine.player_manager.dto.PlayerDTO;
import be.helb_prigogine.player_manager.services.PlayerService;


@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // POST /joueurs : Ajouter un nouveau joueur
    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerDTO newPlayer = playerService.createPlayer(playerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPlayer);
    }
}

