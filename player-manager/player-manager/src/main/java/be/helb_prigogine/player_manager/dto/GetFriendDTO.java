package be.helb_prigogine.player_manager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFriendDTO {
    private String pseudonym;
    private int level;
    private int totalPoints;
}
