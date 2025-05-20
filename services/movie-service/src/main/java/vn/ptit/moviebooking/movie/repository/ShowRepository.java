package vn.ptit.moviebooking.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.ptit.moviebooking.movie.dto.mapping.ShowDTO;
import vn.ptit.moviebooking.movie.entity.Show;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    @Query(
        value = """
            SELECT s.id,
                s.movie_id as movieId,
                s.cinema_room_id as roomId,
                s.ticket_price as ticketPrice,
                s.start_time as startTime,
                s.end_time as endTime,
                m.name as movie,
                cr.name as room
            FROM hdv_movie.show_time s
            JOIN hdv_movie.movie m ON s.movie_id = m.id
            JOIN hdv_movie.cinema_room cr on s.cinema_room_id = cr.id
            WHERE s.movie_id = ?1
        """,
        nativeQuery = true
    )
    List<ShowDTO> findAllByMovieId(Integer movieId);
}
