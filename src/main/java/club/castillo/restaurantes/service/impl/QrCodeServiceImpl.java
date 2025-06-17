package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.QrCodeResponseDTO;
import club.castillo.restaurantes.model.Menu;
import club.castillo.restaurantes.model.Order;
import club.castillo.restaurantes.model.QrCode;
import club.castillo.restaurantes.repository.MenuRepository;
import club.castillo.restaurantes.repository.OrderRepository;
import club.castillo.restaurantes.repository.QrCodeRepository;
import club.castillo.restaurantes.service.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    @Override
    public QrCodeResponseDTO generateOrderQrCode(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        String content = generateBase64Qr("ORDER:" + orderId);
        QrCode qrCode = QrCode.builder()
                .order(order)
                .content(content)
                .active(true)
                .build();
        QrCode saved = qrCodeRepository.save(qrCode);
        return mapToDto(saved);
    }

    @Override
    public QrCodeResponseDTO generateMenuQrCode(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menú no encontrado"));
        String content = generateBase64Qr("MENU:" + menuId);
        QrCode qrCode = QrCode.builder()
                .menu(menu)
                .content(content)
                .active(true)
                .build();
        QrCode saved = qrCodeRepository.save(qrCode);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QrCodeResponseDTO getQrCodeById(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Código QR no encontrado"));
        return mapToDto(qrCode);
    }

    private String generateBase64Qr(String text) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR", e);
        }
    }

    private QrCodeResponseDTO mapToDto(QrCode qrCode) {
        return QrCodeResponseDTO.builder()
                .id(qrCode.getId())
                .orderId(qrCode.getOrder() != null ? qrCode.getOrder().getId() : null)
                .menuId(qrCode.getMenu() != null ? qrCode.getMenu().getId() : null)
                .content(qrCode.getContent())
                .generatedAt(qrCode.getGeneratedAt())
                .active(qrCode.getActive())
                .build();
    }
}
