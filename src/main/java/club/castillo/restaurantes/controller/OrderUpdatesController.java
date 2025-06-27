package club.castillo.restaurantes.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class OrderUpdatesController {

    @SubscribeMapping("/orders/{restaurantId}")
    public void subscribe(@DestinationVariable Long restaurantId) {
        // Subscription endpoint for order updates
    }
}
