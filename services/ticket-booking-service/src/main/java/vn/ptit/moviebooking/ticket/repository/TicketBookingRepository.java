package vn.ptit.moviebooking.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.ticket.entity.Booking;

@Repository
public interface TicketBookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT b.customer_id FROM booking b WHERE b.id = ?1", nativeQuery = true)
    Integer findCustomerIdByBookingId(Integer bookingId);
}
