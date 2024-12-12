package be.helb_prigogine.player_manager.entities;

//Jakarta Persistence nécessaire à importer pour pouvoir use les @ suivants :
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//Lombok nécessaire pour pouvoir use les @ suivants :
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor 
@Getter
@Setter
@Table(name = "player", schema = "java5project")
public class Player {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; //On est pas obligé de préciser @Column et leur nom, ca s'est juste si on veut changer le nom
    //dans la db sinon meme nom que variable
    private String name;
    private String pseudonym;
    private String email;
    private Integer level;
    private Integer totalPoints;
}
