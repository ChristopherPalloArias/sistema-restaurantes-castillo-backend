package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.QrCodeResponseDTO;
import club.castillo.restaurantes.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcodes")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @PostMapping("/order/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QrCodeResponseDTO> generateOrderQr(@PathVariable Long id) {
        return ResponseEntity.ok(qrCodeService.generateOrderQrCode(id));
    }

    @PostMapping("/menu/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QrCodeResponseDTO> generateMenuQr(@PathVariable Long id) {
        return ResponseEntity.ok(qrCodeService.generateMenuQrCode(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QrCodeResponseDTO> getQr(@PathVariable Long id) {
        return ResponseEntity.ok(qrCodeService.getQrCodeById(id));
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<QrCodeResponseDTO> validateQr(@PathVariable Long id) {
        return ResponseEntity.ok(qrCodeService.validateQrCode(id));
    }
}
