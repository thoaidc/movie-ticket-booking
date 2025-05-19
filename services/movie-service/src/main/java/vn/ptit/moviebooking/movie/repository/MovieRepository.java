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
            SELECT * FROM hdv_movie.movie m
            WHERE (:keyword IS NULL OR (m.name LIKE :keyword OR m.director LIKE :keyword OR m.genre LIKE :keyword))
                AND (:fromDate IS NULL OR m.release_date >= :fromDate)
                AND (:toDate IS NULL OR m.release_date <= :toDate)
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
