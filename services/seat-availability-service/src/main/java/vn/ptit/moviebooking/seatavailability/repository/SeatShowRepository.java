package vn.ptit.moviebooking.seatavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.seatavailability.entity.SeatShow;

import java.util.Collection;
import java.util.List;

@Repository
public interface SeatShowRepository extends JpaRepository<SeatShow, Integer> {

    List<SeatShow> findAllByIdInAndStatus(Collection<Integer> id, String status);
}
