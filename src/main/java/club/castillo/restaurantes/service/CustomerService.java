package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.CustomerRequestDTO;
import club.castillo.restaurantes.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);
    void disableById(Long id);
    void enableById(Long id);
    CustomerResponseDTO getCustomerById(Long id);
    List<CustomerResponseDTO> getAllCustomers();
    List<CustomerResponseDTO> getAllActiveCustomers();
    CustomerResponseDTO getCustomerByIdentification(String identification);
}
