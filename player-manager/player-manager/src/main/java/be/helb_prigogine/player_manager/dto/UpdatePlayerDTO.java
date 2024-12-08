package be.helb_prigogine.player_manager.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePlayerDTO {
    @Email(message = "Email should be valid")
    private String email;
    private String name;
    private String pseudonym;
}