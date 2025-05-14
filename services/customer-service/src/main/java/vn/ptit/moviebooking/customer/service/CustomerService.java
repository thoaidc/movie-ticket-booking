package vn.ptit.moviebooking.customer.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.ptit.moviebooking.customer.dto.request.SaveCustomerRequest;
import vn.ptit.moviebooking.customer.exception.BaseBadRequestException;
import vn.ptit.moviebooking.customer.repository.CustomerRepository;
import vn.ptit.moviebooking.customer.entity.Customer;

import java.util.Optional;

@Service
@Validated
public class CustomerService {

    private final CustomerRepository customerRepository;
    private static final String ENTITY_NAME = "CustomerService";

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerById(Integer customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, "Customer not exists");
        }

        return customerOptional.get();
    }

    @Transactional
    public Customer saveCustomerInfo(@Valid SaveCustomerRequest request) {
        Customer customer = new Customer();
        customer.setFullname(request.getFullname());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customerRepository.save(customer);
        return customer;
    }
}
