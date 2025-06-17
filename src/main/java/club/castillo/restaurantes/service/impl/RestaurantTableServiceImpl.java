package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.RestaurantTableRequestDTO;
import club.castillo.restaurantes.dto.RestaurantTableResponseDTO;
import club.castillo.restaurantes.model.Restaurant;
import club.castillo.restaurantes.model.RestaurantTable;
import club.castillo.restaurantes.model.Zone;
import club.castillo.restaurantes.repository.RestaurantRepository;
import club.castillo.restaurantes.repository.RestaurantTableRepository;
import club.castillo.restaurantes.repository.ZoneRepository;
import club.castillo.restaurantes.service.RestaurantTableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final ZoneRepository zoneRepository;

    @Override
    public RestaurantTableResponseDTO createTable(RestaurantTableRequestDTO requestDTO) {
        RestaurantTable table = new RestaurantTable();
        table.setNumber(requestDTO.getNumber());
        table.setStatus(requestDTO.getStatus());

        if (requestDTO.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
            table.setRestaurant(restaurant);
        }

        if (requestDTO.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
            table.setZone(zone);
        }

        table.setActive(true);
        RestaurantTable saved = tableRepository.save(table);
        return mapToDto(saved);
    }

    @Override
    public RestaurantTableResponseDTO updateTable(Long id, RestaurantTableRequestDTO requestDTO) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada"));

        table.setNumber(requestDTO.getNumber());
        if (requestDTO.getStatus() != null) {
            table.setStatus(requestDTO.getStatus());
        }

        if (requestDTO.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
            table.setRestaurant(restaurant);
        }

        if (requestDTO.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
            table.setZone(zone);
        }

        RestaurantTable updated = tableRepository.save(table);
        return mapToDto(updated);
    }

    @Override
    public void disableTable(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada"));
        table.setActive(false);
        tableRepository.save(table);
    }

    @Override
    public void enableTable(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada"));
        table.setActive(true);
        tableRepository.save(table);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantTableResponseDTO getTableById(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada"));
        return mapToDto(table);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantTableResponseDTO> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantTableResponseDTO> getAllActiveTables() {
        return tableRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RestaurantTableResponseDTO mapToDto(RestaurantTable table) {
        RestaurantTableResponseDTO.RestaurantDTO restaurantDTO = null;
        if (table.getRestaurant() != null) {
            restaurantDTO = RestaurantTableResponseDTO.RestaurantDTO.builder()
                    .id(table.getRestaurant().getId())
                    .name(table.getRestaurant().getName())
                    .build();
        }

        RestaurantTableResponseDTO.ZoneDTO zoneDTO = null;
        if (table.getZone() != null) {
            zoneDTO = RestaurantTableResponseDTO.ZoneDTO.builder()
                    .id(table.getZone().getId())
                    .name(table.getZone().getName())
                    .build();
        }

        return RestaurantTableResponseDTO.builder()
                .id(table.getId())
                .number(table.getNumber())
                .status(table.getStatus())
                .active(table.getActive())
                .restaurant(restaurantDTO)
                .zone(zoneDTO)
                .build();
    }
}
