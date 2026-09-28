package com.example.Job_Portal.employerEntity;

import com.example.Job_Portal.recruiter.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    public Optional<Employer>findByUserId(Long id);
    boolean existsByCompanyname(String companyname);
    public Employer findByCompanyname(String username);
}
