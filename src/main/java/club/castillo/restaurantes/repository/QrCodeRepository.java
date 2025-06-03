package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}
