package com.example.Job_Portal.recruiter;

import com.example.Job_Portal.candidate.Candidates;
import com.example.Job_Portal.employerEntity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    List<Recruiter> findByEmployerId(Long id);
    Optional<Recruiter> findByUserId(Long id);

}
