package vn.ptit.moviebooking.service;

import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.entity.Customer;
import vn.ptit.moviebooking.exception.BaseBadRequestException;
import vn.ptit.moviebooking.repository.CustomerRepository;

import java.util.Optional;

@Service
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
}
