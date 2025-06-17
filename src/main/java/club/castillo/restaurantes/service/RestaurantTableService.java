package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.RestaurantTableRequestDTO;
import club.castillo.restaurantes.dto.RestaurantTableResponseDTO;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTableResponseDTO createTable(RestaurantTableRequestDTO requestDTO);
    RestaurantTableResponseDTO updateTable(Long id, RestaurantTableRequestDTO requestDTO);
    void disableTable(Long id);
    void enableTable(Long id);
    RestaurantTableResponseDTO getTableById(Long id);
    List<RestaurantTableResponseDTO> getAllTables();
    List<RestaurantTableResponseDTO> getAllActiveTables();
}
