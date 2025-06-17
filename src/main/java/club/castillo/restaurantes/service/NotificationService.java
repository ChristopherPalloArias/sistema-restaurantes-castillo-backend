package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.OrderResponseDTO;

public interface NotificationService {
    void sendOrderUpdate(OrderResponseDTO order);
}
