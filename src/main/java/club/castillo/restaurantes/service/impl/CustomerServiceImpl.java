package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.CustomerRequestDTO;
import club.castillo.restaurantes.dto.CustomerResponseDTO;
import club.castillo.restaurantes.model.Customer;
import club.castillo.restaurantes.model.CustomerType;
import club.castillo.restaurantes.repository.CustomerRepository;
import club.castillo.restaurantes.repository.CustomerTypeRepository;
import club.castillo.restaurantes.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        if (requestDTO.getIdentification() != null &&
                customerRepository.existsByIdentification(requestDTO.getIdentification())) {
            throw new IllegalArgumentException("Ya existe un cliente con esa identificación");
        }
        CustomerType type = customerTypeRepository.findById(requestDTO.getCustomerTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        Customer customer = Customer.builder()
                .name(requestDTO.getName())
                .identification(requestDTO.getIdentification())
                .email(requestDTO.getEmail())
                .customerType(type)
                .status(Optional.ofNullable(requestDTO.getStatus()).orElse("active"))
                .joinedAt(requestDTO.getJoinedAt())
                .active(true)
                .build();
        Customer saved = customerRepository.save(customer);
        return mapToDTO(saved);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        if (requestDTO.getIdentification() != null &&
                !requestDTO.getIdentification().equals(customer.getIdentification()) &&
                customerRepository.existsByIdentification(requestDTO.getIdentification())) {
            throw new IllegalArgumentException("Ya existe un cliente con esa identificación");
        }
        CustomerType type = customerTypeRepository.findById(requestDTO.getCustomerTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cliente no encontrado"));
        customer.setName(requestDTO.getName());
        customer.setIdentification(requestDTO.getIdentification());
        customer.setEmail(requestDTO.getEmail());
        customer.setStatus(requestDTO.getStatus());
        customer.setJoinedAt(requestDTO.getJoinedAt());
        customer.setCustomerType(type);
        Customer updated = customerRepository.save(customer);
        return mapToDTO(updated);
    }

    @Override
    public void disableById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    @Override
    public void enableById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        customer.setActive(true);
        customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        return mapToDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllActiveCustomers() {
        return customerRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerByIdentification(String identification) {
        Customer customer = customerRepository.findByIdentification(identification)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        return mapToDTO(customer);
    }

    private CustomerResponseDTO mapToDTO(Customer customer) {
        CustomerResponseDTO.CustomerTypeDTO typeDTO = null;
        if (customer.getCustomerType() != null) {
            typeDTO = CustomerResponseDTO.CustomerTypeDTO.builder()
                    .id(customer.getCustomerType().getId())
                    .name(customer.getCustomerType().getName())
                    .build();
        }
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .identification(customer.getIdentification())
                .email(customer.getEmail())
                .status(customer.getStatus())
                .joinedAt(customer.getJoinedAt())
                .active(customer.getActive())
                .customerType(typeDTO)
                .build();
    }
}
