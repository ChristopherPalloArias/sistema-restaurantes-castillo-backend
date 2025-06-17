package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.ZoneRequestDTO;
import club.castillo.restaurantes.dto.ZoneResponseDTO;

import java.util.List;

public interface ZoneService {
    ZoneResponseDTO createZone(ZoneRequestDTO requestDTO);
    ZoneResponseDTO updateZone(Long id, ZoneRequestDTO requestDTO);
    void disableZone(Long id);
    void enableZone(Long id);
    ZoneResponseDTO getZoneById(Long id);
    List<ZoneResponseDTO> getAllZones();
    List<ZoneResponseDTO> getAllActiveZones();
}
