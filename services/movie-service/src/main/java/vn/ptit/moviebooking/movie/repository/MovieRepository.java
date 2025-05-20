package vn.ptit.moviebooking.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ptit.moviebooking.movie.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query(
        value = """
            SELECT DISTINCT m.*, earliest.earliest_start
            FROM hdv_movie.movie m
            JOIN (
              SELECT st.movie_id, MIN(st.start_time) AS earliest_start
              FROM hdv_movie.show_time st
              GROUP BY st.movie_id
            ) AS earliest ON m.id = earliest.movie_id
            JOIN hdv_movie.show_time st ON st.movie_id = m.id AND st.start_time = earliest.earliest_start
            WHERE (:keyword IS NULL OR (m.name LIKE :keyword OR m.director LIKE :keyword OR m.genre LIKE :keyword))
                AND (:fromDate IS NULL OR st.start_time >= :fromDate)
                AND (:toDate IS NULL OR st.start_time <= :toDate)
            ORDER BY earliest.earliest_start
        """,
        nativeQuery = true
    )
    Page<Movie> findAllWithPaging(
        @Param("keyword") String keyword,
        @Param("fromDate") String fromDate,
        @Param("toDate") String toDate,
        Pageable pageable
    );
}
