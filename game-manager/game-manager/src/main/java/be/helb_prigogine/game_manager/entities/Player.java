package main.java.be.helb_prigogine.game_manager.entities;

//Jakarta Persistence nécessaire à importer pour pouvoir use les @ suivants :
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//Lombok nécessaire pour pouvoir use les @ suivants :
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor 
@Entity
@Table(name = "player", schema = "java5Project")
public class Player {
    @Column(name = "id")
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column(name = "pseudonym")
    @Getter
    @Setter
    private String pseudonym;

    @Column(name = "email")
    @Getter
    @Setter
    private String email;

    @Column(name = "level")
    @Getter
    @Setter
    private int level;

    @Column(name = "totalPoints")
    @Getter
    @Setter
    private int totalPoints;
}
