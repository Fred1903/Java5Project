package be.helb_prigogine.player_manager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.helb_prigogine.player_manager.entities.Player;



@Repository
public interface PlayerRepository extends JpaRepository<Player,Long>{
    Optional<Player> findByEmail(String email);
    Optional<Player> findByPseudonym(String pseudonym);
} 