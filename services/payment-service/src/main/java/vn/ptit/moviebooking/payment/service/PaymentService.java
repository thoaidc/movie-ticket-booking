package vn.ptit.moviebooking.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.moviebooking.payment.constants.PaymentConstants;
import vn.ptit.moviebooking.payment.dto.request.PaymentRequest;
import vn.ptit.moviebooking.payment.dto.request.RefundProcessCommand;
import vn.ptit.moviebooking.payment.entity.Payment;
import vn.ptit.moviebooking.payment.entity.Refund;
import vn.ptit.moviebooking.payment.repository.PaymentRepository;
import vn.ptit.moviebooking.payment.repository.RefundRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    public PaymentService(PaymentRepository paymentRepository, RefundRepository refundRepository) {
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
    }

    @Transactional
    public Payment createPayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setStatus(PaymentConstants.PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment paymentProcessTest(Payment payment, PaymentRequest paymentRequest) {
        if (Objects.equals(paymentRequest.getAtm(), "123") && Objects.equals(paymentRequest.getPin(), "123")) {
            payment.setPaymentTime(Instant.now());
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setStatus(PaymentConstants.PaymentStatus.COMPLETED);
            return paymentRepository.save(payment);
        } else {
            payment.setStatus(PaymentConstants.PaymentStatus.FAILED);
            return paymentRepository.save(payment);
        }
    }

    @Transactional
    public Refund refund(RefundProcessCommand refundRequest) {
        Refund refund = new Refund();
        refund.setReason(refundRequest.getReason());
        refund.setRefundTime(Instant.now());
        return refundRepository.save(refund);
    }
}
