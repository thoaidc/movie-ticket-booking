package vn.ptit.moviebooking.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.movie.entity.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {}
