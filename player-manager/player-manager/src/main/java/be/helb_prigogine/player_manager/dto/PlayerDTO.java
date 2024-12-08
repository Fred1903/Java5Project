package be.helb_prigogine.player_manager.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {
    private Long id;
    private String name;
    private String email;
    private String pseudonym;
    private int level;
    private int totalPoints;
}
