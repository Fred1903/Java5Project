package main.java.be.helb_prigogine.game_manager.entities;

//Jakarta Persistence nécessaire à importer pour pouvoir use les @ suivants :
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "friend", schema = "java5Project")
public class Friend {
    @Column(name = "id")
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;


    @ManyToOne //many to one vers joueur et joinColumn car clé étrangère
    @JoinColumn(name="idPlayer", nullable=false)
    @Getter
    @Setter
    private int idPlayer;


    @OneToOne
    @JoinColumn(name="idFriend", nullable=false)
    @Getter
    @Setter
    private int idFriend;




}
