package be.helb_prigogine.player_manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFriendDTO {
    @NotNull
    private Long idFriend;
    @NotNull
    private Long idPlayer;    
}
