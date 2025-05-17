package vn.ptit.moviebooking.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.movie.entity.CinemaRoom;

import java.util.List;

@Repository
public interface CinemaRoomRepository extends JpaRepository<CinemaRoom, Integer> {

    List<CinemaRoom> findAllByCinemaId(Integer cinemaId);
}
