package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.MenuResponseDTO;

import java.util.Optional;

public interface MenuService {
    /**
     * Obtiene el menú completo de un restaurante específico.
     * 
     * @param restaurantId ID del restaurante
     * @param showUnavailable si es true, incluye productos no disponibles
     * @param maxPrice precio máximo para filtrar productos (opcional)
     * @param searchTerm término de búsqueda para filtrar productos (opcional)
     * @return el menú del restaurante con sus categorías y productos
     */
    MenuResponseDTO getRestaurantMenu(Long restaurantId, 
                                    boolean showUnavailable, 
                                    Optional<Double> maxPrice, 
                                    Optional<String> searchTerm);
} 