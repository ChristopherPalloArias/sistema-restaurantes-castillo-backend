package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.CustomerTypeRequestDTO;
import club.castillo.restaurantes.dto.CustomerTypeResponseDTO;

import java.util.List;

public interface CustomerTypeService {
    CustomerTypeResponseDTO createCustomerType(CustomerTypeRequestDTO requestDTO);
    CustomerTypeResponseDTO updateCustomerType(Long id, CustomerTypeRequestDTO requestDTO);
    void disableById(Long id);
    void enableById(Long id);
    CustomerTypeResponseDTO getCustomerTypeById(Long id);
    List<CustomerTypeResponseDTO> getAllCustomerTypes();
    List<CustomerTypeResponseDTO> getAllActiveCustomerTypes();
    CustomerTypeResponseDTO getCustomerTypeByName(String name);
}
