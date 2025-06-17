package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findByActiveTrue();
}
