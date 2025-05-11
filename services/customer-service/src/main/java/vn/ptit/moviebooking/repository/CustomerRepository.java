package vn.ptit.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {}
