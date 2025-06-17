package club.castillo.restaurantes.controller;

import org.springframework.messaging.handler.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class OrderUpdatesController {

    @SubscribeMapping("/orders")
    public void subscribe() {
        // Subscription endpoint for order updates
    }
}
