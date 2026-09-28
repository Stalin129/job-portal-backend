package com.example.Job_Portal.jobAppllication;

import com.example.Job_Portal.jobs.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationsRepository extends JpaRepository<JobApplications, Long> {
 List<JobApplications>findByUser_Id(Long userId);
 boolean existsByUserIdAndJobsId(Long userId, Long jobId);
 public List<JobApplications>findByJobsId(Long id);
 JobApplications findByJobsIdAndUserId(Long jobId,Long userId);
}
