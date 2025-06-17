package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.ZoneRequestDTO;
import club.castillo.restaurantes.dto.ZoneResponseDTO;
import club.castillo.restaurantes.model.Zone;
import club.castillo.restaurantes.repository.ZoneRepository;
import club.castillo.restaurantes.service.ZoneService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    @Override
    public ZoneResponseDTO createZone(ZoneRequestDTO requestDTO) {
        Zone zone = Zone.builder()
                .name(requestDTO.getName())
                .active(true)
                .build();
        Zone saved = zoneRepository.save(zone);
        return mapToDto(saved);
    }

    @Override
    public ZoneResponseDTO updateZone(Long id, ZoneRequestDTO requestDTO) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
        zone.setName(requestDTO.getName());
        Zone updated = zoneRepository.save(zone);
        return mapToDto(updated);
    }

    @Override
    public void disableZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
        zone.setActive(false);
        zoneRepository.save(zone);
    }

    @Override
    public void enableZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
        zone.setActive(true);
        zoneRepository.save(zone);
    }

    @Override
    @Transactional(readOnly = true)
    public ZoneResponseDTO getZoneById(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
        return mapToDto(zone);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZoneResponseDTO> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZoneResponseDTO> getAllActiveZones() {
        return zoneRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ZoneResponseDTO mapToDto(Zone zone) {
        return ZoneResponseDTO.builder()
                .id(zone.getId())
                .name(zone.getName())
                .active(zone.getActive())
                .build();
    }
}
