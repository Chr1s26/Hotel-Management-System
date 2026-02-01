package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.customer.CustomerCreateDTO;
import com.project.HotelManagementSystem.dto.customer.CustomerDTO;
import com.project.HotelManagementSystem.dto.customer.CustomerUpdateDTO;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CustomerRepository;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public CustomerCreateDTO createCustomer(CustomerCreateDTO customerCreateDTO) {
        Optional<Customer> customrOp = customerRepository.findByNameIgnoreCaseAndPhone(customerCreateDTO.getName(),customerCreateDTO.getPhone());
        if (customrOp.isPresent()) {
            throw new DuplicateException("customer",customerCreateDTO,"name","customers/create","A customer with the same name and same phone number already exists");
        }
        Customer customer = toCustomerEntity(customerCreateDTO);
        customerRepository.save(customer);
        return toCreateDTO(customer);
    }

    public CustomerUpdateDTO updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Optional<Customer> customerOp = customerRepository.findByNameIgnoreCaseAndPhoneAndIdNot(customerUpdateDTO.getName(), customerUpdateDTO.getPhone(), id);

        if (customerOp.isPresent()) {
            throw new DuplicateException("customer",customerUpdateDTO,"name","customers/edit","A customer with the same name and same phone number already exists");
        }

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("customer",customerUpdateDTO,"name","customers/edit","A customer with id not found"));
        customer.setName(customerUpdateDTO.getName());
        customer.setPhone(customerUpdateDTO.getPhone());
        customer.setDateOfBirth(customerUpdateDTO.getDateOfBirth());
        customer.setNationality(customerUpdateDTO.getNationality());
        customer.setVipStatus(customerUpdateDTO.isVipStatus());
        customer.setStatus(StatusType.ACTIVE);
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(authService.getCurrentUser());

        if(customerUpdateDTO.getUser() != null && (customer.getUser() == null || !customer.getUser().getId().equals(customerUpdateDTO.getUser()))) {
            User newUser = userRepository.findById(customerUpdateDTO.getUser()).orElseThrow(() -> new ResourceNotFoundException("customer",customerUpdateDTO,"name","customers/edit","A customer with user id not found"));
            customer.setUser(newUser);
        }

        customer = customerRepository.save(customer);
        return toUpdateDto(customer);
    }

    public void deleteCustomer(Long id) {
        Optional<Customer> customerOp = customerRepository.findById(id);
        if (customerOp.isEmpty()) {
            throw new ResourceNotFoundException("customer", customerOp.get(),"id","customers","An account with this id cannot be found");
        }
        Customer customer = customerOp.get();
        User user = customer.getUser();
        if(user != null){
            Role customerRole = roleRepository.findByRoleName("CUSTOMER").orElse(null);
            if(user.getRoles() != null){
                user.getRoles().remove(customerRole);
                userRepository.save(user);
            }
            user.setCustomer(null);
            customer.setUser(null);
        }
        customerRepository.delete(customer);
    }

    public CustomerDTO findCustomerById(Long id){
        Optional<Customer> customerOp = customerRepository.findById(id);
        if (customerOp.isEmpty()) {
            throw new ResourceNotFoundException("customer", customerOp.get(),"id","customers","An account with this id cannot be found");
        }
        return toDTO(customerOp.get());
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setDateOfBirth(customer.getDateOfBirth());
        customerDTO.setNationality(customer.getNationality());
        customerDTO.setVipStatus(customer.isVipStatus());
        customerDTO.setStatus(customer.getStatus());
        customerDTO.setUser(customer.getUser());
        customerDTO.setCreatedAt(customer.getCreatedAt());
        customerDTO.setUpdatedAt(customer.getUpdatedAt());
        customerDTO.setCreatedBy(customer.getCreatedBy());
        customerDTO.setUpdatedBy(customer.getUpdatedBy());
        return customerDTO;
    }

    private CustomerUpdateDTO toUpdateDto(Customer customer) {
        CustomerUpdateDTO customerUpdateDTO = new CustomerUpdateDTO();
        customerUpdateDTO.setName(customer.getName());
        customerUpdateDTO.setPhone(customer.getPhone());
        customerUpdateDTO.setDateOfBirth(customer.getDateOfBirth());
        customerUpdateDTO.setNationality(customer.getNationality());
        customerUpdateDTO.setVipStatus(customer.isVipStatus());
        if(customer.getUser() != null){
            customerUpdateDTO.setUser(customer.getUser().getId());
        }
        return customerUpdateDTO;
    }

    private CustomerCreateDTO toCreateDTO(Customer customer) {
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO();
        customerCreateDTO.setName(customer.getName());
        customerCreateDTO.setPhone(customer.getPhone());
        customerCreateDTO.setDateOfBirth(customer.getDateOfBirth());
        customerCreateDTO.setNationality(customer.getNationality());
        customerCreateDTO.setVipStatus(customer.isVipStatus());
        if(customer.getUser() != null){
            customerCreateDTO.setUser(customer.getUser().getId());
        }
        return customerCreateDTO;
    }

    private Customer toCustomerEntity(CustomerCreateDTO customerCreateDTO) {

        User user = userRepository.findById(customerCreateDTO.getUser()).orElseThrow(() -> new ResourceNotFoundException("customer",customerCreateDTO,"user","customers/create","An account with this id cannot be found"));
        if(user.getCustomer() != null){
            throw new DuplicateException("customer", customerCreateDTO, "user", "customers/create", "This user is already an customer");
        }
        Customer customer = new Customer();
        customer.setName(customerCreateDTO.getName());
        customer.setPhone(customerCreateDTO.getPhone());
        customer.setDateOfBirth(customerCreateDTO.getDateOfBirth());
        customer.setNationality(customerCreateDTO.getNationality());
        customer.setVipStatus(customerCreateDTO.isVipStatus());
        customer.setUser(user);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setStatus(StatusType.ACTIVE);
        customer.setCreatedBy(authService.getCurrentUser());

        Role customerRole = roleRepository.findByRoleName("CUSTOMER").orElseThrow(() -> new ResourceNotFoundException("customer",customerCreateDTO,"name","customers/create","Customer role cannot be found"));
        if(user.getRoles() == null){
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(customerRole);

        return customer;
    }
}
