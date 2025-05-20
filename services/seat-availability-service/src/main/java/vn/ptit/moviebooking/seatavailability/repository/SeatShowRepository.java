package vn.ptit.moviebooking.seatavailability.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.seatavailability.entity.SeatShow;

import java.util.Collection;
import java.util.List;

@Repository
public interface SeatShowRepository extends JpaRepository<SeatShow, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SeatShow s WHERE s.id IN :ids AND s.status = :status")
    List<SeatShow> findAllByIdInAndStatusForUpdate(@Param("ids") Collection<Integer> ids, @Param("status") String status);

    List<SeatShow> findAllByShowId(Integer showId);
}
