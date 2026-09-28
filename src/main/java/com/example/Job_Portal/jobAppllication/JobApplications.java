package com.example.Job_Portal.jobAppllication;

import com.example.Job_Portal.jobs.Jobs;
import com.example.Job_Portal.userEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_applications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "job_id"})
        }

)
public class JobApplications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Jobs jobs;
    private String resume;
    private String status;
    private LocalDateTime appliedAt;

    @PrePersist
    public void onCreate() {
        appliedAt = LocalDateTime.now();
    }
}
