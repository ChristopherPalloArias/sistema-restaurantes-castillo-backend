package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.OrderResponseDTO;
import club.castillo.restaurantes.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendOrderUpdate(OrderResponseDTO order) {
        String destination = "/topic/orders/" + order.getRestaurantId();
        messagingTemplate.convertAndSend(destination, order);
    }
}
