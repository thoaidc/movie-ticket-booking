package vn.ptit.moviebooking.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.payment.entity.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {}
