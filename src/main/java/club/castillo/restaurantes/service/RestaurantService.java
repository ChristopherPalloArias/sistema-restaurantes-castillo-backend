package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.RestaurantRequestDTO;
import club.castillo.restaurantes.dto.RestaurantResponseDTO;
import club.castillo.restaurantes.model.RestaurantStatus;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDTO createRestaurant(RestaurantRequestDTO requestDTO);
    RestaurantResponseDTO updateRestaurant(Long id, RestaurantRequestDTO requestDTO);
    void disableRestaurant(Long id);
    void enableRestaurant(Long id);
    RestaurantResponseDTO getRestaurantById(Long id);
    List<RestaurantResponseDTO> getAllRestaurants();
    List<RestaurantResponseDTO> getAllActiveRestaurants();
    List<RestaurantResponseDTO> getRestaurantsByStatus(RestaurantStatus status);

    RestaurantResponseDTO updateRestaurantStatus(Long id, RestaurantStatus status);
}
