package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.CustomerTypeRequestDTO;
import club.castillo.restaurantes.dto.CustomerTypeResponseDTO;
import club.castillo.restaurantes.model.CustomerType;
import club.castillo.restaurantes.repository.CustomerTypeRepository;
import club.castillo.restaurantes.service.CustomerTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final CustomerTypeRepository customerTypeRepository;

    @Override
    public CustomerTypeResponseDTO createCustomerType(CustomerTypeRequestDTO requestDTO) {
        if (customerTypeRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un tipo de cliente con ese nombre");
        }
        CustomerType customerType = CustomerType.builder()
                .name(requestDTO.getName())
                .active(true)
                .build();
        CustomerType saved = customerTypeRepository.save(customerType);
        return mapToDTO(saved);
    }

    @Override
    public CustomerTypeResponseDTO updateCustomerType(Long id, CustomerTypeRequestDTO requestDTO) {
        CustomerType customerType = customerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        if (!customerType.getName().equals(requestDTO.getName()) &&
                customerTypeRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un tipo de cliente con ese nombre");
        }
        customerType.setName(requestDTO.getName());
        CustomerType updated = customerTypeRepository.save(customerType);
        return mapToDTO(updated);
    }

    @Override
    public void disableById(Long id) {
        CustomerType type = customerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        type.setActive(false);
        customerTypeRepository.save(type);
    }

    @Override
    public void enableById(Long id) {
        CustomerType type = customerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        type.setActive(true);
        customerTypeRepository.save(type);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerTypeResponseDTO getCustomerTypeById(Long id) {
        CustomerType type = customerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        return mapToDTO(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerTypeResponseDTO> getAllCustomerTypes() {
        return customerTypeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerTypeResponseDTO> getAllActiveCustomerTypes() {
        return customerTypeRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerTypeResponseDTO getCustomerTypeByName(String name) {
        CustomerType type = customerTypeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        return mapToDTO(type);
    }

    private CustomerTypeResponseDTO mapToDTO(CustomerType customerType) {
        return CustomerTypeResponseDTO.builder()
                .id(customerType.getId())
                .name(customerType.getName())
                .active(customerType.getActive())
                .build();
    }
}
