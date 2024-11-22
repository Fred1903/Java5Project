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

import java.time.LocalDate;      ///////////////LES ONE TO MANY et MANY TO ONE A VERIF ICI ET DANS AUTRES FICHIERS !!!!!
//////////////////////////ATTENTION POUR L INSTANT LES ENTITIES DES " PROJETS SONT DANS LE MEME DOSSIER ; A CHANGER !!!!"

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
@Table(name = "party", schema = "java5Project")
public class Party {
    @Column(name = "id")
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "partyDate")
    @Getter
    @Setter
    private LocalDate partyDate;

    @Column(name = "maximumScore")
    @Getter
    @Setter
    private int maximumScore;

    @OneToOne //many to one vers joueur et joinColumn car clé étrangère
    @JoinColumn(name="idPartyType", nullable=false)
    @Getter
    @Setter
    private int idPartyType;
}
