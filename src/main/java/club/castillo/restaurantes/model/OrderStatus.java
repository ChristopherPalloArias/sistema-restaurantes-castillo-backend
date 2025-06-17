package club.castillo.restaurantes.model;

public enum OrderStatus {
    PENDING,           // Pedido creado pero no confirmado
    CONFIRMED,        // Pedido confirmado y en preparación
    IN_PREPARATION,   // Pedido en preparación en cocina
    READY,           // Pedido listo para entregar
    DELIVERED,       // Pedido entregado al cliente
    CANCELLED        // Pedido cancelado (solo posible en estado PENDING)
} 