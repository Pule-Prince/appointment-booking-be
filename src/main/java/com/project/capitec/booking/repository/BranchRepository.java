package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query("SELECT b FROM Branch b WHERE b.active = true ORDER BY b.code")
    List<Branch> findAllActive();
}
