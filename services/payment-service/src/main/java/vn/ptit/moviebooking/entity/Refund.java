package vn.ptit.moviebooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Entity
@Table(name = "refund")
@DynamicInsert
@DynamicUpdate
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", length = 10, nullable = false)
    private String status;

    @Column(name = "refund_time", nullable = false)
    private Instant refundTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Instant refundTime) {
        this.refundTime = refundTime;
    }
}
