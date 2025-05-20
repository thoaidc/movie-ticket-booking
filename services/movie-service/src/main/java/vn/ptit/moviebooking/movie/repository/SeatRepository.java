package vn.ptit.moviebooking.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.movie.entity.Seat;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    @Query(
        value = """
            SELECT s.* FROM hdv_movie.seat s
            JOIN hdv_movie.show_time sh ON s.cinema_room_id = sh.cinema_room_id
            WHERE sh.id = ?1
        """,
        nativeQuery = true
    )
    List<Seat> findAllByShowId(Integer showId);

    @Query(value = "SELECT s.id FROM seat s WHERE s.cinema_room_id = ?1", nativeQuery = true)
    List<Integer> findAllIdByCinemaRoomId(Integer roomId);
}
