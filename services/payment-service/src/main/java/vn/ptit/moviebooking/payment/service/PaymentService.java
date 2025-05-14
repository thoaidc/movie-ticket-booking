package vn.ptit.moviebooking.payment.service;

import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.payment.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private static final String ENTITY_NAME = "PaymentService";

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
