package vn.ptit.moviebooking.customer.service;

import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.customer.exception.BaseBadRequestException;
import vn.ptit.moviebooking.customer.repository.CustomerRepository;
import vn.ptit.moviebooking.customer.entity.Customer;

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
