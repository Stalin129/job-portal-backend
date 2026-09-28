package com.example.Job_Portal.jobs;

import com.example.Job_Portal.employerEntity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobsRepository extends JpaRepository<Jobs, Long> {
    List<Jobs>findByRoleAndLocation(String jobname,String location);
    List<Jobs> findByEmployerId(Long id);

}
