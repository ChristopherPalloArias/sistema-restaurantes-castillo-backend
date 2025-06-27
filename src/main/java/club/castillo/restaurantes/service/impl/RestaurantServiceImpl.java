package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.RestaurantRequestDTO;
import club.castillo.restaurantes.dto.RestaurantResponseDTO;
import club.castillo.restaurantes.model.Restaurant;
import club.castillo.restaurantes.model.RestaurantStatus;
import club.castillo.restaurantes.model.User;
import club.castillo.restaurantes.model.Zone;
import club.castillo.restaurantes.repository.RestaurantRepository;
import club.castillo.restaurantes.repository.UserRepository;
import club.castillo.restaurantes.repository.ZoneRepository;
import club.castillo.restaurantes.service.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ZoneRepository zoneRepository;

    @Override
    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(requestDTO.getName());
        restaurant.setAddress(requestDTO.getAddress());
        restaurant.setStatus(Optional.ofNullable(requestDTO.getStatus()).orElse(RestaurantStatus.CLOSED));
        restaurant.setActive(true);

        if (requestDTO.getManagerId() != null) {
            User manager = userRepository.findById(requestDTO.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            restaurant.setManager(manager);
        }

        if (requestDTO.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
            restaurant.setZone(zone);
        }

        Restaurant saved = restaurantRepository.save(restaurant);
        return mapToResponseDTO(saved);
    }

    @Override
    public RestaurantResponseDTO updateRestaurant(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));

        restaurant.setName(requestDTO.getName());
        restaurant.setAddress(requestDTO.getAddress());
        if (requestDTO.getStatus() != null) {
            restaurant.setStatus(requestDTO.getStatus());
        }

        if (requestDTO.getManagerId() != null) {
            User manager = userRepository.findById(requestDTO.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            restaurant.setManager(manager);
        }

        if (requestDTO.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada"));
            restaurant.setZone(zone);
        }

        Restaurant updated = restaurantRepository.save(restaurant);
        return mapToResponseDTO(updated);
    }

    @Override
    public void disableRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void enableRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
        restaurant.setActive(true);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
        return mapToResponseDTO(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getAllActiveRestaurants() {
        return restaurantRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getRestaurantsByStatus(RestaurantStatus status) {
        return restaurantRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponseDTO updateRestaurantStatus(Long id, RestaurantStatus status) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
        restaurant.setStatus(status);
        Restaurant saved = restaurantRepository.save(restaurant);
        return mapToResponseDTO(saved);
    }

    private RestaurantResponseDTO mapToResponseDTO(Restaurant restaurant) {
        RestaurantResponseDTO.ManagerDTO managerDTO = null;
        if (restaurant.getManager() != null) {
            managerDTO = RestaurantResponseDTO.ManagerDTO.builder()
                    .id(restaurant.getManager().getId())
                    .name(restaurant.getManager().getName())
                    .email(restaurant.getManager().getEmail())
                    .build();
        }

        Long zoneId = null;
        String zoneName = null;
        if (restaurant.getZone() != null) {
            zoneId = restaurant.getZone().getId();
            zoneName = restaurant.getZone().getName();
        }

        return RestaurantResponseDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .status(restaurant.getStatus())
                .active(restaurant.getActive())
                .manager(managerDTO)
                .zoneId(zoneId)
                .zoneName(zoneName)
                .build();
    }
}
