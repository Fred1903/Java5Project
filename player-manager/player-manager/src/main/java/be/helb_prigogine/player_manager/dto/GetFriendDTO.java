package be.helb_prigogine.player_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetFriendDTO {
    private String pseudonym;
    private Integer level;
    private Integer totalPoints;
}
