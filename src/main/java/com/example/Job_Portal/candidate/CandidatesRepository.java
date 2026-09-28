package com.example.Job_Portal.candidate;

import com.example.Job_Portal.userEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatesRepository extends JpaRepository<Candidates, Long> {

    Optional<Candidates> findByUserId(Long id);

}
