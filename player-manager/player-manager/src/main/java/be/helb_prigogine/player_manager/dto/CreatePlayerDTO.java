package be.helb_prigogine.player_manager.dto;
//import org.hibernate.validator.constraints.Email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlayerDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Pseudonym cannot be blank")
    private String pseudonym;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;
}
