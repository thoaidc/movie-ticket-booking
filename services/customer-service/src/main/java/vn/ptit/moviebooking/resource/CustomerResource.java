package vn.ptit.moviebooking.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ptit.moviebooking.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public BaseResponseDTO getCustomerById(@PathVariable Integer customerId) {
        return BaseResponseDTO.builder().ok(customerService.getCustomerById(customerId));
    }
}
