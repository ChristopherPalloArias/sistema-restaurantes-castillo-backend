package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.QrCodeResponseDTO;

public interface QrCodeService {
    QrCodeResponseDTO generateOrderQrCode(Long orderId);
    QrCodeResponseDTO generateMenuQrCode(Long menuId);
    QrCodeResponseDTO getQrCodeById(Long id);
    /**
     * Validates that a QR code exists and is active.
     *
     * @param id identifier of the QR code to validate
     * @return the QR code information if valid
     */
    QrCodeResponseDTO validateQrCode(Long id);
}
