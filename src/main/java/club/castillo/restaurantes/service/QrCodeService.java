package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.QrCodeResponseDTO;

public interface QrCodeService {
    QrCodeResponseDTO generateOrderQrCode(Long orderId);
    QrCodeResponseDTO generateMenuQrCode(Long menuId);
    QrCodeResponseDTO getQrCodeById(Long id);
}
