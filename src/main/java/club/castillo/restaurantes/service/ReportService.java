package club.castillo.restaurantes.service;

import java.io.IOException;
import java.time.LocalDate;

public interface ReportService {
    byte[] generateSalesReport(Long restaurantId, LocalDate startDate, LocalDate endDate) throws IOException;
}
