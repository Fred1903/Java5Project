package be.helb_prigogine.player_manager.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {
    private Long id;
    private String name;
    private String email;
    private String pseudonym;
    private Integer level;   
    private Integer totalPoints;
    private List<GetFriendDTO> friends;
}
