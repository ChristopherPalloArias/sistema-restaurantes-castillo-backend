package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.model.Order;
import club.castillo.restaurantes.model.OrderItem;
import club.castillo.restaurantes.repository.OrderRepository;
import club.castillo.restaurantes.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;

    @Override
    public byte[] generateSalesReport(Long restaurantId, LocalDate startDate, LocalDate endDate) throws IOException {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        List<Order> orders = orderRepository.findByRestaurantIdAndCreatedAtBetween(restaurantId, start, end);

        Map<LocalDate, Long> ordersPerDate = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCreatedAt().toLocalDate(), Collectors.counting()));

        Map<String, Integer> productTotals = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(i -> i.getProduct().getName(), Collectors.summingInt(OrderItem::getQuantity)));

        List<Map.Entry<String, Integer>> topProducts = productTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .toList();

        double avgCompletionMinutes = orders.stream()
                .filter(o -> o.getCompletedAt() != null)
                .mapToLong(o -> Duration.between(o.getCreatedAt(), o.getCompletedAt()).toMinutes())
                .average()
                .orElse(0);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet ordersSheet = workbook.createSheet("OrdersPerDate");
            Row header = ordersSheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Orders");
            int rowIdx = 1;
            for (Map.Entry<LocalDate, Long> entry : ordersPerDate.entrySet()) {
                Row row = ordersSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            Sheet productSheet = workbook.createSheet("TopProducts");
            Row h = productSheet.createRow(0);
            h.createCell(0).setCellValue("Product");
            h.createCell(1).setCellValue("Quantity");
            rowIdx = 1;
            for (Map.Entry<String, Integer> entry : topProducts) {
                Row row = productSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            Sheet metricsSheet = workbook.createSheet("Metrics");
            Row m = metricsSheet.createRow(0);
            m.createCell(0).setCellValue("Average completion minutes");
            m.createCell(1).setCellValue(avgCompletionMinutes);

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
